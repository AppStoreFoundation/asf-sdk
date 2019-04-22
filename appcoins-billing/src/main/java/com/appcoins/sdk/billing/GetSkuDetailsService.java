package com.appcoins.sdk.billing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class GetSkuDetailsService {

  private final static String URL_PATH = "/inapp/8.20180518/packages/packageName/products?names=";

  private final String serviceUrl;

  public GetSkuDetailsService(final String serviceUrl) {
    this.serviceUrl = serviceUrl;
  }

  public String getSkuDetailsForPackageName(final String packageName, final List <String> sku) {
    String response = "";
    URL url = null;
    try {
      url = new URL(buildURL(packageName,sku));

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

  private String buildURL(String packageName, List<String> sku) {
    String url = serviceUrl + URL_PATH.replaceFirst(
        "packageName", packageName);
    for (String skuName : sku) {
      url += skuName + ",";
    }
    return url.substring(0, url.length() - 1);
  }
}
