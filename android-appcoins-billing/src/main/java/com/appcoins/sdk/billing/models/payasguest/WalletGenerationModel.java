package com.appcoins.sdk.billing.models.payasguest;

public class WalletGenerationModel {

  private final String walletAddress;
  private final String signature;
  private final boolean error;

  public WalletGenerationModel(String walletAddress, String signature, boolean error) {

    this.walletAddress = walletAddress;
    this.signature = signature;
    this.error = error;
  }

  public WalletGenerationModel() {

    this.walletAddress = "";
    this.signature = "";
    this.error = true;
  }

  public String getWalletAddress() {
    return walletAddress;
  }

  public String getSignature() {
    return signature;
  }

  public boolean hasError() {
    return error;
  }
}
