package com.asf.appcoins.sdk.ads;

import android.content.Context;
import android.util.Log;
import com.asf.appcoins.sdk.ads.campaign.manager.CampaignManager;
import com.asf.appcoins.sdk.ads.ip.IpApi;
import com.asf.appcoins.sdk.ads.ip.IpResponse;
import com.asf.appcoins.sdk.ads.poa.PoAServiceConnector;
import com.asf.appcoins.sdk.ads.poa.PoAServiceConnectorImpl;
import com.asf.appcoins.sdk.core.web3.AsfWeb3j;
import com.asf.appcoins.sdk.core.web3.AsfWeb3jImpl;
import io.reactivex.schedulers.Schedulers;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

import static com.asf.appcoins.sdk.ads.AppCoinsAds.NETWORK_MAIN;
import static com.asf.appcoins.sdk.ads.AppCoinsAds.NETWORK_ROPSTEN;

/**
 * Created by Joao Raimundo on 01-03-2018.
 */
public final class AppCoinsAdsBuilder {

  private static final String TAG = AppCoinsAdsBuilder.class.getSimpleName();

  private PoAServiceConnector poaConnector;
  private String country;
  private boolean debug;
  private AsfWeb3j asfWeb3j;

  public AppCoinsAdsBuilder withDebug(boolean debug) {
    this.debug = debug;
    return this;
  }

  public AppCoinsAds createAdvertisementSdk(Context context) {
    if (this.poaConnector == null) {
      this.poaConnector = new PoAServiceConnectorImpl(null);
    }

    if (country == null) {
      country = context.getResources()
          .getConfiguration().locale.getDisplayCountry();
    }

    int networkId;
    Address contractAddress;
    Web3j web3;
    if (debug) {
      networkId = NETWORK_ROPSTEN;
      web3 = Web3jFactory.build(new HttpService("https://ropsten.infura.io/1YsvKO0VH5aBopMYJzcy"));
      contractAddress = new Address("0xd95c64c6eee9164539d679354f349779a04f57cb");
    } else {
      networkId = NETWORK_MAIN;
      web3 = Web3jFactory.build(new HttpService("https://mainnet.infura.io/1YsvKO0VH5aBopMYJzcy"));
      contractAddress = new Address("0xab949343e6c369c6b17c7ae302c1debd4b7b61c3");
    }

    if (this.asfWeb3j == null) {
      this.asfWeb3j = new AsfWeb3jImpl(web3);
    }

    String countryId = IpApi.create()
        .myIp()
        .map(IpResponse::getCountryCode)
        .subscribeOn(Schedulers.io())
        .doOnError(throwable -> Log.w(TAG, "createAdvertisementSdk: Failed to get country code!",
            throwable))
        .onErrorReturn(throwable -> "")
        .blockingFirst();

    return new AppCoinsAdsImpl(poaConnector, networkId,
        new CampaignManager(asfWeb3j, contractAddress, countryId));
  }
}