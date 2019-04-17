package com.appcoins.sdk.billing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GetSkuDetailsService {

  private final String serviceUrl;

  public GetSkuDetailsService(final String serviceUrl) {
    this.serviceUrl = serviceUrl;
  }

  public String getSkuDetailsForPackageName(String packageName) {
    String response = "";
    URL url = null;
    try {
      url = new URL(buildURL(packageName));

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

  private String buildURL(String packageName) {
    return serviceUrl + "inapp/8.20180518/packages/{packageName}/products".replaceFirst(
        "{packageName}", packageName);
  }
}
