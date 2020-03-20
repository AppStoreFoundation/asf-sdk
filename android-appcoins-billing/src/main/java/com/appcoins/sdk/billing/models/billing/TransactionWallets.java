package com.appcoins.sdk.billing.models.billing;

public class TransactionWallets {

  private final String mainWalletAddress;
  private final String developerWalletAddress;
  private final String oemWalletAddress;
  private final String storeWalletAddress;
  private final String userWalletAddress;

  public TransactionWallets(String mainWalletAddress, String developerWalletAddress,
      String oemWalletAddress, String storeWalletAddress, String userWalletAddress) {

    this.mainWalletAddress = mainWalletAddress;
    this.developerWalletAddress = developerWalletAddress;
    this.oemWalletAddress = oemWalletAddress;
    this.storeWalletAddress = storeWalletAddress;
    this.userWalletAddress = userWalletAddress;
  }

  public String getMainWalletAddress() {
    return mainWalletAddress;
  }

  public String getDeveloperWalletAddress() {
    return developerWalletAddress;
  }

  public String getOemWalletAddress() {
    return oemWalletAddress;
  }

  public String getStoreWalletAddress() {
    return storeWalletAddress;
  }

  public String getUserWalletAddress() {
    return userWalletAddress;
  }
}
