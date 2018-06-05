package com.bds.microraidenj.ws;

import com.asf.microraidenj.type.Address;
import com.asf.microraidenj.type.ByteArray;
import io.reactivex.Observable;
import java.math.BigInteger;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BDSMicroRaidenApi {

  String ENDPOINT = "http://34.240.68.255/appc/";

  static BDSMicroRaidenApi create() {
    Retrofit retrofit = new Retrofit.Builder().addConverterFactory(JacksonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(ENDPOINT)
        .build();

    return retrofit.create(BDSMicroRaidenApi.class);
  }

  @GET("close_sig") Observable<CloseChannelResponse> closeChannel(
      @Query("sender") Address senderAddress, @Query("openBlock") BigInteger openBlock,
      @Query("balance") BigInteger owedBalance, @Query("balance_proof") ByteArray balanceProof);

  @GET("channelpayment") Observable<MakePaymentResponse> makePayment(
      @Query("balance_proof") ByteArray balanceProof, @Query("sender") Address senderAddress,
      @Query("openBlock") BigInteger openBlock, @Query("item_cost") BigInteger itemCost,
      @Query("balance") BigInteger balance, @Query("dev") Address devAddress,
      @Query("store") Address storeAddress, @Query("oem") Address oemAddress);
}
