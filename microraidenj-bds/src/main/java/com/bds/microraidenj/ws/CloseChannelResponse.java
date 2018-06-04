package com.bds.microraidenj.ws;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class CloseChannelResponse {

  @JsonProperty("closingSig") private String closingSig;

  public CloseChannelResponse(String closingSig) {
    this.closingSig = closingSig;
  }

  public String getClosingSig() {
    return closingSig;
  }

  public void setClosingSig(String closingSig) {
    this.closingSig = closingSig;
  }
}
