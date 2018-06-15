package com.bds.microraidenj.ws;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class CloseChannelResponse {

  @JsonProperty("close_sig") private String closingSig;

  public CloseChannelResponse() {
  }

  public String getClosingSig() {
    return closingSig;
  }

  public void setClosingSig(String closingSig) {
    this.closingSig = closingSig;
  }
}
