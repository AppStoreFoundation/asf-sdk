package com.asf.appcoins.sdk.ads.poa.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import com.asf.appcoins.sdk.ads.BuildConfig;
import com.asf.appcoins.sdk.ads.LifeCycleListener;
import com.asf.appcoins.sdk.ads.poa.PoAServiceConnector;
import com.asf.appcoins.sdk.ads.poa.campaign.Campaign;
import com.asf.appcoins.sdk.ads.poa.campaign.CampaignContract;
import com.asf.appcoins.sdk.ads.poa.campaign.CampaignContractImpl;
import com.asf.appcoins.sdk.core.web3.AsfWeb3j;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import org.web3j.abi.datatypes.Address;

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

public class PoAManager implements LifeCycleListener.Listener {

  private static final String FINISHED_KEY = "finished";

  public static final String TAG = PoAManager.class.getName();
  /** The instance of the manager */
  private static PoAManager instance;
  /** The connector with the wallet service, receiver of the messages of the PoA. */
  private static PoAServiceConnector poaConnector;
  /** The application context */
  private static Context appContext;
  /** integer used to identify the network to wich we are connected */
  private static int network = 0;
  private static CampaignContract campaignContract;
  private static String country;
  private final SharedPreferences preferences;
  /** boolean indicating if we are already processing a PoA */
  private boolean processing;
  /** The handle to keep the runnable tasks that we be running within a certain period */
  private Handler handler = new Handler();
  /** The runnnable taks that will be trigger periodically */
  private Runnable sendProof;
  /** integer used to track how many proof were already sent */
  private int proofsSent = 0;
  private BigInteger campaignId;

  public PoAManager(SharedPreferences preferences) {
    this.preferences = preferences;
  }

  /**
   * Initialisation method for the manager
   *
   * @param context The context of the application.
   * @param connector The PoA service connector used on the communication of the proof of attention.
   */
  public static PoAManager init(Context context, PoAServiceConnector connector, int networkId,
      AsfWeb3j asfWeb3j, Address contractAddress, String countryId) {
    if (instance == null) {
      SharedPreferences preferences =
          context.getSharedPreferences("PoAManager", Context.MODE_PRIVATE);
      instance = new PoAManager(preferences);
      poaConnector = connector;
      appContext = context;
      network = networkId;
      campaignContract = new CampaignContractImpl(asfWeb3j, contractAddress);
      country = countryId;
    }
    return instance;
  }

  /**
   * Getter for the instance of the manager.
   *
   * @param context The application context
   * @param connector The onnector to the wallet service.
   */
  public static PoAManager get(Context context, PoAServiceConnector connector, int networkId,
      AsfWeb3j asfWeb3j, Address contractAddress, String countryId) {
    if (instance == null) {
      init(context, connector, networkId, asfWeb3j, contractAddress, countryId);
    }
    return instance;
  }

  /**
   * Method that starts the process. This will trigger the handshake required to communicate
   * with the wallet service, if not already done.
   * Then it will trigger the first proof sent.
   */
  private void startProcess() {
    // If starting the PoA process do handshake
    if (!processing) {
      processing = true;
      poaConnector.startHandshake(appContext);
    }

    // set the network being used
    Bundle bundle = new Bundle();
    bundle.putString("packageName", appContext.getPackageName());
    bundle.putInt("networkId", network);

    poaConnector.sendMessage(appContext, MSG_SET_NETWORK, bundle);

    handleCampaign();

    sendProof();
  }

  /**
   * Method that stops the process. It will send a message to the listening wallet to stop the
   * process and call the finish process method.
   */
  public void stopProcess() {
    if (processing) {
      Bundle bundle = new Bundle();
      bundle.putString("packageName", appContext.getPackageName());
      poaConnector.sendMessage(appContext, MSG_STOP_PROCESS, bundle);
      finishProcess();
    }
  }

