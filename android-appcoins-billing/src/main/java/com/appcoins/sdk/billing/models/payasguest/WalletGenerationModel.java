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
