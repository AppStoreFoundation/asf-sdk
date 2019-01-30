package com.appcoins.sdk.billing;

import java.util.List;

public class SkuDetailsParams {

    private String itemType;

    private List<String> moreItemSkus;

    private List<String> moreSubsSkus;

    public List<String> getMoreSubsSkus() {
        return moreSubsSkus;
    }

    public void setMoreSubsSkus(List<String> moreSubsSkus) {
        this.moreSubsSkus = moreSubsSkus;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public List<String> getMoreItemSkus() {
        return moreItemSkus;
    }

    public void setMoreItemSkus(List<String> moreItemSkus) {

        this.moreItemSkus = moreItemSkus;
    }
}
