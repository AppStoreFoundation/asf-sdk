package com.asf.appcoins.sdk.ads.poa.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import com.asf.appcoins.sdk.ads.BuildConfig;
import com.asf.appcoins.sdk.ads.WalletPoAServiceListenner;
import com.asf.appcoins.sdk.ads.network.AppCoinsClient;
import com.asf.appcoins.sdk.ads.network.QueryParams;
import com.asf.appcoins.sdk.ads.network.listeners.CheckConnectivityResponseListener;
import com.asf.appcoins.sdk.ads.network.listeners.GetCampaignResponseListener;
import com.asf.appcoins.sdk.ads.network.responses.AppCoinsClientResponse;
import com.asf.appcoins.sdk.ads.network.responses.ConnectivityResponse;
import com.asf.appcoins.sdk.ads.network.responses.GetCampaignResponse;
import com.asf.appcoins.sdk.ads.network.threads.CheckConnectivityRetry;
import com.asf.appcoins.sdk.ads.poa.PoAServiceConnector;
import com.asf.appcoins.sdk.ads.poa.campaign.Campaign;
import com.asf.appcoins.sdk.ads.poa.campaign.CampaignMapper;
import com.asf.appcoins.sdk.ads.repository.AppcoinsAdvertisementConnection;
import com.asf.appcoins.sdk.ads.repository.AppcoinsAdvertisementListenner;
import com.asf.appcoins.sdk.ads.repository.AppcoinsAdvertisementRepository;
import com.asf.appcoins.sdk.ads.repository.AppcoinsAdvertisementThreadGetCampaign;
import com.asf.appcoins.sdk.ads.repository.ResponseCode;
import com.sdk.appcoins_lifecycle.lifecycle.Lifecycle;
import com.sdk.appcoins_lifecycle.lifecycle.LifecycleObserver;
import com.sdk.appcoins_lifecycle.lifecycle.common.OnLifecycleEvent;
import java.math.BigInteger;

import static com.asf.appcoins.sdk.ads.poa.MessageListener.MSG_REGISTER_CAMPAIGN;
import static com.asf.appcoins.sdk.ads.poa.MessageListener.MSG_SEND_PROOF;
import static com.asf.appcoins.sdk.ads.poa.MessageListener.MSG_SET_NETWORK;
import static com.asf.appcoins.sdk.ads.poa.MessageListener.MSG_STOP_PROCESS;

/**
 * Class that will manage the PoA process, by sending the proofs on the correct time. By handling
 * when the process is finished or is stopped.
 *
 * Created by Joao Raimundo on 06/04/2018.
 */

