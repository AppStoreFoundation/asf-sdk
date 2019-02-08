package com.appcoins.net;

import java.util.HashMap;

public abstract class Operation {

  public abstract String mapParams(QueryParams queryParams);

  public abstract AppcoinsClientResponse mapResponse(String response);

}
