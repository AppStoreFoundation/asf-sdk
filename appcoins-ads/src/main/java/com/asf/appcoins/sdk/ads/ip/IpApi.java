package com.asf.appcoins.sdk.ads.ip;

public interface IpApi {
/*
  static IpApi create(boolean isDebug) {
    //TODO interceptor novo
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    String url;
    if (isDebug) {
      url = BuildConfig.DEV_BACKEND_BASE_HOST;
    } else {
      url = BuildConfig.PROD_BACKEND_BASE_HOST;
    }
    //TODO REMOVER RETROFIT
    Retrofit retrofit = new Retrofit.Builder().addConverterFactory(JacksonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(builder.build())
        .baseUrl(url)
        .build();

    return retrofit.create(IpApi.class);
  }

  @GET("exchange/countrycode") Single<IpResponse> getCountry();
  */
}
