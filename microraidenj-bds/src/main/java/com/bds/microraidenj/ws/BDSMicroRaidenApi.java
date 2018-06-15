package com.bds.microraidenj.ws;

import com.asf.microraidenj.type.Address;
import com.asf.microraidenj.type.ByteArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Observable;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BDSMicroRaidenApi {

  String ENDPOINT = "http://34.240.68.255/microraiden/";

  static BDSMicroRaidenApi create(boolean debug) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();

    if (debug) {
      builder.addInterceptor(BDSMicroRaidenApi::debugCall);
    }

    OkHttpClient client = builder.build();

    Retrofit retrofit =
        new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(createDefaultConverter())
            .client(client)
            .baseUrl(ENDPOINT)
            .build();

    return retrofit.create(BDSMicroRaidenApi.class);
  }

  static Converter.Factory createDefaultConverter() {
    ObjectMapper objectMapper = new ObjectMapper();

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    objectMapper.setDateFormat(df);

    return JacksonConverterFactory.create(objectMapper);
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
      @Query("block") BigInteger block, @Query("type") Type type);

  @GET("listallchannels") Observable<ListAllChannelsResponse> listAllChannels(
      @Query("sender") Address senderAddress, @Query("closed") boolean closed);
}
