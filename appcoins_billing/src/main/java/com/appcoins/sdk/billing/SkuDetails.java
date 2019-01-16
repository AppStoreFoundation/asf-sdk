package com.appcoins.sdk.billing;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class SkuDetails {
    private final String mItemType;
    private final String mSku;
    private final String mType;
    private final String mPrice;
    private final long mPriceAmountMicros;
    private final String mPriceCurrencyCode;
    private final String mTitle;
    private final String mDescription;
    private final String mJson;


    /*
    public SkuDetails(String jsonSkuDetails,String type) throws JsonParseException {
        this(type, jsonSkuDetails);
    }
    */
    public SkuDetails(String itemType, String jsonSkuDetails) throws Exception {
        mItemType = itemType;
        mJson = jsonSkuDetails;

        JsonObject o = parseJsonObject();

        mSku = o.get("productId").getAsString();
        mType = o.get("type").getAsString();
        mPrice = o.get("price").getAsString();
        mPriceAmountMicros = o.get("price_amount_micros").getAsLong();
        mPriceCurrencyCode = o.get("price_currency_code").getAsString();
        mTitle = o.get("title").getAsString();
        mDescription = o.get("description").getAsString();
    }

    public String getSku() { return mSku; }
    public String getType() { return mType; }
    public String getPrice() { return mPrice; }
    public long getPriceAmountMicros() { return mPriceAmountMicros; }
    public String getPriceCurrencyCode() { return mPriceCurrencyCode; }
    public String getTitle() { return mTitle; }
    public String getDescription() { return mDescription; }

    private JsonObject parseJsonObject() throws JsonParseException{
        JsonElement jelement = new JsonParser().parse(mJson);
        return jelement.getAsJsonObject();
    }

    public String toString(String json) {
        return "SkuDetails:" + mJson;
    }
}
