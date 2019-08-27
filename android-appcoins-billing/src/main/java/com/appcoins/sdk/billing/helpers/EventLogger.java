package com.appcoins.sdk.billing.helpers;

import com.appcoins.billing.sdk.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

    private void LogEvent(String eventName, String data) {
        String finalURL = BASE_URL + SERVICE_PATH + eventName;

        PostDataToURL(finalURL, data);
    }

    public void LogPurchaseEvent(String sku, String value, String appPackage) {
        String eventName = purchaseEventName;

        int sdkVersionCode = BuildConfig.VERSION_CODE;
        String sdkPackageName = BuildConfig.APPLICATION_ID; //"com.appcoins.billing.sdk";

        Boolean hasWallet = WalletUtils.hasWalletInstalled();

        String jsonData = "{ \"aptoide_vercode\": " +  sdkVersionCode + ", \"aptoide_package\": \"" + sdkPackageName + "\", \"unity_version\": \"" + ENTITY_NAME + "\", \"data\": { \"purchase\": { \"package_name\":\"" + appPackage + "\", \"sku\":\"" + sku + "\"";
            if (value != "ERROR" && hasWallet) {
                jsonData += ", \"value\": " + value;
            }
            jsonData += "}, \"wallet_installed\": " + (hasWallet ? "true" : "false") + " } }";
        LogEvent(eventName,jsonData);
    }

    private void PostDataToURL(String urlStr, String jsonData) {
        URL url = null;
        String responseStr = "";

        try {
            url = new URL(urlStr);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            //This try will close outputstream automatically once it's done with it
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonData.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            connection.connect();

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                responseStr = response.toString();
                System.out.println(responseStr);
            }

            if (connection != null) {
                connection.disconnect();
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
