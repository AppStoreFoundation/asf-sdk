package com.appcoins.sdk.billing.models;

public class TransactionInformation {

  private final String value;
  private final String currency;
  private final String reference;
  private final String paymentType;
  private final String origin;
  private final String packageName;
  private final String metadata;
  private final String sku;
  private final String callbackUrl;
  private final String transactionType;

  public TransactionInformation(String value, String currency, String reference, String paymentType,
      String origin, String packageName, String metadata, String sku, String callbackUrl,
      String transactionType) {

    this.value = value;
    this.currency = currency;
    this.reference = reference;
    this.paymentType = paymentType;
    this.origin = origin;
    this.packageName = packageName;
    this.metadata = metadata;
    this.sku = sku;
    this.callbackUrl = callbackUrl;
    this.transactionType = transactionType;
  }

  public String getValue() {
    return value;
  }

  public String getCurrency() {
    return currency;
  }

  public String getReference() {
    return reference;
  }

  public String getPaymentType() {
    return paymentType;
  }

  public String getOrigin() {
    return origin;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getMetadata() {
    return metadata;
  }

  public String getSku() {
    return sku;
  }

  public String getCallbackUrl() {
    return callbackUrl;
  }

  public String getTransactionType() {
    return transactionType;
  }
}
