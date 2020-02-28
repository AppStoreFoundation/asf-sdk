package com.appcoins.sdk.billing.service.wallet;

class WalletGenerationResponse {

  private final String address;
  private final String ewt;
  private String signature;
  private boolean error;

  public WalletGenerationResponse(String address, String ewt, String signature, boolean error) {

    this.address = address;
    this.ewt = ewt;
    this.signature = signature;
    this.error = error;
  }

  public WalletGenerationResponse() {
    this.address = "";
    this.ewt = "";
    this.signature = "";
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

  public String getSignature() {
    return signature;
  }
}
