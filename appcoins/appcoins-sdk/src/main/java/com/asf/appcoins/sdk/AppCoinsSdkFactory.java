package com.asf.appcoins.sdk;

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

  public final AppCoinsSdk create(String rpcServerUrl) {
    Web3j web3j = Web3jFactory.build(new HttpService(rpcServerUrl, httpClient, false));

    return new AppCoinsSdkImpl(new AsfWeb3jImpl(web3j));
  }
}