public class PoAManager
    implements LifecycleObserver, CheckConnectivityResponseListener, GetCampaignResponseListener,
    DialogVisibleListener, WalletPoAServiceListenner {

  public static final String TAG = PoAManager.class.getName();
  private static final String FINISHED_KEY = "finished";
  private static final int PREFERENCES_LISTENER_DELAY = 1000;
  /** The instance of the manager */
  private static PoAManager instance;
  private static boolean showPopUpNotification;
  private static String POA_NOTIFICATION_VALUE = "POA_NOTIFICATION";
  private final SharedPreferences preferences;
  private final AppCoinsClient appcoinsClient;
  boolean fromBackground = false;
  /** The connector with the wallet service, receiver of the messages of the PoA. */
  private PoAServiceConnector poaConnector;
  /** The application context */
  private Context appContext;
  /** integer used to identify the network to wich we are connected */
  private int network = 0;
  /** boolean indicating if we are already processing a PoA */
  private boolean processing;
  /** The handle to keep the runnable tasks that we be running within a certain period */
  private Handler handler = new Handler();
  /** The runnnable taks that will be trigger periodically */
  private Runnable sendProof;
  /** The handle to keep the runnable tasks that we be running within a certain period */
  private Handler spHandler = new Handler();
  /** The runnnable taks that will be trigger periodically */
  private Runnable spListener;
  /** integer used to track how many proof were already sent */
  private int proofsSent = 0;
  /** The campaign ID value */
  private BigInteger campaignId;
  private boolean foreground = false;
  private boolean dialogVisible = false;
  private Handler handleRetryConnection = new Handler();
  private int connectionRetries = 0;
  private boolean isWalletInstalled;
  private AppcoinsAdvertisementRepository appcoinsAdvertisementRepository;
  private AppcoinsAdvertisementConnection appcoinsAdvertisementConnection;

  public PoAManager(SharedPreferences preferences, PoAServiceConnector connector, Context context,
      int networkId, AppCoinsClient appcoinsClient) {
    this.preferences = preferences;
    this.poaConnector = connector;
    this.appContext = context;
    this.network = networkId;
    this.appcoinsClient = appcoinsClient;
  }

  /**
   * Getter for the instance of the manager.
   */
  public static PoAManager get() {
    return instance;
  }

  /**
   * Initialisation method for the manager
   *
   * @param context The context of the application.
   * @param connector The PoA service connector used on the communication of the proof of attention.
   */
  public static void init(Context context, PoAServiceConnector connector, int networkId)
      throws PackageManager.NameNotFoundException {
    if (instance == null) {
      WalletUtils.setContext(context);
      SharedPreferences preferences =
          context.getSharedPreferences("PoAManager", Context.MODE_PRIVATE);

      String packageName = context.getPackageName();
      instance = new PoAManager(preferences, connector, context, networkId,
          createAppCoinsClient(packageName, getVerCode(context, packageName), networkId));
    }
  }

  private static AppCoinsClient createAppCoinsClient(String packageName, int versionCode,
      int networkId) {
    boolean isDebug = networkId != 1;
    String url;
    if (isDebug) {
      url = BuildConfig.DEV_BACKEND_BASE_HOST;
    } else {
      url = BuildConfig.PROD_BACKEND_BASE_HOST;
    }
    return new AppCoinsClient(packageName, versionCode, url, new LogInterceptor());
  }

  private static int getVerCode(Context context, String packageName)
      throws PackageManager.NameNotFoundException {
    return context.getPackageManager()
        .getPackageInfo(packageName, 0).versionCode;
  }

  /**
   * Method that starts the process. This will trigger the handshake required to communicate
   * with the wallet service, if not already done.
   * Then it will trigger the first proof sent.
   */
  public void startProcess() {
    processing = true;

    // set the network being used
    Bundle bundle = new Bundle();
    bundle.putString("packageName", appContext.getPackageName());
    bundle.putInt("networkId", network);

    poaConnector.sendMessage(appContext, MSG_SET_NETWORK, bundle);
  }

  /**
   * Method that stops the process. First removes pending tasks for proofs to be sent and shared
   * preferences change listener. Then sends message to the listening wallet to stop the process
   * and call the finish process method.
   */
  public void stopProcess() {
    if (processing) {
      if (sendProof != null) {
        handler.removeCallbacks(sendProof);
      }

      if (spListener != null) {
        spHandler.removeCallbacks(spListener);
      }

      Log.d(TAG, "Stopping process.");
      Bundle bundle = new Bundle();
      bundle.putString("packageName", appContext.getPackageName());
      poaConnector.sendMessage(appContext, MSG_STOP_PROCESS, bundle);
      finishProcess();
    }
  }

  /**
   * Method that finish the process. The method simply disconnects from the bound service.
   */
  public void finishProcess() {
    Log.d(TAG, "Finishing process.");
    processing = false;
    proofsSent = 0;
    campaignId = null;
    poaConnector.disconnectFromService(appContext);
  }

  /**
   * Method that send a proof. The checks how many were sent and if bellow the number of proofs
   * required to be sent, schedules a new send proof to be triggered after defined delay.
   * If all proofs were sent, it stops the process.
   */
  private void sendProof() {
    if (foreground) {
      if (fromBackground) {
        fromBackground = false;

        if (proofsSent < BuildConfig.ADS_POA_NUMBER_OF_PROOFS) {
          postponeSendProof();
        }

        Log.e(TAG, "Proof " + (proofsSent + 1) + " skipped! Came from background!");
      } else {
        long timestamp = System.currentTimeMillis();
        Bundle bundle = new Bundle();
        bundle.putString("packageName", appContext.getPackageName());
        bundle.putLong("timeStamp", timestamp);
        poaConnector.sendMessage(appContext, MSG_SEND_PROOF, bundle);
        proofsSent++;
        Log.e(TAG, "Proof " + proofsSent + " sent!");
        // schedule the next proof sending
        if (proofsSent < BuildConfig.ADS_POA_NUMBER_OF_PROOFS) {
          postponeSendProof();
        } else {
          // or stop the process
          if (campaignId != null && !getSharedPreferencesBoolean(FINISHED_KEY)) {
            setSharedPreferencesBoolean(FINISHED_KEY, true);
            finishProcess();
          }
        }
      }
    } else {
      if (proofsSent < BuildConfig.ADS_POA_NUMBER_OF_PROOFS) {
        postponeSendProof();

        Log.e(TAG, "Proof " + (proofsSent + 1) + " skipped! Application is background!");
      }
    }
  }

  private void postponeSendProof() {
    handler.postDelayed(new Runnable() {
      @Override public void run() {
        sendProof();
      }
    }, BuildConfig.ADS_POA_PROOFS_INTERVAL_IN_MILIS);
  }

  private void handleCampaign() {
    ConnectivityResponse connectivityResponse = new ConnectivityResponse(this);
    if (campaignId == null) {
      appcoinsClient.checkConnectivity(connectivityResponse);
    }
  }

  /**
   * Method to check if we have the wallet package name available to start the PoA process.
   * If not available start a runnable in 1 second to check again.
   * If the available start process.
   */
  private void checkPreferencesForPackage() {
    if (!processing) {
      if (foreground) {
        Log.d(TAG, "Starting PoA process");
        startProcess();
      } else {
        spHandler.postDelayed(new Runnable() {
          @Override public void run() {
            checkPreferencesForPackage();
          }
        }, PREFERENCES_LISTENER_DELAY);
      }
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP) public void onBackground() {
    foreground = false;
    fromBackground = true;
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START) public void onForeground() {
    isWalletInstalled = WalletUtils.hasWalletInstalled();
    foreground = true;

    if (isWalletInstalled) {
      WalletUtils.removeNotification();
    }

    if (!getSharedPreferencesBoolean(FINISHED_KEY)) {
      handleCampaign();
    }
  }

  private void sendMSGRegisterCampaign(String packageName, String id) {
    Bundle bundle = new Bundle();
    bundle.putString("packageName", packageName);
    bundle.putString("campaignId", id);
    poaConnector.sendMessage(appContext, MSG_REGISTER_CAMPAIGN, bundle);
  }

  private void initiateProofSending() {
    if (proofsSent < BuildConfig.ADS_POA_NUMBER_OF_PROOFS) {
      postponeSendProof();
    }
  }

  @Override public void OnDialogVisibleListener(boolean value) {
    dialogVisible = value;
  }

  /*
   * @Param value -> true if there is connectivity,false otherwise.
   * Method that the response of the function checkConnectivity.
   * If there is connectivity executes GetCampaign
   * If not retry in x mills until regainings Connectivity.
   */
  @Override public void responseConnectivity(boolean hasConnectivity) {
    if (hasConnectivity) {
      Log.d("Message:", "Connectivity Available");
      retrieveCampaign();
    } else {
      Log.d("Message:", "Connectivity Not available Available");
      if (connectionRetries < BuildConfig.ADS_CONNECTION_RETRYS_NUMBER) {
        connectionRetries++;
        int delayedTime = connectionRetries * BuildConfig.ADS_CONNECTIVITY_RETRY_IN_MILLS;
        CheckConnectivityRetry checkConnectivityRetry = new CheckConnectivityRetry(this);
        handleRetryConnection.postDelayed(checkConnectivityRetry, delayedTime);
      } else {
        Log.d("Message:", "Connectivity Retry exceeded..");
      }
    }
  }

  private void retrieveCampaign() {
    if (isWalletInstalled) {
      Log.d(TAG, "Wallet Installed");
      startWalletConnection();
    } else {
      Log.d(TAG, "No Wallet Installed");
      Log.d(TAG, "Checking for available campaigns");
      QueryParams queryParams = new QueryParams("desc", "price", "true", "BDS");
      GetCampaignResponse getCampaignResponse = new GetCampaignResponse(this);
      appcoinsClient.getCampaign(queryParams, getCampaignResponse);
    }
  }

  /*
   * @Param value -> response object from the GetCampaign service.
   * Method that handles the GetCampaignService response.
   * If not retry in x mills until regainings Connectivity.
   */
  @Override public void responseGetCampaign(AppCoinsClientResponse appCoinsClientResponse) {
    Campaign campaign = CampaignMapper.mapCampaign(appCoinsClientResponse);
    processCampaign(campaign);
  }

  @Override public void responseGetCampaignWallet(Bundle response) {
    Campaign campaign = CampaignMapper.mapCampaignFromBundle(response);
    processCampaign(campaign);
  }

  private void processCampaign(Campaign campaign) {
    if (!campaign.hasCampaign()) {
      stopProcess();
    } else {
      if (isWalletInstalled) {
        startCampaign(campaign);
      } else {
        promptWalletNotification();
      }
    }
  }

  private void startCampaign(Campaign campaign) {
    this.campaignId = campaign.getId();
    Log.d(TAG, "CampaignID:" + campaignId + " PackageName: " + appContext.getPackageName());
    if (poaConnector.isConnectionReady()) {
      isConnected();
    } else {
      if (!poaConnector.connectToService(appContext, this)) {
        Log.d(TAG, "No Service to be Binded");
      }
    }
  }

  private void promptWalletNotification() {
    if (!getSharedPreferencesBoolean(POA_NOTIFICATION_VALUE)) {
      Log.d(TAG, "Prompting Notification Install");
      setSharedPreferencesBoolean(POA_NOTIFICATION_VALUE, true);
      showPopUpNotification = true;
      spHandler.post(new Runnable() {
        @Override public void run() {
          WalletUtils.createInstallWalletNotification();
        }
      });
    }
  }

  private void startWalletConnection() {
    appcoinsAdvertisementRepository = new AppcoinsAdvertisementRepository();
    appcoinsAdvertisementConnection =
        new AppcoinsAdvertisementConnection(appContext, appcoinsAdvertisementRepository);
    final PoAManager p = this;
    appcoinsAdvertisementConnection.startConnection(new AppcoinsAdvertisementListenner() {
      @Override public void onAdvertisementFinished(int responseCode) {
        if (responseCode == ResponseCode.OK.getValue()) {
          Log.d(TAG, "Retrieving the Campaign by the Wallet");
          AppcoinsAdvertisementThreadGetCampaign appcoinsAdvertisementThreadGetCampaign =
              new AppcoinsAdvertisementThreadGetCampaign(p, appcoinsAdvertisementRepository);
          Thread t = new Thread(appcoinsAdvertisementThreadGetCampaign);
          t.start();
        } else {
          Log.d(TAG, "Coudn't connect to the wallet");
          stopProcess();
        }
      }
    });
  }

  private void setSharedPreferencesBoolean(String key, boolean value) {
    preferences.edit()
        .putBoolean(key, value)
        .apply();
  }

  private boolean getSharedPreferencesBoolean(String key) {
    return preferences.getBoolean(key, false);
  }

  /*
   * New listenner to be called when connection to WalletPoaService is succefully done.
   */
  @Override public void isConnected() {
    try {
      poaConnector.registerCampaign(appContext, campaignId.toString());
      checkPreferencesForPackage();
      sendMSGRegisterCampaign(appContext.getPackageName(), campaignId.toString());
      initiateProofSending();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
}
