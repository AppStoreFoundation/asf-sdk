package com.appcoins.sdk.billing;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
 * Represents an in-app billing purchase.
 */
public class Purchase {
    String mItemType;  // ITEM_TYPE_INAPP or ITEM_TYPE_SUBS
    String mOrderId;
    String mPackageName;
    String mSku;
    long mPurchaseTime;
    int mPurchaseState;
    String mDeveloperPayload;
    String mToken;
    String mOriginalJson;
    String mSignature;
    boolean mIsAutoRenewing;

    public Purchase(String id, String itemType, String jsonPurchaseInfo, String signature) throws
            JsonParseException {
        mItemType = itemType;
        mOriginalJson = jsonPurchaseInfo;

        JsonObject o = parseJsonObject();

        mOrderId = o.get("orderId").getAsString();
        mPackageName = o.get("packageName").getAsString();
        mSku = o.get("productId").getAsString();
        mPurchaseTime = o.get("purchaseTime").getAsLong();
        mPurchaseState = o.get("purchaseState").getAsInt();
        mDeveloperPayload = o.get("developerPayload").getAsString();
        mToken = o.get("token").getAsString();
        if(mToken == null){
            mToken = o.get("purchaseToken").getAsString();
        }

        mIsAutoRenewing = o.get("autoRenewing").getAsBoolean();
        mSignature = signature;
        mToken = id;
    }

    public String getItemType() { return mItemType; }
    public String getOrderId() { return mOrderId; }
    public String getPackageName() { return mPackageName; }
    public String getSku() { return mSku; }
    public long getPurchaseTime() { return mPurchaseTime; }
    public int getPurchaseState() { return mPurchaseState; }
    public String getDeveloperPayload() { return mDeveloperPayload; }
    public String getToken() { return mToken; }
    public String getOriginalJson() { return mOriginalJson; }
    public String getSignature() { return mSignature; }
    public boolean isAutoRenewing() { return mIsAutoRenewing; }

    private JsonObject parseJsonObject() throws JsonParseException{
        JsonElement jelement = new JsonParser().parse(mOriginalJson);
        return jelement.getAsJsonObject();
    }

    @Override
    public String toString() { return "PurchaseInfo(type:" + mItemType + "):" + mOriginalJson; }
}
