package com.appcoins.sdk.billing.service.wallet;

class WalletGenerationResponse {

  private final String address;
  private String signature;
  private boolean error;

  WalletGenerationResponse(String address, String signature, boolean error) {

    this.address = address;
    this.signature = signature;
    this.error = error;
  }

  WalletGenerationResponse() {
    this.address = "";
    this.signature = "";
    this.error = true;
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
