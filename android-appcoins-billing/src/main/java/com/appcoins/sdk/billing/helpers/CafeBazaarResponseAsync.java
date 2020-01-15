package com.appcoins.sdk.billing.helpers;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CafeBazaarResponseAsync extends AsyncTask {

  private ResponseListener responseListener;

  CafeBazaarResponseAsync(ResponseListener responseListener) {
    this.responseListener = responseListener;
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT) private int getResponseCode() throws IOException {
    URL url = new URL("https://cdn.api.cafebazaar.ir/rest-v1/process");
    HttpURLConnection huc = (HttpURLConnection) url.openConnection();
    huc.setRequestMethod("POST");
    huc.setRequestProperty("Content-Type", "application/json; utf-8");
    huc.setRequestProperty("Accept", "application/json");
    huc.setDoOutput(true);
    String jsonInputString = "{\n"
        + "\t\"properties\": {\n"
        + "\t\t\"language\": 2,\n"
        + "\t\t\"clientID\": \"1\",\n"
        + "\t\t\"deviceID\": \"1\",\n"
        + "\t\t\"clientVersion\": \"web\"\n"
        + "\t},\n"
        + "\t\"singleRequest\": {\n"
        + "\t\t\"appDetailsRequest\": {\n"
        + "\t\t\t\"language\": \"fa\",\n"
        + "\t\t\t\"packageName\": \"com.hezardastaan.wallet\"\n"
        + "\t\t}\n"
        + "\t}\n"
        + "}";
    try (OutputStream os = huc.getOutputStream()) {
      byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
      os.write(input, 0, input.length);
    }
    huc.connect();
    int responseCode = 404;
    huc.disconnect();
    return responseCode;
  }

  @Override protected Object doInBackground(Object[] objects) {
    int responseCode = 404;
    try {
      if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        responseCode = getResponseCode();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    responseListener.onResponseCode(responseCode);
    return null;
  }
}
