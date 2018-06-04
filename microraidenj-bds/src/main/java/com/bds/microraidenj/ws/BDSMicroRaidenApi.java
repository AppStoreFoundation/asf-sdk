package com.bds.microraidenj.ws;

import com.asf.microraidenj.type.Address;
import io.reactivex.Observable;
import java.math.BigInteger;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BDSMicroRaidenApi {

  String ENDPOINT = "http:///";

  static BDSMicroRaidenApi create() {
    Retrofit retrofit = new Retrofit.Builder().addConverterFactory(JacksonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(ENDPOINT)
        .build();

    return retrofit.create(BDSMicroRaidenApi.class);
  }

  @GET("") Observable<CloseChannelResponse> closeChannel(
      @Query("senderAddress") Address senderAddress, @Query("openBlockNum") BigInteger openBlockNum,
      @Query("owedBalance") BigInteger owedBalance, @Query("balanceProof") String balanceProof);
}
