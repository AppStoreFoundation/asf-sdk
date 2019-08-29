package com.appcoins.sdk.billing.helpers;

import com.appcoins.billing.sdk.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class EventLogger {

    public static String ENTITY_NAME = "SDK";

    private final String BASE_URL = "https://ws75.aptoide.com/api/7/";
    private final String SERVICE_PATH = "user/addEvent/action=CLICK/context=BILLING_SDK/name=";
    private final String purchaseEventName = "PURCHASE_INTENT";

    private void LogEvent(String eventName, JSONObject jsonObj) {
        String finalURL = BASE_URL + SERVICE_PATH + eventName;

        PostDataToURL(finalURL, jsonObj);
    }

    public void LogPurchaseEvent(String sku, String value, String appPackage) throws JSONException {
        String eventName = purchaseEventName;

        int sdkVersionCode = BuildConfig.VERSION_CODE;
        String sdkPackageName = BuildConfig.APPLICATION_ID; //"com.appcoins.billing.sdk";

        Boolean hasWallet = WalletUtils.hasWalletInstalled();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("aptoide_vercode",Integer.toString(sdkVersionCode));
        jsonObj.put("aptoide_package",sdkPackageName);
        jsonObj.put("unity_version", "sdk");

        JSONObject purchaseObj = new JSONObject();
        purchaseObj.put("package_name", appPackage);
        purchaseObj.put("sku", sku);

        JSONObject dataObj = new JSONObject();
        dataObj.put("wallet_installed", hasWallet);
        dataObj.put("purchase", purchaseObj);

        jsonObj.put("data", dataObj);

        LogEvent(eventName, jsonObj);
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

            try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))){
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                if (connection != null) {
                    connection.disconnect();
                    System.out.println(response.toString());
                }
            }
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
}
