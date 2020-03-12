package com.asf.appcoins.sdk.ads.network.clients;

import com.appcoins.sdk.billing.helpers.WalletUtils;
import com.asf.appcoins.sdk.ads.network.Interceptor;
import com.asf.appcoins.sdk.ads.network.responses.GetResponseHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CheckConnectionCampaignService extends CampaignService implements Runnable {

  private static final String SERVICE_URL_PATH = "/campaign/listall";

  public CheckConnectionCampaignService(String packageName, int versionCode, String serviceUrl,
      Interceptor interceptor, GetResponseHandler getResponseHandler) {
    super(packageName, versionCode, serviceUrl, interceptor, null, getResponseHandler);
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
      url = new URL(serviceUrl + SERVICE_URL_PATH);
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setRequestProperty("User-Agent", WalletUtils.getUserAgent());

      int responseCode = urlConnection.getResponseCode();
      result = responseCode == HttpURLConnection.HTTP_OK;
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

