package com.appcoins.sdk.billing.models;

public class WalletGenerationModel {

  private final String walletAddress;
  private final String ewt;
  private boolean error;

  public WalletGenerationModel(String walletAddress, String ewt, boolean error) {

    this.walletAddress = walletAddress;
    this.ewt = ewt;
    this.error = error;
  }

  public String getWalletAddress() {
    return walletAddress;
  }

  public String getEwt() {
    return ewt;
  }

  public boolean hasError() {
    return error;
  }
}
