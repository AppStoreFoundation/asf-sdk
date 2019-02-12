package com.appcoins.net;

import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogCreator {

  public static String Intercept(HttpURLConnection connection, String response) throws IOException {

    StringBuilder logBuilder = new StringBuilder();
    logBuilder.append("<---BEGIN REQUEST-->");
    logBuilder.append("\n");
    String urlpath = URLEncoder.encode(connection.toString(), "UTF-8");
    logBuilder.append(urlpath);
    logBuilder.append(response);

    //Connection details
   /* String path = connection.getResponseCode() + "" //200
        + connection.getResponseMessage();
    String urlpath = URLEncoder.encode(connection.toString(), "UTF-8");
    StringBuilder logBuilder = new StringBuilder();
    logBuilder.append("<---BEGIN REQUEST-->");
    logBuilder.append("\n");
    logBuilder.append("Request encoded url: ")
        .append(connection.getRequestMethod())
        .append(" ")
        .append(urlpath);
    logBuilder.append("\n");

    //Header details
    Map<String,List<String>> headers = new HashMap<String, List<String>>();
    headers = connection.getHeaderFields();
    logBuilder.append("\n=============== Headers ===============\n");
    for (int i = headers.size() - 1; i > -1; i--) {
      logBuilder.append(connection.getHeaderField(i))
          .append(" : ")
          .append(connection.getHeaderField(i))
          .append("\n");
    }
    logBuilder.append("\n=============== END Headers ===============\n");

    //Response Details
    //String response = appcoinsHTTPClient.getResponse();
    //logBuilder.append(response);*/

    return logBuilder.toString();
  }

  private String requestDecodedPath(URL urlConnection) {
    try {
      String path = URLDecoder.decode(urlConnection.toString(), "UTF-8");
      String query = URLDecoder.decode(urlConnection.toString(), "UTF-8");
      return path;
    } catch (Exception ex) {
      /* Quality */
    }
    return null;
  }
}