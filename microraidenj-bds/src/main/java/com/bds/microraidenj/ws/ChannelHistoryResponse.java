package com.bds.microraidenj.ws;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ChannelHistoryResponse {

  @JsonProperty("result") private List<MakePaymentResponse.Result> result;

  public List<MakePaymentResponse.Result> getResult() {
    return result;
  }

  public void setResult(List<MakePaymentResponse.Result> result) {
    this.result = result;
  }
}
