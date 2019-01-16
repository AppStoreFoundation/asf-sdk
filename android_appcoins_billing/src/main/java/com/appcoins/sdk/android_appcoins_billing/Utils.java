package com.appcoins.sdk.android_appcoins_billing;

class Utils {

    protected static String ITEM_TYPE_INAPP = "inapp";
    protected static final String ITEM_TYPE_SUBS = "subs";
    protected static int API_VERSION_V3 = 3;
    protected static int API_VERSION_V5 = 5;

    protected static final String IAB_BIND_ACTION = "com.appcoins.wallet.iab.action.BIND";
    protected static final String IAB_BIND_PACKAGE = "com.appcoins.wallet";

    protected static final int BILLING_RESPONSE_RESULT_OK = 0;
    protected static final int IABHELPER_ERROR_BASE = -1000;
    protected static final int IABHELPER_REMOTE_EXCEPTION = -1001;
    protected static final int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;


    protected static final String GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";
    protected static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
    protected static final String RESPONSE_CODE = "RESPONSE_CODE";
    protected static final int IABHELPER_BAD_RESPONSE = -1002;
}
