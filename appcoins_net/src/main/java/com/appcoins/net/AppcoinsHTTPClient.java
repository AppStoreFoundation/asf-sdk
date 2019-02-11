package com.appcoins.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class AppcoinsHTTPClient implements Runnable, AppcoinsConnectionQuerys {

  private final String serviceUrl;
  private final String params;
  private GetResponseHandler getResponseHandler;
  private URL urlConnection;
  private String concat;

  public AppcoinsHTTPClient(String serviceUrl , String params , GetResponseHandler getResponseHandler) {
    this.serviceUrl = serviceUrl;
    this.params = params;
    this.getResponseHandler = getResponseHandler;
  }

  @Override public String Get() throws IOException {
    concat = serviceUrl + params;
    urlConnection = new URL(concat);
    URLConnection yc = urlConnection.openConnection();
    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
    String inputLine;
    String response = "";
    while ((inputLine = in.readLine()) != null) {
      response += inputLine;
    }

    in.close();
    yc.getInputStream().close();

    return response;
  }

  @Override public void run() {
    try {
      String response = Get();
      getResponseHandler.getResponseHandler(response);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}