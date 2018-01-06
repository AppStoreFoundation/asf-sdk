package cm.aptoide.pt.ethereum.ws;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter.Factory;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;

public class WebServiceFactory {

  private final OkHttpClient httpClient;
  private final Converter.Factory converterFactory;
  private final Factory callAdapterFactory;

  public WebServiceFactory(OkHttpClient httpClient, Converter.Factory converterFactory,
      Factory callAdapterFactory) {
    this.httpClient = httpClient;
    this.converterFactory = converterFactory;
    this.callAdapterFactory = callAdapterFactory;
  }

  public <T> T createWebService(String baseHost, Class<T> clazz) {
    return createRetrofit(baseHost).create(clazz);
  }

  private Retrofit createRetrofit(String baseHost) {
    return new Builder().baseUrl(baseHost)
        .client(httpClient)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .build();
  }
}
