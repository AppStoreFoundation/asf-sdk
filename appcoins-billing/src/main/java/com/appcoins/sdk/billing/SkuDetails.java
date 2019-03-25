package com.appcoins.sdk.billing;

public class SkuDetails {
  private final String itemType;
  private final String sku;
  private final String type;
  private final String price;
  private final long priceAmountMicros;
  private final String priceCurrencyCode;
  private final String title;
  private final String description;

  public SkuDetails(String itemType, String sku, String type, String price, long priceAmountMicros,
      String priceCurrencyCode, String title, String description) {
    this.itemType = itemType;
    this.sku = sku;
    this.type = type;
    this.price = price;
    this.priceAmountMicros = priceAmountMicros;
    this.priceCurrencyCode = priceCurrencyCode;
    this.title = title;
    this.description = description;
  }

  public String getItemType() {
    return itemType;
  }

  public String getSku() {
    return sku;
  }

  public String getType() {
    return type;
  }

  public String getPrice() {
    return price;
  }

  public long getPriceAmountMicros() {
    return priceAmountMicros;
  }

  public String getPriceCurrencyCode() {
    return priceCurrencyCode;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }
}
