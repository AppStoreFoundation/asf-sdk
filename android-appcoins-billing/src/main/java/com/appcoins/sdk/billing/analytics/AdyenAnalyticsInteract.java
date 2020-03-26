package com.appcoins.sdk.billing.analytics;

import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.models.billing.AdyenPaymentInfo;

public class AdyenAnalyticsInteract {

  private BillingAnalytics billingAnalytics;

  public AdyenAnalyticsInteract(BillingAnalytics billingAnalytics) {

    this.billingAnalytics = billingAnalytics;
  }

  public void sendConfirmationEvent(AdyenPaymentInfo adyenPaymentInfo, String action) {
    BuyItemProperties buyItemProperties = adyenPaymentInfo.getBuyItemProperties();
    String packageName = buyItemProperties.getPackageName();
    String sku = buyItemProperties.getSku();
    String appcPrice = adyenPaymentInfo.getAppcPrice();
    String paymentMethod = mapPaymentToAnalytics(adyenPaymentInfo.getPaymentMethod());
    String transactionType = buyItemProperties.getType();
    billingAnalytics.sendPaymentConfirmationEvent(packageName, sku, appcPrice, paymentMethod,
        transactionType, action);
  }

  public void sendPaymentErrorEvent(AdyenPaymentInfo adyenPaymentInfo, String errorCode,
      String refusalCode, String refusalDetails) {
    BuyItemProperties buyItemProperties = adyenPaymentInfo.getBuyItemProperties();
    String packageName = buyItemProperties.getPackageName();
    String sku = buyItemProperties.getSku();
    String appcPrice = adyenPaymentInfo.getAppcPrice();
    String paymentMethod = mapPaymentToAnalytics(adyenPaymentInfo.getPaymentMethod());
    String transactionType = buyItemProperties.getType();
    if (errorCode != null) {
      billingAnalytics.sendPaymentErrorEvent(packageName, sku, appcPrice, paymentMethod,
          transactionType, errorCode);
    } else {
      billingAnalytics.sendAdyenErrorEvent(packageName, sku, appcPrice, paymentMethod,
          transactionType, refusalCode, refusalDetails);
    }
  }

  public void sendPaymentSuccessEvent(AdyenPaymentInfo adyenPaymentInfo) {
    BuyItemProperties buyItemProperties = adyenPaymentInfo.getBuyItemProperties();
    String packageName = buyItemProperties.getPackageName();
    String sku = buyItemProperties.getSku();
    String appcPrice = adyenPaymentInfo.getAppcPrice();
    String paymentMethod = mapPaymentToAnalytics(adyenPaymentInfo.getPaymentMethod());
    String transactionType = buyItemProperties.getType();
    billingAnalytics.sendPaymentSuccessEvent(packageName, sku, appcPrice, paymentMethod,
        transactionType);
  }

  private String mapPaymentToAnalytics(String paymentMethod) {
    if (paymentMethod.equals("credit_card")) {
      return BillingAnalytics.PAYMENT_METHOD_CC;
    } else {
      return BillingAnalytics.PAYMENT_METHOD_PAYPAL;
    }
  }
}
