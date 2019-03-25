package com.asf.appcoins.sdk.contractproxy;

import com.asf.appcoins.sdk.contractproxy.repository.RemoteRepository;
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
        baseHost = BuildConfig.ROPSTEN_NETWORK_BACKEND_BASE_HOST;
        break;
      default:
        baseHost = BuildConfig.MAIN_NETWORK_BACKEND_BASE_HOST;
        break;
    }

    return new Retrofit.Builder().baseUrl(baseHost)
        .client(new OkHttpClient.Builder().build())
        .addConverterFactory(JacksonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(RemoteRepository.Api.class);

  }
}