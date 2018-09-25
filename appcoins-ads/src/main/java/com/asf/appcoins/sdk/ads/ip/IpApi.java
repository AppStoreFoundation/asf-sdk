package com.asf.appcoins.sdk.ads.ip;

import com.asf.appcoins.sdk.ads.BuildConfig;
import com.asf.appcoins.sdk.core.util.LogInterceptor;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;

public interface IpApi {

  static IpApi create(boolean isDebug) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new LogInterceptor());
    String url;
    if (isDebug) {
      url = BuildConfig.DEV_BACKEND_BASE_HOST;
    } else {
      url = BuildConfig.PROD_BACKEND_BASE_HOST;
    }
    Retrofit retrofit = new Retrofit.Builder().addConverterFactory(JacksonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(builder.build())
        .baseUrl(url)
        .build();

    return retrofit.create(IpApi.class);
  }

  @GET("exchange/countrycode") Single<IpResponse> getCountry();
}
