package com.asf.appcoins.sdk.contractproxy;

import com.asf.appcoins.sdk.contractproxy.repository.RemoteRepository;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Joao Raimundo on 01-03-2018.
 */
public final class AppCoinsAddressProxyBuilder {

  public AppCoinsAddressProxySdk createAddressProxySdk() {
    return new BdsAppCoinsAddressProxySdk(new RemoteRepository(this::provideApi));
  }

  private RemoteRepository.Api provideApi(int chainId) {
    String baseHost;
    switch (chainId) {
      case 3:
        baseHost = "http://52.209.250.255";
        break;
      default:
        baseHost = "http://34.254.1.70";
        break;
    }
    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.MINUTES)
        .writeTimeout(30, TimeUnit.MINUTES)
        .build();
    return new Retrofit.Builder().baseUrl(baseHost)
        .client(client)
        .addConverterFactory(JacksonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(RemoteRepository.Api.class);
  }
}