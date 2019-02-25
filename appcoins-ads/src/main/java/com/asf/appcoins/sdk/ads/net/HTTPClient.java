package com.asf.appcoins.sdk.ads.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HTTPClient implements Runnable {

  protected final String serviceUrl;
  protected final String params;
  protected final Interceptor interceptor;
  protected GetResponseHandler getResponseHandler;
  protected URL urlConnection;
  protected String concat;

  public HTTPClient(String serviceUrl, Interceptor interceptor, String params,
      GetResponseHandler getResponseHandler) {
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

    concat = serviceUrl + params;

    urlConnection = new URL(concat);

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

    if(in != null){
      in.close();
    }

    if(connection != null){
      connection.disconnect();
    }

    return response;
  }
}

