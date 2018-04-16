package com.asf.appcoins.sdk.ads.ip;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;

public interface IpApi {

  String ENDPOINT = "http://ip-api.com/";

  static IpApi create() {
    Retrofit retrofit = new Retrofit.Builder().addConverterFactory(JacksonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(ENDPOINT)
        .build();

    return retrofit.create(IpApi.class);
  }

  @GET("json") Observable<IpResponse> myIp();
}
