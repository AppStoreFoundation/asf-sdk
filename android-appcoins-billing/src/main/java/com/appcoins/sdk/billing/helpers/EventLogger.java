package com.appcoins.sdk.billing.helpers;

import com.appcoins.billing.sdk.BuildConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

public class EventLogger implements Runnable {

  public static String ENTITY_NAME = "SDK";

  private final String BASE_URL = "https://ws75.aptoide.com/api/7/";
  private final String SERVICE_PATH = "user/addEvent/action=CLICK/context=BILLING_SDK/name=";
  private final String purchaseEventName = "PURCHASE_INTENT";
  private String sku;
  private String appPackage;

  public EventLogger(String sku, String appPackage) {
    this.sku = sku;
    this.appPackage = appPackage;
  }

  public void LogPurchaseEvent() throws JSONException {
    String eventName = purchaseEventName;

    int sdkVersionCode = BuildConfig.VERSION_CODE;
    String sdkPackageName = BuildConfig.APPLICATION_ID;

    Boolean hasWallet = WalletUtils.hasWalletInstalled();

    JSONObject jsonObj = new JSONObject();
    jsonObj.put("aptoide_vercode", Integer.toString(sdkVersionCode));
    jsonObj.put("aptoide_package", sdkPackageName);
    jsonObj.put("unity_version", "sdk");

    JSONObject purchaseObj = new JSONObject();
    purchaseObj.put("package_name", appPackage);
    purchaseObj.put("sku", sku);

    JSONObject dataObj = new JSONObject();
    dataObj.put("wallet_installed", hasWallet);
    dataObj.put("purchase", purchaseObj);

    jsonObj.put("data", dataObj);

    String finalURL = BASE_URL + SERVICE_PATH + eventName;

    PostDataToURL(finalURL, jsonObj);
  }

  private void PostDataToURL(String urlStr, JSONObject jsonObj) {
    URL url = null;
    String responseStr = "";

    try {
      url = new URL(urlStr);

      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      connection.setRequestProperty("Content-Type", "application/json");

      OutputStream os = connection.getOutputStream();
      String jsonString = jsonObj.toString();
      byte[] postData = jsonString.getBytes();
      os.write(postData);
      os.close();

      connection.connect();

      int code = connection.getResponseCode();
      System.out.println(code);

      BufferedReader br =
          new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
      StringBuilder response = new StringBuilder();
      String responseLine = null;
      while ((responseLine = br.readLine()) != null) {
        response.append(responseLine.trim());
      }
      if (connection != null) {
        connection.disconnect();
        System.out.println(response.toString());
      }
      br.close();
    } catch (MalformedURLException e) {
      responseStr = "";
      e.printStackTrace();
    } catch (ProtocolException e) {
      responseStr = "";
      e.printStackTrace();
    } catch (IOException e) {
      responseStr = "";
      e.printStackTrace();
    }
  }

  @Override public void run() {
    try {
      LogPurchaseEvent();
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
