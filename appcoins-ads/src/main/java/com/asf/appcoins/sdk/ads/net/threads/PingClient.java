package com.asf.appcoins.sdk.ads.net.threads;

import com.asf.appcoins.sdk.ads.net.GetResponseHandler;
import com.asf.appcoins.sdk.ads.net.Interceptor;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PingClient extends HTTPClient implements Runnable {

  public PingClient(String serviceUrl, Interceptor interceptor,
      GetResponseHandler getResponseHandler) {
    super(serviceUrl, interceptor, null, getResponseHandler);
  }

  @Override public void run() {
    boolean result = pingServers();
    getResponseHandler.getResponseHandler(result);
  }

  private boolean pingServers() {

    boolean result = false;
    URL url = null;
    HttpURLConnection urlConnection = null;
    try {
      url = new URL(serviceUrl);
      urlConnection = (HttpURLConnection) url.openConnection();

      int responseCode = urlConnection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        result = true;
      } else {
        result = false;
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
      result = false;
    } catch (IOException e) {
      e.printStackTrace();
      result = false;
    }

    if (urlConnection != null) {
      urlConnection.disconnect();
    }

    return result;
  }
}

