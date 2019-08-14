package com.appcoins.sdk.billing;

public class SkuDetails {
  private final String itemType;
  private final String sku;
  private final String type;
  private final String price;
  private final String priceCurrencyCode;
  private final long priceAmountMicros;
  private final String appcPrice;
  private final String appcPriceCurrencyCode;
  private final long appcPriceAmountMicros;
  private final String fiatPrice;
  private final String fiatPriceCurrencyCode;
  private final long fiatPriceAmountMicros;
  private final String title;
  private final String description;

  public SkuDetails(String itemType, String sku, String type, String price, long priceAmountMicros,
      String priceCurrencyCode, String appcPrice, long appcPriceAmountMicros,
      String appcPriceCurrencyCode, String fiatPrice, long fiatPriceAmountMicros,
      String fiatPriceCurrencyCode, String title, String description) {
    this.itemType = itemType;
    this.sku = sku;
    this.type = type;
    this.price = price;
    this.priceAmountMicros = priceAmountMicros;
    this.priceCurrencyCode = priceCurrencyCode;
    this.appcPrice = appcPrice;
    this.appcPriceAmountMicros = appcPriceAmountMicros;
    this.appcPriceCurrencyCode = appcPriceCurrencyCode;
    this.fiatPrice = fiatPrice;
    this.fiatPriceAmountMicros = fiatPriceAmountMicros;
    this.fiatPriceCurrencyCode = fiatPriceCurrencyCode;
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

  public String getAppcPrice() {
    return appcPrice;
  }

  public String getAppcPriceCurrencyCode() {
    return appcPriceCurrencyCode;
  }

  public long getAppcPriceAmountMicros() {
    return appcPriceAmountMicros;
  }

  public String getFiatPrice() {
    return fiatPrice;
  }

  public String getFiatPriceCurrencyCode() {
    return fiatPriceCurrencyCode;
  }

  public long getFiatPriceAmountMicros() {
    return fiatPriceAmountMicros;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }
}
