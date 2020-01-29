package com.appcoins.sdk.billing.service;

import java.util.List;
import java.util.Map;

public interface Service {

  void makeRequest(String endPoint, String httpMethod, List<String> paths,
      Map<String, String> queries, Map<String, String> body,
      ServiceResponseListener serviceResponseListener);
}