  /**
   * Method that finish the process. It will remove any running tasks and disconnect from the wallet
   * service.
   */
  public void finishProcess() {
    processing = false;
    proofsSent = 0;

    if (sendProof != null) {
      handler.removeCallbacks(sendProof);
    }

    poaConnector.disconnectFromService(appContext);
  }

  /**
   * Method that send a proof. The checks how many were sent and if bellow the number of proofs
   * required to be sent, schedules a new send proof to be triggered after defined delay.
   * If all proofs were sent, it stops the process.
   */
  private void sendProof() {
    // Connection to service may already been done, but we still need to make sure that it is
    // connected. In case no connection is not yet done, the message is stored to be sent as soon as
    // the connection is done.
    poaConnector.connectToService(appContext);
    // send proof
    long timestamp = System.currentTimeMillis();
    Bundle bundle = new Bundle();
    bundle.putString("packageName", appContext.getPackageName());
    bundle.putLong("timeStamp", timestamp);
    poaConnector.sendMessage(appContext, MSG_SEND_PROOF, bundle);
    proofsSent++;

    // schedule the next proof sending
    if (proofsSent < BuildConfig.ADS_POA_NUMBER_OF_PROOFS) {
      handler.postDelayed(sendProof = this::sendProof,
          BuildConfig.ADS_POA_PROOFS_INTERVAL_IN_MILIS);
    } else {
      // or stop the process
      processing = false;
      preferences.edit()
          .putBoolean(FINISHED_KEY, true)
          .apply();
      finishProcess();
    }
  }
  public List<Campaign> getActiveCampaigns(String packageName, BigInteger vercode)
      throws IOException {
    List<BigInteger> campaignsIdsByCountry = campaignContract.getCampaignsByCountry(country);
    List<BigInteger> campaignsIdsByCountryWl = campaignContract.getCampaignsByCountry("WL");

    campaignsIdsByCountry.addAll(campaignsIdsByCountryWl);

    List<Campaign> campaign = new LinkedList<>();

    for (BigInteger bidId : campaignsIdsByCountry) {
      String campaignPackageName = campaignContract.getPackageNameOfCampaign(bidId);
      List<BigInteger> vercodes = campaignContract.getVercodesOfCampaign(bidId);
      boolean campaignValid = campaignContract.isCampaignValid(bidId);

      boolean addCampaign =
          campaignPackageName.equals(packageName) && vercodes.contains(vercode) && campaignValid;

      if (addCampaign) {
        campaign.add(new Campaign(bidId, vercodes, country));
      }
    }

    return campaign;
  }

  private void handleCampaign() {
    if (hasNetwork()) {
      String packageName = appContext.getPackageName();
      Disposable subscribe = Single.fromCallable(() -> getVerCode(appContext, packageName))
          .subscribeOn(Schedulers.io())
          .map(verCode -> getActiveCampaigns(packageName, BigInteger.valueOf(verCode)))
          .subscribe(campaigns -> {
            if (campaigns.isEmpty()) {
              stopProcess();
            } else {
              BigInteger campaignId = campaigns.get(0)
                  .getId();

              Bundle bundle = new Bundle();
              bundle.putString("packageName", appContext.getPackageName());
              bundle.putString("campaignId", campaignId.toString());

              poaConnector.sendMessage(appContext, MSG_REGISTER_CAMPAIGN, bundle);

              this.campaignId = campaignId;
            }
          });
    }
  }

  private boolean hasNetwork() {
    NetworkInfo activeNetworkInfo = ((ConnectivityManager) appContext.getSystemService(
        Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

  private int getVerCode(Context context, String packageName)
      throws PackageManager.NameNotFoundException {
    return context.getPackageManager()
        .getPackageInfo(packageName, 0).versionCode;
  }

  @Override public void onBecameForeground() {
    if (!preferences.getBoolean(FINISHED_KEY, false)) {
      startProcess();
    }
  }

  @Override public void onBecameBackground() {
    stopProcess();
  }
}
