package com.appcoins.sdk.android_appcoins_billing.types;

import com.appcoins.sdk.android_appcoins_billing.helpers.Utils;

public class IabResult {
    int mResponse;
    String mMessage;

    public IabResult(int response, String message) {
        mResponse = response;
        if (message == null || message.trim().length() == 0) {
            mMessage = Utils.getResponseDesc(response);
        }
        else {
            mMessage = message + " (response: " + Utils.getResponseDesc(response) + ")";
        }
    }
    public int getResponse() { return mResponse; }
    public String getMessage() { return mMessage; }
    public boolean isSuccess() { return mResponse == Utils.BILLING_RESPONSE_RESULT_OK; }
    public boolean isFailure() { return !isSuccess(); }
    public String toString() { return "IabResult: " + getMessage(); }

}

