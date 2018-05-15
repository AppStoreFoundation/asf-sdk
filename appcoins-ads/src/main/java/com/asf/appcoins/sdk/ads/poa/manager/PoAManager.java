package com.asf.appcoins.sdk.ads.poa.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.asf.appcoins.sdk.ads.BuildConfig;
import com.asf.appcoins.sdk.ads.LifeCycleListener;
import com.asf.appcoins.sdk.ads.R;
import com.asf.appcoins.sdk.ads.ip.IpApi;
import com.asf.appcoins.sdk.ads.ip.IpResponse;
import com.asf.appcoins.sdk.ads.poa.PoAServiceConnector;
import com.asf.appcoins.sdk.ads.poa.campaign.Campaign;
import com.asf.appcoins.sdk.ads.poa.campaign.CampaignContract;
import com.asf.appcoins.sdk.ads.poa.campaign.CampaignContractImpl;
import com.asf.appcoins.sdk.core.util.wallet.WalletUtils;
import com.asf.appcoins.sdk.core.web3.AsfWeb3j;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import net.grandcentrix.tray.AppPreferences;
import net.grandcentrix.tray.core.OnTrayPreferenceChangeListener;
import net.grandcentrix.tray.core.TrayItem;
import org.web3j.abi.datatypes.Address;

import static com.asf.appcoins.sdk.ads.poa.MessageListener.MSG_REGISTER_CAMPAIGN;
import static com.asf.appcoins.sdk.ads.poa.MessageListener.MSG_SEND_PROOF;
import static com.asf.appcoins.sdk.ads.poa.MessageListener.MSG_SET_NETWORK;
import static com.asf.appcoins.sdk.ads.poa.MessageListener.MSG_STOP_PROCESS;
import static com.asf.appcoins.sdk.ads.poa.PoAServiceConnector.PREFERENCE_WALLET_PCKG_NAME;

/**
 * Class that will manage the PoA process, by sending the proofs on the correct time. By handling
 * when the process is finished or is stopped.
 *
 * Created by Joao Raimundo on 06/04/2018.
 */

public class PoAManager implements LifeCycleListener.Listener {

  public static final String TAG = PoAManager.class.getName();
  private static final String FINISHED_KEY = "finished";
  /** The instance of the manager */
  private static PoAManager instance;
  /** The connector with the wallet service, receiver of the messages of the PoA. */
  private static PoAServiceConnector poaConnector;
  /** The application context */
  private static Context appContext;
  /** integer used to identify the network to wich we are connected */
  private static int network = 0;
  private static CampaignContract campaignContract;
  private final CompositeDisposable compositeDisposable;
  private final SharedPreferences preferences;
  /** boolean indicating if we are already processing a PoA */
  private boolean processing;
  /** The handle to keep the runnable tasks that we be running within a certain period */
  private Handler handler = new Handler();
  /** The runnnable taks that will be trigger periodically */
  private Runnable sendProof;
  /** integer used to track how many proof were already sent */
  private int proofsSent = 0;
  /** The campaign ID value */
  private BigInteger campaignId;

  private boolean foreground = false;
  private boolean dialogVisible = false;

  public PoAManager(SharedPreferences preferences) {
    this.preferences = preferences;
    this.compositeDisposable = new CompositeDisposable();
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
  public static void init(Context context, PoAServiceConnector connector, int networkId,
      AsfWeb3j asfWeb3j, Address contractAddress) {
    if (instance == null) {
      SharedPreferences preferences =
          context.getSharedPreferences("PoAManager", Context.MODE_PRIVATE);
      instance = new PoAManager(preferences);
      PoAManager.poaConnector = connector;
      PoAManager.appContext = context;
      PoAManager.network = networkId;
      PoAManager.campaignContract = new CampaignContractImpl(asfWeb3j, contractAddress);
    }
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
    String countryId = IpApi.create()
        .myIp()
        .map(IpResponse::getCountryCode)
        .subscribeOn(Schedulers.io())
        .doOnError(throwable -> Log.w(TAG, "createAdvertisementSdk: Failed to get country code!",
            throwable))
        .blockingFirst();

    List<BigInteger> campaignsIdsByCountry = campaignContract.getCampaignsByCountry(countryId);
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
        campaign.add(new Campaign(bidId, vercodes, countryId));
      }
    }

    return campaign;
  }

  private void handleCampaign() {
    String packageName = appContext.getPackageName();

    compositeDisposable.add(ReactiveNetwork.observeInternetConnectivity()
        .subscribeOn(Schedulers.io())
        .filter(hasInternet -> hasInternet)
        .filter(hasInternet -> this.campaignId == null)
        .map(__ -> getVerCode(appContext, packageName))
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
        }));
  }

  private int getVerCode(Context context, String packageName)
      throws PackageManager.NameNotFoundException {
    return context.getPackageManager()
        .getPackageInfo(packageName, 0).versionCode;
  }

  @Override public void onBecameForeground(Activity activity) {
    foreground = true;

    if (!preferences.getBoolean(FINISHED_KEY, false)) {
      if (!WalletUtils.hasWalletInstalled(activity) && !dialogVisible) {
        Disposable disposable = WalletUtils.promptToInstallWallet(activity,
            activity.getString(R.string.install_wallet_from_ads))
            .toCompletable()
            .doOnSubscribe(disposable1 -> dialogVisible = true)
            .doOnComplete(() -> dialogVisible = false)
            .subscribe(() -> {
            }, Throwable::printStackTrace);
      } else {
        final AppPreferences appPreferences =
            new AppPreferences(appContext); // this Preference comes for free from the library
        appPreferences.registerOnTrayPreferenceChangeListener(new OnTrayPreferenceChangeListener() {
          @Override public void onTrayPreferenceChanged(Collection<TrayItem> items) {
            if (foreground && appPreferences.contains(PREFERENCE_WALLET_PCKG_NAME)) {
              startProcess();
            }
          }
        });
        poaConnector.startHandshake(appContext, network);
      }
    }
  }

  @Override public void onBecameBackground() {
    foreground = false;

    stopProcess();
    compositeDisposable.dispose();
  }
}
