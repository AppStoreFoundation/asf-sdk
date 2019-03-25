package com.asf.appcoins.sdk.ads.network.clients;

import android.net.Uri;
import com.asf.appcoins.sdk.ads.network.QueryParams;
import com.asf.appcoins.sdk.ads.network.Interceptor;
import com.asf.appcoins.sdk.ads.network.LogCreator;
import com.asf.appcoins.sdk.ads.network.responses.GetResponseHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CampaignService implements Runnable {

  public static final String PACKAGE_NAME = "packageName";
  public static final String VER_CODE = "vercode";
  public static final String COUNTRY_CODE = "countryCode";
  public static final String SORT = "sort";
  public static final String BY = "by";
  public static final String VALID = "valid";
  public static final String TYPE = "type";

  private String packageName;
  private int versionCode;
  protected final String serviceUrl;
  protected final QueryParams params;
  protected final Interceptor interceptor;
  protected GetResponseHandler getResponseHandler;
  protected URL urlConnection;
  protected String concat;

  public CampaignService(String packageName, int versionCode, String serviceUrl, Interceptor interceptor,
      QueryParams params, GetResponseHandler getResponseHandler) {
    this.packageName = packageName;
    this.versionCode = versionCode;
    this.serviceUrl = serviceUrl;
    this.params = params;
    this.getResponseHandler = getResponseHandler;
    this.interceptor = interceptor;
  }

  @Override public void run() {
    String response = null;
    try {
      response = getCampaign();
    } catch (IOException e) {
      e.printStackTrace();
      response = "";
    }
    getResponseHandler.getResponseHandler(response);
  }

  public String getCampaign() throws IOException {
    String response = "";

    long time = System.nanoTime();

    urlConnection = new URL(buildURL());

    HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
    connection.setRequestMethod("GET");
    Map<String, List<String>> requestProperties = connection.getRequestProperties();

    connection.connect();

    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String inputLine;

    long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - time);

    while ((inputLine = in.readLine()) != null) {
      response += inputLine;
    }

    String log = LogCreator.Intercept(requestProperties, connection, response, tookMs);
    interceptor.OnInterceptPublish(log);

    if (in != null) {
      in.close();
    }

    if (connection != null) {
      connection.disconnect();
    }

    return response;
  }

  private String buildURL(){
    Uri campaignUri = Uri.parse(serviceUrl+"/campaign/listall?")
        .buildUpon()
        .appendQueryParameter(PACKAGE_NAME, packageName)
        .appendQueryParameter(VER_CODE, Integer.toString(versionCode))
        .appendQueryParameter(SORT, params.getSort())
        .appendQueryParameter(BY, params.getBy())
        .appendQueryParameter(VALID, params.getValid())
        .appendQueryParameter(TYPE, params.getType())
        .build();
    return campaignUri.toString();
  }
}

