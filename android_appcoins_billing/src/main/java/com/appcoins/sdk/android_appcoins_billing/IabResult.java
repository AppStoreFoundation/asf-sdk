package com.appcoins.sdk.android_appcoins_billing;

public class IabResult {
    int mResponse;
    String mMessage;

    public IabResult(int response, String message) {
        mResponse = response;
        if (message == null || message.trim().length() == 0) {
            mMessage = getResponseDesc(response);
        }
        else {
            mMessage = message + " (response: " + getResponseDesc(response) + ")";
        }
    }
    public int getResponse() { return mResponse; }
    public String getMessage() { return mMessage; }
    public boolean isSuccess() { return mResponse == Utils.BILLING_RESPONSE_RESULT_OK; }
    public boolean isFailure() { return !isSuccess(); }
    public String toString() { return "IabResult: " + getMessage(); }


    private String getResponseDesc(int code) {
        String[] iab_msgs = ("0:OK/1:User Canceled/2:Unknown/"
                + "3:Billing Unavailable/4:Item unavailable/"
                + "5:Developer Error/6:Error/7:Item Already Owned/"
                + "8:Item not owned").split("/");
        String[] iabhelper_msgs = ("0:OK/-1001:Remote exception during initialization/"
                + "-1002:Bad response received/"
                + "-1003:Purchase signature verification failed/"
                + "-1004:Send intent failed/"
                + "-1005:User cancelled/"
                + "-1006:Unknown purchase response/"
                + "-1007:Missing token/"
                + "-1008:Unknown error/"
                + "-1009:Subscriptions not available/"
                + "-1010:Invalid consumption attempt").split("/");

        if (code <= Utils.IABHELPER_ERROR_BASE) {
            int index = Utils.IABHELPER_ERROR_BASE - code;
            if (index >= 0 && index < iabhelper_msgs.length) {
                return iabhelper_msgs[index];
            } else {
                return String.valueOf(code) + ":Unknown IAB Helper Error";
            }
        } else if (code < 0 || code >= iab_msgs.length) {
            return String.valueOf(code) + ":Unknown";
        } else {
            return iab_msgs[code];
        }
    }

}

