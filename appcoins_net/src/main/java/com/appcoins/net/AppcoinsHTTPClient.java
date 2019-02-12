package com.appcoins.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AppcoinsHTTPClient implements Runnable, AppcoinsConnectionQuerys {

  public final static int GET_CONNECTION = 0;

  public final static int PING_CONNECTION = 1;

  private final String serviceUrl;
  private final String params;
  private final Interceptor interceptor;
  private GetResponseHandler getResponseHandler;
  private URL urlConnection;
  private String concat;
  private int operation;

  public AppcoinsHTTPClient(String serviceUrl, Interceptor interceptor, String params,
      int operation, GetResponseHandler getResponseHandler) {
    this.serviceUrl = serviceUrl;
    this.params = params;
    this.getResponseHandler = getResponseHandler;
    this.interceptor = interceptor;
    this.operation = operation;
  }

  @Override public void run() {
    switch (operation) {
      case GET_CONNECTION:
        String response = Get();
        getResponseHandler.getResponseHandler(response);
        break;
      case PING_CONNECTION:
        boolean result = Ping();
        getResponseHandler.getResponseHandler(result);
        break;
    }
  }

  @Override public String Get() {
    String response = "";

    try {

      concat = serviceUrl + params;

      urlConnection = new URL(concat);
      long time = System.nanoTime();

      HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
      connection.setRequestMethod("GET");
      Map<String, List<String>> requestProperties = connection.getRequestProperties();

      connection.connect();
      int code = connection.getResponseCode();

      String path = null;

      if (!(code >= 200 && code < 400)) {
        connection.getErrorStream();
      } else {

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;

        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - time);

        while ((inputLine = in.readLine()) != null) {
          response += inputLine;
        }

        String log = LogCreator.Intercept(requestProperties, connection, response,tookMs);
        interceptor.OnInterceptPublish(log);
        in.close();
        connection.disconnect();
        return response;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return response;
  }

  @Override public boolean Ping() {
    return pingGoogle();
  }

  private boolean pingGoogle() {

    boolean result = false;
    URL url = null;
    HttpURLConnection urlConnection = null;
    try {
      url = new URL("http://www.google.com");
      urlConnection = (HttpURLConnection) url.openConnection();
      int responseCode = urlConnection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        result = true;
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    urlConnection.disconnect();
    return result;
  }
}