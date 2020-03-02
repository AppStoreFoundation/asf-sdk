package com.appcoins.sdk.billing.payasguest;

import android.os.Bundle;

class BillingMapper {

  private final static String INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
  private final static String INAPP_DATA_SIGNATURE = "INAPP_DATA_SIGNATURE";
  private final static String INAPP_ORDER_REFERENCE = "order_reference";
  private final static String INAPP_PURCHASE_ID = "INAPP_PURCHASE_ID";
  private final static String RESPONSE_CODE = "RESPONSE_CODE";

  public BillingMapper() {

  }

  public Bundle map(PurchaseModel purchaseModel, String orderReference) {
    Bundle bundle = new Bundle();
    SkuPurchase skuPurchase = purchaseModel.getPurchase();
    bundle.putString(INAPP_PURCHASE_ID, skuPurchase.getUid());
    bundle.putString(INAPP_PURCHASE_DATA, skuPurchase.getSignature()
        .getMessage()
        .toString());
    bundle.putString(INAPP_DATA_SIGNATURE, skuPurchase.getSignature()
        .getValue());
    bundle.putString(INAPP_ORDER_REFERENCE, orderReference);
    bundle.putInt(RESPONSE_CODE, 0); //Success
    return bundle;
  }
}
