package com.bds.microraidenj.ws;

import com.asf.microraidenj.type.Address;
import com.asf.microraidenj.type.ByteArray;
import io.reactivex.Observable;
import java.io.IOException;
import java.math.BigInteger;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BDSMicroRaidenApi {

  String ENDPOINT = "http://34.240.68.255/appc/";

  static BDSMicroRaidenApi create(boolean debug) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();

    if (debug) {
      builder.addInterceptor(BDSMicroRaidenApi::debugCall);
    }

    OkHttpClient client = builder.build();

    Retrofit retrofit = new Retrofit.Builder().addConverterFactory(JacksonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client)
        .baseUrl(ENDPOINT)
        .build();

    return retrofit.create(BDSMicroRaidenApi.class);
  }

  static Response debugCall(Interceptor.Chain chain) throws IOException {
    System.out.println("Request: " + chain.request());
    Response response = chain.proceed(chain.request());
    System.out.println("Response: " + response);
    return response;
  }

  @GET("close_sig") Observable<CloseChannelResponse> closeChannel(
      @Query("sender") Address senderAddress, @Query("openBlock") BigInteger openBlock,
      @Query("balance") BigInteger owedBalance, @Query("balance_proof") ByteArray balanceProof);

  @GET("channelpayment") Observable<MakePaymentResponse> makePayment(
      @Query("balance_proof") ByteArray balanceProof, @Query("sender") Address senderAddress,
      @Query("openBlock") BigInteger openBlock, @Query("item_cost") BigInteger itemCost,
      @Query("balance") BigInteger balance, @Query("dev") Address devAddress,
      @Query("store") Address storeAddress, @Query("oem") Address oemAddress);

  @GET("channelhistory") Observable<ChannelHistoryResponse> channelHistory(
      @Query("sender") Address senderAddress, @Query("receiver") Address receiverAddress,
      @Query("type") Type type);
}
