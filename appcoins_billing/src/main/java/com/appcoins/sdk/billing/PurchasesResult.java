package com.appcoins.sdk.billing;

import com.appcoins.sdk.billing.Purchase;

import java.net.ResponseCache;
import java.util.List;

public class PurchasesResult {

    public List<Purchase> purchases;
    public int responseCode;

    public PurchasesResult(List<Purchase> purchases, int responseCode) {
    }

public PurchasesResult(){}

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }


}
