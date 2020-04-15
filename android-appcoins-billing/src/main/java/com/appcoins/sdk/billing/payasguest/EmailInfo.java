package com.appcoins.sdk.billing.payasguest;

class EmailInfo {
  private final String walletAddress;
  private final String packageName;
  private final String sku;
  private final String sdkVersionName;
  private final int mobileVersion;
  private final String appName;

  public EmailInfo(String walletAddress, String packageName, String sku, String sdkVersionName,
      int mobileVersion, String appName) {

    this.walletAddress = walletAddress;
    this.packageName = packageName;
    this.sku = sku;
    this.sdkVersionName = sdkVersionName;
    this.mobileVersion = mobileVersion;
    this.appName = appName;
  }

  public String getWalletAddress() {
    return walletAddress;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getSku() {
    return sku;
  }

  public String getSdkVersionName() {
    return sdkVersionName;
  }

  public int getMobileVersion() {
    return mobileVersion;
  }

  public String getAppName() {
    return appName;
  }
}
