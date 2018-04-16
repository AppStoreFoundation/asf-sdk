package com.asf.appcoins.sdk.ads;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.asf.appcoins.sdk.ads.campaign.manager.CampaignManager;
import com.asf.appcoins.sdk.ads.poa.PoAManager;
import com.asf.appcoins.sdk.ads.poa.PoAServiceConnector;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.math.BigInteger;

import static com.asf.appcoins.sdk.ads.poa.MessageListener.MSG_REGISTER_CAMPAIGN;
import static com.asf.appcoins.sdk.ads.poa.MessageListener.MSG_SEND_PROOF;

/**
 * Created by Joao Raimundo on 01-03-2018.
 */

final class AdvertisementSdkImpl implements AdvertisementSdk {

  private final PoAServiceConnector poaConnector;
  private final CampaignManager campaignManager;

  private Context context;

  private int networkId;

  AdvertisementSdkImpl(PoAServiceConnector poaConnector, int networkId,
      CampaignManager campaignManager) {
    this.poaConnector = poaConnector;
    this.networkId = networkId;
    this.campaignManager = campaignManager;
  }


  @Override public void handshake() {
    poaConnector.startHandshake(context);
  }

  @Override public void sendProof() {
    poaConnector.connectToService(context);
    long timestamp = System.currentTimeMillis();
    Bundle bundle = new Bundle();
    bundle.putString("packageName", context.getPackageName());
    bundle.putLong("timeStamp", timestamp);
    poaConnector.sendMessage(context, MSG_SEND_PROOF, bundle);
  }

  @Override public void registerCampaign(String campaignId) {
    poaConnector.connectToService(context);
    Bundle bundle = new Bundle();
    bundle.putString("packageName", context.getPackageName());
    bundle.putString("campaignId", campaignId);
    poaConnector.sendMessage(context, MSG_REGISTER_CAMPAIGN, bundle);
  }

  @Override public void setNetwork(int networkId) {
    if (poaConnector.connectToService(context)) {
      Bundle bundle = new Bundle();
      bundle.putInt("networkId", networkId);
      poaConnector.sendMessage(context, MSG_REGISTER_CAMPAIGN, bundle);
    }
  }

  @Override public void init(Application application) {
    this.context = application;
    LifeCycleListener.get(application).setListener(PoAManager.get(application, poaConnector, networkId));

    handleCampaign(application, PoAManager.get(application, poaConnector, networkId));
  }

  private void handleCampaign(Context context, PoAManager poAManager) {

    String packageName = context.getPackageName();
    Disposable subscribe = Single.fromCallable(() -> getVerCode(context, packageName))
        .subscribeOn(Schedulers.io())
        .map(
            verCode -> campaignManager.getActiveCampaigns(packageName, BigInteger.valueOf(verCode)))
        .subscribe(campaigns -> {
          if (campaigns.isEmpty()) {
            poAManager.stopProcess();
          } else {
            registerCampaign(campaigns.get(0)
                .getId()
                .toString());
          }
        });
  }

  private int getVerCode(Context context, String packageName)
      throws PackageManager.NameNotFoundException {
    return context.getPackageManager()
        .getPackageInfo(packageName, 0).versionCode;
  }
}
