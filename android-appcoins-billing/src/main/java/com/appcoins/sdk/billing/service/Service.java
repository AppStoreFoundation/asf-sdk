package com.appcoins.sdk.billing.service;

import java.util.List;
import java.util.Map;

public interface Service {

  /**
   * @param endPoint String to be added to the base url of the request
   * @param httpMethod Method of the request to be made: GET, POST, PATCH, DELETE
   * @param paths List of paths to be added to the url
   * @param queries Map of the key values to be added as query
   * @param header Map of the key values to be added to the header
   * @param body Map of the key values to be added to the body.
   * @param serviceResponseListener Listener in which the response will be sent.
   */
  void makeRequest(String endPoint, String httpMethod, List<String> paths,
      Map<String, String> queries, Map<String, String> header, Map<String, Object> body,
      ServiceResponseListener serviceResponseListener);
}
