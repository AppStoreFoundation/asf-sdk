package com.appcoins.sdk.billing.service;

import android.net.Uri;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BdsService implements Service {

  private String baseUrl;

  public BdsService(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  static RequestResponse createRequest(String baseUrl, String endPoint, String httpMethod,
      List<String> paths, Map<String, String> queries, Map<String, String> body) {
    HttpURLConnection urlConnection = null;
    try {
      String urlBuilder = buildUrl(baseUrl, endPoint, paths, queries);
      URL url = new URL(urlBuilder);
      urlConnection = openUrlConnection(url, httpMethod);

      if ((httpMethod.equals("POST") || httpMethod.equals("PATCH")) && body != null) {
        if (httpMethod.equals("PATCH")) {
          urlConnection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
        }
        setPostOutput(urlConnection, body);
      }

      int responseCode = urlConnection.getResponseCode();
      InputStream inputStream;
      if (responseCode >= 400) {
        inputStream = urlConnection.getErrorStream();
      } else {
        inputStream = urlConnection.getInputStream();
      }
      return readResponse(inputStream, responseCode);
    } catch (Exception firstException) {
      return handleException(urlConnection, firstException);
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
    }
  }

  private static String buildUrl(String baseUrl, String endPoint, List<String> paths,
      Map<String, String> queries) {
    StringBuilder urlBuilder = new StringBuilder(baseUrl + endPoint);
    for (String path : paths) {
      urlBuilder.append("/")
          .append(path);
    }
    if (!queries.isEmpty()) {
      urlBuilder.append("?");
    }
    for (Map.Entry<String, String> entry : queries.entrySet()) {
      urlBuilder = new StringBuilder(Uri.parse(urlBuilder.toString())
          .buildUpon()
          .appendQueryParameter(entry.getKey(), entry.getValue())
          .build()
          .toString());
    }
    return urlBuilder.toString();
  }

  private static String buildBody(Map<String, String> bodyKeys) {
    StringBuilder builder = new StringBuilder("{");
    if (bodyKeys != null) {
      for (Map.Entry<String, String> entry : bodyKeys.entrySet()) {
        if (entry.getValue() != null) {
          builder.append("\"" + entry.getKey() + "\"" + ":" + entry.getValue())
              .append(",");
        }
      }
      if (!bodyKeys.isEmpty()) {
        builder.deleteCharAt(builder.length() - 1);
      }
      builder.append("}");
    }
    return builder.toString();
  }

  private static HttpURLConnection openUrlConnection(URL url, String httpMethod)
      throws IOException {
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    urlConnection.setRequestMethod(httpMethod);
    return urlConnection;
  }

  private static RequestResponse readResponse(InputStream inputStream, int responseCode)
      throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
    String inputLine;
    StringBuilder response = new StringBuilder();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
    return new RequestResponse(responseCode, response.toString(), null);
  }

  private static void setPostOutput(HttpURLConnection urlConnection, Map<String, String> bodyKeys)
      throws IOException {
    urlConnection.setRequestProperty("Content-Type", "application/json");
    urlConnection.setRequestProperty("Accept", "application/json");
    urlConnection.setDoOutput(true);
    OutputStream os = urlConnection.getOutputStream();
    String body = buildBody(bodyKeys);
    byte[] input = body.getBytes(StandardCharsets.UTF_8);
    os.write(input, 0, input.length);
  }

  private static RequestResponse handleException(HttpURLConnection urlConnection,
      Exception firstException) {
    firstException.printStackTrace();
    int responseCode = 500;
    if (urlConnection != null) {
      try {
        responseCode = urlConnection.getResponseCode();
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
    }
    return new RequestResponse(responseCode, null, firstException);
  }

  public void makeRequest(String endPoint, String httpMethod, List<String> paths,
      Map<String, String> queries, Map<String, String> body,
      ServiceResponseListener serviceResponseListener) {
    if (paths == null) {
      paths = new ArrayList<>();
    }
    if (queries == null) {
      queries = new HashMap<>();
    }
    AsyncTask asyncTask = new ServiceAsyncTask(baseUrl, endPoint, httpMethod, paths, queries, body,
        serviceResponseListener);
    asyncTask.execute();
  }
}
