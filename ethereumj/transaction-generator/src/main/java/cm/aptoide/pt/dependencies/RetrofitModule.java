package cm.aptoide.pt.dependencies;

import cm.aptoide.pt.ws.WebServiceFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.text.SimpleDateFormat;
import java.util.Locale;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitModule {

  public WebServiceFactory provideRetrofitFactory() {
    return new WebServiceFactory(provideOkHttpClient(), provideConverterFactory(),
        provideRxJavaCallAdapterFactory());
  }

  public OkHttpClient provideOkHttpClient() {
    return new OkHttpClient();
  }

  public Converter.Factory provideConverterFactory() {
    return JacksonConverterFactory.create(provideObjectMapper());
  }

  public CallAdapter.Factory provideRxJavaCallAdapterFactory() {
    return RxJavaCallAdapterFactory.create();
  }

  public SimpleDateFormat provideDateFormat() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
  }

  public ObjectMapper provideObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    objectMapper.setDateFormat(provideDateFormat());

    return objectMapper;
  }
}
