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

  private WalletGenerationModel() {

    this.walletAddress = "";
    this.signature = "";
    this.error = true;
  }

  public static WalletGenerationModel createErrorWalletGenerationModel() {
    return new WalletGenerationModel();
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
