package com.appcoins.sdk.billing.analytics;

interface EventSender {

  void sendPaymentMethodEvent(String packageName, String skuDetails, String value,
      String purchaseDetails, String transactionType, String action);

  void sendPaymentConfirmationEvent(String packageName, String skuDetails, String value,
      String purchaseDetails, String transactionType, String action);

  void sendPaymentErrorEvent(String packageName, String skuDetails, String value,
      String purchaseDetails, String transactionType, String errorCode);

  void sendAdyenErrorEvent(String packageName, String skuDetails, String value,
      String purchaseDetails, String transactionType, String errorCode, String errorDetails);

  void sendPaymentSuccessEvent(String packageName, String skuDetails, String value,
      String purchaseDetails, String transactionType);

  void sendPurchaseStartEvent(String packageName, String skuDetails, String value,
      String transactionType, String context);
}
