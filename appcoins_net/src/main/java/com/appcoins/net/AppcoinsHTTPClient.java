package com.appcoins.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppcoinsHTTPClient implements Runnable, AppcoinsConnectionQuerys {

  private final String serviceUrl;
  private final String params;
  private final Interceptor interceptor;
  private GetResponseHandler getResponseHandler;
  private URL urlConnection;
  private String concat;

  public AppcoinsHTTPClient(String serviceUrl, Interceptor interceptor, String params,
      GetResponseHandler getResponseHandler) {
    this.serviceUrl = serviceUrl;
    this.params = params;
    this.getResponseHandler = getResponseHandler;
    this.interceptor = interceptor;
  }

  @Override public void run() {
    try {
      String response = Get();
      getResponseHandler.getResponseHandler(response);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override public String Get() throws IOException {
    try {
      concat = serviceUrl + params;

      urlConnection = new URL(concat);
      HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
      int code = connection.getResponseCode();

      String path = null;

      if (!(code >= 200 && code < 400)) {
        connection.getErrorStream();
      } else {

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        String response = "";
        while ((inputLine = in.readLine()) != null) {
          response += inputLine;
        }

        String log = LogCreator.Intercept(connection, response);
        interceptor.OnInterceptPublish(log);
        in.close();
        connection.disconnect();
        return response;
      }
      return params;
    } catch (IOException e) {
      throw new IOException("Invalid Url");
    }
  }
}