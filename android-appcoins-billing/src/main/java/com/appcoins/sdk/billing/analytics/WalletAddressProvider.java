package com.appcoins.sdk.billing.analytics;

public class WalletAddressProvider {

  private static WalletAddressProvider instance;
  private String walletAddress;

  private WalletAddressProvider() {

  }

  public static WalletAddressProvider provideWalletAddressProvider() {
    if (instance == null) {
      instance = new WalletAddressProvider();
    }
    return instance;
  }

  public void saveWalletAddress(String walletAddress) {
    this.walletAddress = walletAddress;
  }

  /**
   * This method may return a null Wallet Address
   * @return Previously saved wallet address
   */
  String getWalletAddress() {
    return walletAddress;
  }
}
