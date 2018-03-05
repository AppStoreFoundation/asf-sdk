package com.asf.appcoins.sdk;

import com.asf.appcoins.sdk.entity.SKU;
import java.util.List;
import okhttp3.OkHttpClient;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

/**
 * Created by neuro on 01-03-2018.
 */
public final class AppCoinsSdkFactory {

  private final OkHttpClient httpClient;

  public AppCoinsSdkFactory(OkHttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public AppCoinsSdk create(String rpcServerUrl, String developerAddress, String oemAddress,
      String storeAddress, List<SKU> skus) {
    Web3j web3j = Web3jFactory.build(new HttpService(rpcServerUrl, httpClient, false));

    return new AppCoinsSdkBuilder(developerAddress, oemAddress, storeAddress).setAsfWeb3j(
        new AsfWeb3jImpl(web3j))
        .setDeveloperAddress(developerAddress)
        .setOemAddress(oemAddress)
        .setStoreAddress(storeAddress)
        .setSkus(skus)
        .createAppCoinsSdk();
  }
}
