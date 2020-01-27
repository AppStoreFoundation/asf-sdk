package com.appcoins.sdk.billing.service;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class BdsService implements Service {

  private String baseUrl;

  public BdsService(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  private static RequestResponse createRequest(String baseUrl, String endPoint, String httpMethod,
      List<String> paths, Map<String, String> queries, String body) {
    HttpURLConnection urlConnection = null;
    try {
      StringBuilder urlBuilder = buildUrl(baseUrl, endPoint, paths, queries);
      URL url = new URL(urlBuilder.toString());
      urlConnection = openUrlConnection(url, httpMethod);

      if (httpMethod.equals("POST") && body != null) {
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

  private static StringBuilder buildUrl(String baseUrl, String endPoint, List<String> paths,
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
      urlBuilder.append(entry.getKey())
          .append("=")
          .append(entry.getValue())
          .append("&");
    }
    if (!queries.isEmpty()) {
      urlBuilder.setLength(urlBuilder.length() - 1);
    }
    return urlBuilder;
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

  private static void setPostOutput(HttpURLConnection urlConnection, String body)
      throws IOException {
    urlConnection.setRequestProperty("Content-Type", "application/json");
    urlConnection.setRequestProperty("Accept", "application/json");
    urlConnection.setDoOutput(true);
    OutputStream os = urlConnection.getOutputStream();
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
      Map<String, String> queries, String body, ServiceResponseListener serviceResponseListener) {
    AsyncTask asyncTask = new ServiceAsyncTask(baseUrl, endPoint, httpMethod, paths, queries, body,
        serviceResponseListener);
    asyncTask.execute();
  }

  protected static class ServiceAsyncTask extends AsyncTask {

    private final String httpMethod;
    private final List<String> paths;
    private final Map<String, String> queries;
    private final String body;
    private String baseUrl;
    private String endPoint;
    private ServiceResponseListener serviceResponseListener;

    ServiceAsyncTask(String baseUrl, String endPoint, String httpMethod, List<String> paths,
        Map<String, String> queries, String body, ServiceResponseListener serviceResponseListener) {
      this.baseUrl = baseUrl;
      this.endPoint = endPoint;
      this.httpMethod = httpMethod;
      this.paths = paths;
      this.queries = queries;
      this.body = body;
      this.serviceResponseListener = serviceResponseListener;
    }

    @Override protected Object doInBackground(Object[] objects) {
      RequestResponse requestResponse =
          createRequest(baseUrl, endPoint, httpMethod, paths, queries, body);
      serviceResponseListener.onResponseReceived(requestResponse.getResponseCode(),
          requestResponse.getResponse(), requestResponse.getException());
      return null;
    }
  }
}
