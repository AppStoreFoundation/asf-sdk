package com.appcoins.sdk.android_appcoins_billing;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.appcoins.sdk.billing.Inventory;
import com.appcoins.sdk.billing.Purchase;
import com.appcoins.sdk.billing.PurchasesResult;

import java.util.ArrayList;
import java.util.List;

public class IabHelper implements ServiceConnection {

    private WalletBillingService mService;

    private Context mContext;

    private OnIabSetupFinishedListener listener;

    public IabHelper(Context ctx, String base64PublicKey){
        this.mContext = ctx;
        Utils.mSignatureBase64 = base64PublicKey;
    }



    private boolean mSubscriptionsSupported;

    private boolean mSubscriptionUpdateSupported;

    boolean mDebugLog = true;

    String mDebugTag = "IabHelper";

    PurchasesResult purchasesResult;

    Inventory inventory;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = new WalletBillingService(service);
        try {
            checkBillingVersionV3INAPP(mService, mContext.getPackageName(), Utils.API_VERSION_V3 , Utils.ITEM_TYPE_INAPP);
            checkBillingVersionV5SUBS(mService, mContext.getPackageName() , Utils.API_VERSION_V5 , Utils.ITEM_TYPE_SUBS);
            checkBillingVersionV3SUBS(mService, mContext.getPackageName(), Utils.API_VERSION_V3 , Utils.ITEM_TYPE_SUBS);
        } catch (RemoteException e) {
            if (listener != null) {
                listener.onIabSetupFinished(new IabResult(Utils.IABHELPER_REMOTE_EXCEPTION, "RemoteException while setting up in-app billing."));
            }
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d("Message","Disconnected");
    }

    @Override
    public void onBindingDied(ComponentName name) {
        Log.d("Message","Connection Died");
    }

    private void checkBillingVersionV3INAPP(WalletBillingService service , String packageName , int apiVersion , String type) throws RemoteException {
        int response = service .isBillingSupported(apiVersion, packageName, type);

        if (response != Utils.BILLING_RESPONSE_RESULT_OK)
        {
            if(listener != null){
                listener.onIabSetupFinished(new IabResult(response,"Error checking for billing v3 support."+packageName));
            }
        }
        else
        {
            Log.d("Message","In-app billing version 3 supported for " + packageName);
        }
    }


    private void checkBillingVersionV5SUBS(WalletBillingService service , String packageName , int apiVersion , String type) throws RemoteException {
        int response = service.isBillingSupported(apiVersion, packageName, type);

        if (response == Utils.BILLING_RESPONSE_RESULT_OK) {
            Log.d("Message","Subscription re-signup AVAILABLE.");
            mSubscriptionUpdateSupported = true;
        } else {
            Log.d("Message","Subscription re-signup not available.");
            mSubscriptionUpdateSupported = false;
        }
    }

    private void checkBillingVersionV3SUBS(WalletBillingService service , String packageName , int apiVersion , String type) throws RemoteException {
        if (mSubscriptionUpdateSupported) {
            mSubscriptionsSupported = true;
        } else {
            // check for v3 subscriptions support
            int response = service.isBillingSupported(apiVersion, packageName, type);
            if (response == Utils.BILLING_RESPONSE_RESULT_OK) {
                Log.d("Message","Subscriptions AVAILABLE.");
                mSubscriptionsSupported = true;
            } else {
                Log.d("Message","Subscriptions NOT AVAILABLE. Response: " + response);
                mSubscriptionsSupported = false;
                mSubscriptionUpdateSupported = false;
            }
        }
    }



    public void startService(OnIabSetupFinishedListener listener){

        Intent serviceIntent = new Intent(Utils.IAB_BIND_ACTION);
        serviceIntent.setPackage(Utils.IAB_BIND_PACKAGE);
        this.listener = listener;

        List<ResolveInfo> intentServices = mContext.getPackageManager().queryIntentServices(serviceIntent, 0);
        if (intentServices != null && !intentServices.isEmpty()) {
            mContext.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
        }else{
            if (listener != null) {
                listener.onIabSetupFinished(new IabResult(Utils.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE,
                        "Billing service unavailable on device."));
            }
        }
    }

