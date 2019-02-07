package com.appcoins.net;

public abstract class Operation {

  public abstract String mapParams(QueryParams queryParams);

  public abstract String mapResponse(String response);

}
