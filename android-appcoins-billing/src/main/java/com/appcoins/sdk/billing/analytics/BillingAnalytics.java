package com.appcoins.sdk.billing.analytics;

import cm.aptoide.analytics.AnalyticsManager;
import java.util.HashMap;
import java.util.Map;

public class BillingAnalytics implements EventSender {
  public static final String START_PAYMENT_METHOD = "appcoins_guest_sdk_payment_method";
  public static final String START_INSTALL = "appcoins_guest_sdk_install_wallet";
  public static final String EVENT_CANCEL = "cancel";
  public static final String EVENT_BUY = "buy";
  public static final String EVENT_NEXT = "next";
  public static final String PAYMENT_METHOD_INSTALL_WALLET = "install_wallet";
  static final String PAYMENT_METHOD_CC = "credit_card";
  static final String PAYMENT_METHOD_PAYPAL = "paypal";
  static final String PAYMENT_START = "appcoins_guest_sdk_payment_start";
  static final String PAYMENT_METHOD = "appcoins_guest_sdk_payment_method";
  static final String PAYMENT_CONFIRMATION = "appcoins_guest_sdk_payment_confirmation";
  static final String PAYMENT_CONCLUSION = "appcoins_guest_sdk_payment_conclusion";
  private static final String SDK = "AppCoinsGuestSDK";
  private static final String EVENT_PACKAGE_NAME = "package_name";
  private static final String EVENT_SKU = "sku";
  private static final String EVENT_VALUE = "value";
  private static final String EVENT_TRANSACTION_TYPE = "transaction_type";
  private static final String EVENT_PAYMENT_METHOD = "payment_method";
  private static final String EVENT_ACTION = "action";
  private static final String EVENT_CONTEXT = "context";
  private static final String EVENT_STATUS = "status";
  private static final String EVENT_ERROR_CODE = "error_code";
  private static final String EVENT_ERROR_DETAILS = "error_details";
  private static final String EVENT_SUCCESS = "success";
  private static final String EVENT_FAIL = "fail";
  private final AnalyticsManager analytics;

  public BillingAnalytics(AnalyticsManager analytics) {
    this.analytics = analytics;
  }

  @Override public void sendPaymentMethodEvent(String packageName, String skuDetails, String value,
      String purchaseDetails, String transactionType, String action) {
    Map<String, Object> eventData =
        createBaseRakamEventMap(packageName, skuDetails, value, purchaseDetails, transactionType,
            action);

    analytics.logEvent(eventData, PAYMENT_METHOD, AnalyticsManager.Action.CLICK, SDK);
  }

  @Override
  public void sendPaymentConfirmationEvent(String packageName, String skuDetails, String value,
      String purchaseDetails, String transactionType, String action) {
    Map<String, Object> eventData =
        createBaseRakamEventMap(packageName, skuDetails, value, purchaseDetails, transactionType,
            action);

    analytics.logEvent(eventData, PAYMENT_CONFIRMATION, AnalyticsManager.Action.CLICK, SDK);
  }

  @Override public void sendPaymentErrorEvent(String packageName, String skuDetails, String value,
      String purchaseDetails, String transactionType, String errorCode) {
    Map<String, Object> eventData =
        createConclusionRakamEventMap(packageName, skuDetails, value, purchaseDetails,
            transactionType, EVENT_FAIL);

    eventData.put(EVENT_ERROR_CODE, errorCode);

    analytics.logEvent(eventData, PAYMENT_CONCLUSION, AnalyticsManager.Action.CLICK, SDK);
  }

  @Override public void sendAdyenErrorEvent(String packageName, String skuDetails, String value,
      String purchaseDetails, String transactionType, String errorCode, String errorDetails) {
    Map<String, Object> eventData =
        createConclusionRakamEventMap(packageName, skuDetails, value, purchaseDetails,
            transactionType, EVENT_FAIL);

    eventData.put(EVENT_ERROR_CODE, errorCode);
    eventData.put(EVENT_ERROR_DETAILS, errorDetails);

    analytics.logEvent(eventData, PAYMENT_CONCLUSION, AnalyticsManager.Action.CLICK, SDK);
  }

  @Override public void sendPaymentSuccessEvent(String packageName, String skuDetails, String value,
      String purchaseDetails, String transactionType) {
    Map<String, Object> eventData =
        createConclusionRakamEventMap(packageName, skuDetails, value, purchaseDetails,
            transactionType, EVENT_SUCCESS);

    analytics.logEvent(eventData, PAYMENT_CONCLUSION, AnalyticsManager.Action.CLICK, SDK);
  }

  @Override public void sendPurchaseStartEvent(String packageName, String skuDetails, String value,
      String transactionType, String context) {
    Map<String, Object> eventData = new HashMap<>();

    eventData.put(EVENT_PACKAGE_NAME, packageName);
    eventData.put(EVENT_SKU, skuDetails);
    eventData.put(EVENT_VALUE, value);
    eventData.put(EVENT_TRANSACTION_TYPE, transactionType);
    eventData.put(EVENT_CONTEXT, context);

    analytics.logEvent(eventData, PAYMENT_START, AnalyticsManager.Action.CLICK, SDK);
  }

  private Map<String, Object> createBaseRakamEventMap(String packageName, String skuDetails,
      String value, String purchaseDetails, String transactionType, String action) {
    Map<String, Object> eventData = new HashMap<>();

    eventData.put(EVENT_PACKAGE_NAME, packageName);
    eventData.put(EVENT_SKU, skuDetails);
    eventData.put(EVENT_VALUE, value);
    eventData.put(EVENT_TRANSACTION_TYPE, transactionType);
    eventData.put(EVENT_PAYMENT_METHOD, purchaseDetails);
    eventData.put(EVENT_ACTION, action);

    return eventData;
  }

  private Map<String, Object> createConclusionRakamEventMap(String packageName, String skuDetails,
      String value, String purchaseDetails, String transactionType, String status) {
    Map<String, Object> eventData = new HashMap<>();

    eventData.put(EVENT_PACKAGE_NAME, packageName);
    eventData.put(EVENT_SKU, skuDetails);
    eventData.put(EVENT_VALUE, value);
    eventData.put(EVENT_TRANSACTION_TYPE, transactionType);
    eventData.put(EVENT_PAYMENT_METHOD, purchaseDetails);
    eventData.put(EVENT_STATUS, status);

    return eventData;
  }
}
