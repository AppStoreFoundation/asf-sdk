package com.appcoins.sdk.billing.service;

import android.os.AsyncTask;
import java.util.List;
import java.util.Map;

public class ServiceAsyncTask extends AsyncTask {

  private final String httpMethod;
  private final List<String> paths;
  private final Map<String, String> queries;
  private final Map<String, String> body;
  private String baseUrl;
  private String endPoint;
  private ServiceResponseListener serviceResponseListener;

  ServiceAsyncTask(String baseUrl, String endPoint, String httpMethod, List<String> paths,
      Map<String, String> queries, Map<String, String> body,
      ServiceResponseListener serviceResponseListener) {
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
        BdsService.createRequest(baseUrl, endPoint, httpMethod, paths, queries, body);
    serviceResponseListener.onResponseReceived(requestResponse);
    return null;
  }
}
