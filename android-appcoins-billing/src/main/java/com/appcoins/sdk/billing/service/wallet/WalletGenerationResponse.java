package com.appcoins.sdk.billing.service.wallet;

class WalletGenerationResponse {

  private final String address;
  private final String ewt;
  private boolean error;

  public WalletGenerationResponse(String address, String ewt, boolean error) {

    this.address = address;
    this.ewt = ewt;
    this.error = error;
  }

  public WalletGenerationResponse() {
    this.address = "";
    this.ewt = "";
    this.error = true;
  }

  public String getEwt() {
    return ewt;
  }

  public String getAddress() {
    return address;
  }

  public boolean hasError() {
    return error;
  }
}
