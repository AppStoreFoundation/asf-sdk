package com.appcoins.sdk.billing.payasguest;

import org.json.JSONObject;

public class Signature {
  private final String value;
  private final JSONObject message;

  public Signature(String value, JSONObject message) {

    this.value = value;
    this.message = message;
  }

  public String getValue() {
    return value;
  }

  public JSONObject getMessage() {
    return message;
  }
}
