package com.appcoins.sdk.billing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class IsBillingSupportedService implements Runnable {

  private final static String URL_PATH = "/inapp/8.20180518/packages/packageName?type=param";

  private final String serviceUrl;

  private String packageName;
  private String type;
  private final IsBillingSupportedServiceListenner isBillingSupportedServiceListenner;

  public IsBillingSupportedService(final String serviceUrl, final String packageName,
      final String type,
      final IsBillingSupportedServiceListenner isBillingSupportedServiceListenner) {
    this.serviceUrl = serviceUrl;
    this.packageName = packageName;
    this.type = type;
    this.isBillingSupportedServiceListenner = isBillingSupportedServiceListenner;
  }

  private String getIsBillingSupportedForPackageName() {
    String response = "";
    URL url = null;
    try {
      url = new URL(buildURL(packageName, type));

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String inputLine;

      while ((inputLine = in.readLine()) != null) {
        response += inputLine;
      }

      if (in != null) {
        in.close();
      }

      if (connection != null) {
        connection.disconnect();
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
      response = "";
    } catch (ProtocolException e) {
      e.printStackTrace();
      response = "";
    } catch (IOException e) {
      e.printStackTrace();
      response = "";
    }

    return response;
  }

  private String buildURL(String packageName, String type) {
    String url = serviceUrl + URL_PATH.replaceFirst("packageName", packageName);
    return url.replaceFirst("packageName", packageName)
        .replaceFirst("param", type);
  }

  @Override public void run() {
    isBillingSupportedServiceListenner.onIsBillingSupportedServiceListenner(
        getIsBillingSupportedForPackageName());
  }
}