    public PurchasesResult queryPurchases(Inventory inv, String skuType){

        PurchasesResult purchasesResult = new PurchasesResult();

        // Query purchases
        logDebug("Querying owned items, item type: " + skuType);
        logDebug("Package name: " + mContext.getPackageName());
        boolean verificationFailed = false;
        String continueToken = null;
        do {
            logDebug("Calling getPurchases with continuation token: " + continueToken);
            Bundle ownedItems =
                    null;
            try {
                ownedItems = mService.getPurchases(3, mContext.getPackageName(), skuType, continueToken);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            int response = getResponseCodeFromBundle(ownedItems);
            purchasesResult.setResponseCode(response);
            logDebug("Owned items response: " + String.valueOf(response));
            if (response != Utils.BILLING_RESPONSE_RESULT_OK) {
                logDebug("getPurchases() failed: " + getResponseDesc(response));
                return purchasesResult;
            }

            if (!ownedItems.containsKey(Utils.RESPONSE_INAPP_ITEM_LIST) || !ownedItems.containsKey(
                    Utils.RESPONSE_INAPP_PURCHASE_DATA_LIST) || !ownedItems.containsKey(
                    Utils.RESPONSE_INAPP_SIGNATURE_LIST)) {
                logError("Bundle returned from getPurchases() doesn't contain required fields.");
                purchasesResult.setResponseCode(Utils.IABHELPER_BAD_RESPONSE);
                return purchasesResult;
            }

            ArrayList<String> ownedSkus = ownedItems.getStringArrayList(Utils.RESPONSE_INAPP_ITEM_LIST);
            ArrayList<String> purchaseDataList =
                    ownedItems.getStringArrayList(Utils.RESPONSE_INAPP_PURCHASE_DATA_LIST);
            ArrayList<String> signatureList =
                    ownedItems.getStringArrayList(Utils.RESPONSE_INAPP_SIGNATURE_LIST);
            ArrayList<String> idsList =
                    ownedItems.getStringArrayList(Utils.RESPONSE_INAPP_PURCHASE_ID_LIST);

            for (int i = 0; i < purchaseDataList.size(); ++i) {
                String purchaseData = purchaseDataList.get(i);
                String signature = signatureList.get(i);
                String sku = ownedSkus.get(i);
                String id = idsList.get(i);
                if (Security.verifyPurchase(Utils.mSignatureBase64, purchaseData, signature)) {
                    logDebug("Sku is owned: " + sku);
                    Log.d("purchaseData", purchaseData);
                    Log.d("siganture", signature);
                    Purchase purchase = null;
                    try {
                        purchase = new Purchase(id, skuType, purchaseData, signature);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (TextUtils.isEmpty(purchase.getToken())) {
                        logWarn("BUG: empty/null token!");
                        logDebug("Purchase data: " + purchaseData);
                    }

                    // Record ownership and token
                    inv.addPurchase(purchase);
                } else {
                    logWarn("Purchase signature verification **FAILED**. Not adding item.");
                    logDebug("   Purchase data: " + purchaseData);
                    logDebug("   Signature: " + signature);
                    Log.d("Key", Utils.mSignatureBase64);
                    verificationFailed = true;
                }
            }

            continueToken = ownedItems.getString(Utils.INAPP_CONTINUATION_TOKEN);
            logDebug("Continuation token: " + continueToken);
        } while (!TextUtils.isEmpty(continueToken));

        purchasesResult.setResponseCode(verificationFailed ? Utils.IABHELPER_VERIFICATION_FAILED : Utils.BILLING_RESPONSE_RESULT_OK);
        return purchasesResult;
    }

    public static String getResponseDesc(int code) {
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

    // Workaround to bug where sometimes response codes come as Long instead of Integer
    int getResponseCodeFromBundle(Bundle b) {
        Object o = b.get(Utils.RESPONSE_CODE);
        if (o == null) {
            logDebug("Bundle with null response code, assuming OK (known issue)");
            return Utils.BILLING_RESPONSE_RESULT_OK;
        } else if (o instanceof Integer) {
            return ((Integer) o).intValue();
        } else if (o instanceof Long) {
            return (int) ((Long) o).longValue();
        } else {
            logError("Unexpected type for bundle response code.");
            logError(o.getClass()
                    .getName());
            throw new RuntimeException("Unexpected type for bundle response code: " + o.getClass()
                    .getName());
        }
    }

   /* void consume(Purchase itemInfo) throws IabException {
        checkNotDisposed();
        checkSetupDone("consume");

        if (!itemInfo.mItemType.equals(ITEM_TYPE_INAPP)) {
            throw new IabException(IABHELPER_INVALID_CONSUMPTION,
                    "Items of type '" + itemInfo.mItemType + "' can't be consumed.");
        }
        */

    void logDebug(String msg) {
        if (mDebugLog) Log.d(mDebugTag, msg);
    }

    void logError(String msg) {
        Log.e(mDebugTag, "In-app billing error: " + msg);
    }

    void logWarn(String msg) {
        Log.w(mDebugTag, "In-app billing warning: " + msg);
    }

}


