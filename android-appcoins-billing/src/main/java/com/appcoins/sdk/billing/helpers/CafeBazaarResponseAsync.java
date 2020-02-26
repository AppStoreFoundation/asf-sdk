package com.appcoins.sdk.billing.helpers;

import android.os.AsyncTask;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class CafeBazaarResponseAsync extends AsyncTask<Object, Object, Integer> {

  private static final int UNKNOWN_ERROR_CODE = 600;
  private ResponseListener responseListener;

  CafeBazaarResponseAsync(ResponseListener responseListener) {
    this.responseListener = responseListener;
  }

  private int getResponseCode() throws IOException {
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
    OutputStream os = huc.getOutputStream();
    byte[] input = jsonInputString.getBytes(Charset.forName("UTF-8"));
    os.write(input, 0, input.length);
    huc.connect();
    int responseCode = huc.getResponseCode();
    huc.disconnect();
    return responseCode;
  }

  @Override protected Integer doInBackground(Object[] objects) {
    int responseCode = UNKNOWN_ERROR_CODE;
    try {
      responseCode = getResponseCode();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return responseCode;
  }

  @Override protected void onPostExecute(Integer responseCode) {
    super.onPostExecute(responseCode);
    responseListener.onResponseCode(responseCode);
  }
}
