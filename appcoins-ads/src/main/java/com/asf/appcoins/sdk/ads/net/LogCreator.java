package com.asf.appcoins.sdk.ads.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class LogCreator {

  private static final Charset UTF8 = Charset.forName("UTF-8");

  public static String Intercept(Map<String, List<String>> requestProperties,
      HttpURLConnection connection, String response, long tookMs) throws IOException {

    //Url details
    StringBuilder logBuilder = new StringBuilder();
    logBuilder.append("<------------BEGIN REQUEST------------>");
    logBuilder.append("\n");
    logBuilder.append("Request encoded url: ")
        .append(" ")
        .append(connection.getURL());
    logBuilder.append("\n");

    //Header details
    logBuilder.append("\n=============== Headers ===============\n");

    for (Map.Entry<String, List<String>> header : requestProperties.entrySet()) {
      List<String> list = header.getValue();

      if (header.getKey() != null) {
        logBuilder.append(header.getKey())
            .append(" : ");
      }

      for (String values : list) {
        logBuilder.append(values);
      }

      logBuilder.append("\n");
    }

    logBuilder.append("\n=============== END Headers ===============\n");

    logBuilder.append("\n");
    logBuilder.append("Response timeout: ")
        .append(tookMs)
        .append("ms");
    logBuilder.append("\n");
    logBuilder.append("Response message: ")
        .append(connection.getResponseMessage());
    logBuilder.append("\n");
    logBuilder.append("Response code: ")
        .append(connection.getResponseCode());

    if (!response.isEmpty()) {
      logBuilder.append("\n");
      logBuilder.append("Response body: \n");
      logBuilder.append(response);
    }

    logBuilder.append("\n=============== Headers ===============\n");

    Map<String, List<String>> headers = connection.getHeaderFields();
    for (Map.Entry<String, List<String>> header : headers.entrySet()) {
      List<String> list = header.getValue();

      if (header.getKey() != null) {
        logBuilder.append(header.getKey())
            .append(" : ");
      }

      for (String values : list) {
        logBuilder.append(values);
      }

      logBuilder.append("\n");
    }

    logBuilder.append("\n=============== END Headers ===============\n");

    logBuilder.append("\n");
    logBuilder.append(
        "<-----------------------------END REQUEST--------------------------------->");
    logBuilder.append("\n\n\n");

    return logBuilder.toString();
  }

  private String requestDecodedPath(URL urlConnection) {
    try {
      String path = URLDecoder.decode(urlConnection.toString(), "UTF-8");
      //String query = URLDecoder.decode(urlConnection.toString(), "UTF-8");
      return path;
    } catch (Exception ex) {
      /* Quality */
    }
    return null;
  }
}