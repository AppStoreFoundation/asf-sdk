package com.appcoins.sdk.billing.models.billing;

import com.appcoins.sdk.billing.BuyItemProperties;

public class AdyenPaymentInfo {

  private final String paymentMethod;
  private final String walletAddress;
  private final String fiatPrice;
  private final String fiatCurrency;
  private final String appcPrice;
  private String signature;
  private boolean shouldResume;
  private String toResumeTransactionId;
  private BuyItemProperties buyItemProperties;

  public AdyenPaymentInfo(String paymentMethod, String walletAddress, String signature,
      String fiatPrice, String fiatCurrency, String appcPrice, boolean shouldResume,
      String toResumeTransactionId, BuyItemProperties buyItemProperties) {

    this.paymentMethod = paymentMethod;
    this.walletAddress = walletAddress;
    this.signature = signature;
    this.fiatPrice = fiatPrice;
    this.fiatCurrency = fiatCurrency;
    this.appcPrice = appcPrice;
    this.shouldResume = shouldResume;
    this.toResumeTransactionId = toResumeTransactionId;
    this.buyItemProperties = buyItemProperties;
  }

  public String getWalletAddress() {
    return walletAddress;
  }

  public String getSignature() {
    return signature;
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

  public boolean shouldResumeTransaction() {
    return shouldResume;
  }

  public String getToResumeTransactionId() {
    return toResumeTransactionId;
  }
}
