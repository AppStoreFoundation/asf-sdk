package com.appcoins.sdk.billing.models;

public class WalletGenerationModel {

  private final String walletAddress;
  private final String ewt;
  private final String signature;
  private final boolean error;

  public WalletGenerationModel(String walletAddress, String ewt, String signature, boolean error) {

    this.walletAddress = walletAddress;
    this.ewt = ewt;
    this.signature = signature;
    this.error = error;
  }

  public String getWalletAddress() {
    return walletAddress;
  }

  public String getEwt() {
    return ewt;
  }

  public String getSignature() {
    return signature;
  }

  public boolean hasError() {
    return error;
  }
}
