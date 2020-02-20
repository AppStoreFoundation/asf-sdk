package com.appcoins.sdk.billing.payasguest;

import com.appcoins.sdk.billing.BuyItemProperties;

public class AdyenPaymentInfo {

  private final String paymentMethod;
  private final String walletAddress;
  private final String ewt;
  private final String fiatPrice;
  private final String fiatCurrency;
  private final String appcPrice;
  private BuyItemProperties buyItemProperties;

  AdyenPaymentInfo(String paymentMethod, String walletAddress, String ewt, String fiatPrice,
      String fiatCurrency, String appcPrice, BuyItemProperties buyItemProperties) {

    this.paymentMethod = paymentMethod;
    this.walletAddress = walletAddress;
    this.ewt = ewt;
    this.fiatPrice = fiatPrice;
    this.fiatCurrency = fiatCurrency;
    this.appcPrice = appcPrice;
    this.buyItemProperties = buyItemProperties;
  }

  public String getWalletAddress() {
    return walletAddress;
  }

  public String getEwt() {
    return ewt;
  }

  public String getFiatPrice() {
    return fiatPrice;
  }

  public String getFiatCurrency() {
    return fiatCurrency;
  }

  public String getAppcPrice() {
    return appcPrice;
  }

  public BuyItemProperties getBuyItemProperties() {
    return buyItemProperties;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }
}
