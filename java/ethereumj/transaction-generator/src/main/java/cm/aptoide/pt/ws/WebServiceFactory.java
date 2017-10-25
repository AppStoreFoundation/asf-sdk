package cm.aptoide.pt.ws;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class WebServiceFactory {

	private final OkHttpClient httpClient;
	private final Converter.Factory converterFactory;
	private final CallAdapter.Factory callAdapterFactory;

	public WebServiceFactory(OkHttpClient httpClient, Converter.Factory converterFactory,
													 CallAdapter.Factory callAdapterFactory) {
		this.httpClient = httpClient;
		this.converterFactory = converterFactory;
		this.callAdapterFactory = callAdapterFactory;
	}

	public <T> T createWebService(String baseHost, Class<T> clazz) {
		return createRetrofit(baseHost).create(clazz);
	}

	private Retrofit createRetrofit(String baseHost) {
		return new Retrofit.Builder().baseUrl(baseHost)
						.client(httpClient)
						.addCallAdapterFactory(callAdapterFactory)
						.addConverterFactory(converterFactory)
						.build();
	}
}
