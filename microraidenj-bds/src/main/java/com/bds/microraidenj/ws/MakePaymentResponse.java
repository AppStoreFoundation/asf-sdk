package com.bds.microraidenj.ws;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MakePaymentResponse {

  @JsonProperty("result") private String result;

  public MakePaymentResponse() {
  }

  public String getResult() {
    return result;
  }
}
