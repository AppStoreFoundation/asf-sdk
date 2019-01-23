package com.appcoins.sdk.android_appcoins_billing;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.appcoins.sdk.billing.Inventory;
import com.appcoins.sdk.billing.SkuDetails;
import com.appcoins.sdk.billing.SkuDetailsParam;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

    class IabHelper implements ServiceConnection {

        private final Object mAsyncInProgressLock = new Object();
        boolean mAsyncInProgress = false;
        String mAsyncOperation = "";
        boolean mSetupDone = false;
        boolean mDisposeAfterAsync = false;
        boolean mDisposed = false;
        private WalletBillingService mService;
        private Context mContext;
        private OnIabSetupFinishedListener listener;
        private boolean mSubscriptionsSupported;
        private boolean mSubscriptionUpdateSupported;
        private int mRequestCode;
        private OnIabPurchaseFinishedListener mPurchaseListener;
        private String mPurchasingItemType;
      private WalletBillingService mService;

    private Context mContext;

    private OnIabSetupFinishedListener listener;

          private boolean mSubscriptionsSupported;

    private boolean mSubscriptionUpdateSupported;

    boolean mDebugLog = true;

    String mDebugTag = "IabHelper";

    PurchasesResult purchasesResult;

    Inventory inventory;

    public IabHelper(Context ctx, String base64PublicKey){
        this.mContext = ctx;
        Utils.mSignatureBase64 = base64PublicKey;
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = new WalletBillingService(service);
        try {
            checkBillingVersionV3INAPP(mService, mContext.getPackageName(), Utils.API_VERSION_V3 , Utils.ITEM_TYPE_INAPP);
            checkBillingVersionV5SUBS(mService, mContext.getPackageName() , Utils.API_VERSION_V5 , Utils.ITEM_TYPE_SUBS);
            checkBillingVersionV3SUBS(mService, mContext.getPackageName(), Utils.API_VERSION_V3 , Utils.ITEM_TYPE_SUBS);
        } catch (RemoteException e) {
            if (listener != null) {

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

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new WalletBillingService(service);
            try {
                checkBillingVersionV3INAPP(mService, mContext.getPackageName(), Utils.API_VERSION_V3 , Utils.ITEM_TYPE_INAPP);
                checkBillingVersionV5SUBS(mService, mContext.getPackageName() , Utils.API_VERSION_V5 , Utils.ITEM_TYPE_SUBS);
                checkBillingVersionV3SUBS(mService, mContext.getPackageName(), Utils.API_VERSION_V3 , Utils.ITEM_TYPE_SUBS);
                mSetupDone = true;
            } catch (RemoteException e) {
                if (listener != null) {
                    listener.onIabSetupFinished(new IabResult(Utils.IABHELPER_REMOTE_EXCEPTION, "RemoteException while setting up in-app billing."));
                }

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
        int response = service.isBillingSupported(apiVersion, packageName, type);=======


  
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
                    Log.d("Size map inserir",inv.getAllPurchases().size() + "");
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

        purchasesResult.setPurchases(inv.getAllPurchases());

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

    public void consume(Purchase itemInfo) throws IabException {
        checkNotDisposed();
        //checkSetupDone("consume");

        if (!itemInfo.getItemType().equals(Utils.ITEM_TYPE_INAPP)) {
            throw new IabException(Utils.IABHELPER_INVALID_CONSUMPTION,
                    "Items of type '" + itemInfo.getItemType() + "' can't be consumed.");
        }

        try {
            String token = itemInfo.getToken();
            String sku = itemInfo.getSku();
            if (token == null || token.equals("")) {
                logError("Can't consume " + sku + ". No token.");
                throw new IabException(Utils.IABHELPER_MISSING_TOKEN,
                        "PurchaseInfo is missing token for sku: " + sku + " " + itemInfo);
            }

        public void startService(OnIabSetupFinishedListener listener){

            //TODO Change to BuildConfig
            Intent serviceIntent = new Intent(Utils.IAB_BIND_ACTION);
            serviceIntent.setPackage(Utils.IAB_BIND_PACKAGE);

            this.listener = listener;


            logDebug("Consuming sku: " + sku + ", token: " + token);
            int response = mService.consumePurchase(3, mContext.getPackageName(), token);
            if (response == Utils.BILLING_RESPONSE_RESULT_OK) {
                logDebug("Successfully consumed sku: " + sku);
            } else {
                logDebug("Error consuming consuming sku " + sku + ". " + getResponseDesc(response));
                throw new IabException(response, "Error consuming sku " + sku);
            }
        } catch (RemoteException e) {
            throw new IabException(Utils.IABHELPER_REMOTE_EXCEPTION,
                    "Remote exception while consuming. PurchaseInfo: " + itemInfo, e);
        }

        public void querySkuDetailsAsync(final SkuDetailsParam skuDetailsParam , final OnSkuDetailsResponseListener onSkuDetailsResponseListener) throws IabAsyncInProgressException {
            final Handler handler = new Handler();
            checkSetupDone("queryInventory");
            flagStartAsync("refresh inventory");

            (new Thread(new Runnable() {

                @Override
                public void run() {
                    IabResult result =
                            new IabResult(Utils.BILLING_RESPONSE_RESULT_OK, "Inventory refresh successful.");
                    int response = 0;
                    Inventory inv = new Inventory();
                    try {

                     response = querySkuDetails(skuDetailsParam.getItemType(), inv, skuDetailsParam.getMoreItemSkus());

                    } catch (RemoteException e) {
                        result = new IabResult(Utils.IABHELPER_REMOTE_EXCEPTION, "Remote exception while refreshing inventory.");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (response != Utils.BILLING_RESPONSE_RESULT_OK) {
                        result = new IabResult(Utils.BILLING_RESPONSE_RESULT_OK, "Error refreshing inventory (querying prices of items).");
                    }

                    if (mSubscriptionsSupported) {
                        try {
                            response = querySkuDetails(Utils.ITEM_TYPE_SUBS, inv, skuDetailsParam.getMoreSubsSkus());
                        } catch (RemoteException e) {
                            result = new IabResult(Utils.IABHELPER_REMOTE_EXCEPTION, "Remote exception while refreshing inventory.");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (response != Utils.BILLING_RESPONSE_RESULT_OK) {
                            result = new IabResult(response, "Error refreshing inventory (querying prices of subscriptions).");
                        }
                    }

                    flagEndAsync();

                    final int result_f = response;
                    final List<SkuDetails> inv_f = inv.getAllSkuDetails();


                    if (!mDisposed && listener != null) {
                        handler.post(new Runnable() {
                            public void run() {
                                onSkuDetailsResponseListener.onSkuDetailsResponseListener(result_f, inv_f);
                            }
                        });
                    }
                }
            })).start();
        }

        void checkSetupDone(String operation) {
            if (!mSetupDone) {
                Log.e("Error:","Illegal state for operation (" + operation + "): IAB helper is not set up.");
                throw new IllegalStateException(
                        "IAB helper is not set up. Can't perform operation: " + operation);
            }
        }

        private void flagStartAsync(String operation) throws IabAsyncInProgressException {
            synchronized (mAsyncInProgressLock) {
                if (mAsyncInProgress) {
                    throw new IabAsyncInProgressException("Can't start async operation ("
                            + operation
                            + ") because another async operation ("
                            + mAsyncOperation
                            + ") is in progress.");
                }
                mAsyncOperation = operation;
                mAsyncInProgress = true;
                Log.d("Message", "Starting async operation: " + operation);
            }
        }

        void flagEndAsync() {
            synchronized (mAsyncInProgressLock) {
                Log.d("Message","Ending async operation: " + mAsyncOperation);
                mAsyncOperation = "";
                mAsyncInProgress = false;
                if (mDisposeAfterAsync) {
                    try {
                        dispose();
                    } catch (IabAsyncInProgressException e) {
                        // Should not be thrown, because we reset mAsyncInProgress immediately before
                        // calling dispose().
                    }
                }
            }
        }

        public int querySkuDetails(String itemType, Inventory inv, List<String> moreSkus)
                throws RemoteException,JSONException {

            Log.d("Message","Querying SKU details.");
            ArrayList<String> skuList = new ArrayList<String>();
            skuList.addAll(inv.getAllOwnedSkus(itemType));
            if (moreSkus != null) {
                for (String sku : moreSkus) {
                    if (!skuList.contains(sku)) {
                        skuList.add(sku);
                    }
                }
            }

            if (skuList.size() == 0) {
                Log.d("Message","queryPrices: nothing to do because there are no SKUs.");
                return Utils.BILLING_RESPONSE_RESULT_OK;
            }

            // Split the sku list in blocks of no more than 20 elements.
            ArrayList<ArrayList<String>> packs = new ArrayList<ArrayList<String>>();
            ArrayList<String> tempList;
            int n = skuList.size() / 20;
            int mod = skuList.size() % 20;
            for (int i = 0; i < n; i++) {
                tempList = new ArrayList<String>();
                for (String s : skuList.subList(i * 20, i * 20 + 20)) {
                    tempList.add(s);
                }
                packs.add(tempList);
            }
            if (mod != 0) {
                tempList = new ArrayList<String>();
                for (String s : skuList.subList(n * 20, n * 20 + mod)) {
                    tempList.add(s);
                }
                packs.add(tempList);
            }

            for (ArrayList<String> skuPartList : packs) {
                Bundle querySkus = new Bundle();
                querySkus.putStringArrayList(Utils.GET_SKU_DETAILS_ITEM_LIST, skuPartList);
                Bundle skuDetails = mService.getSkuDetails(3, mContext.getPackageName(), itemType, querySkus);

                if (!skuDetails.containsKey(Utils.RESPONSE_GET_SKU_DETAILS_LIST)) {
                    int response = getResponseCodeFromBundle(skuDetails);
                    if (response != Utils.BILLING_RESPONSE_RESULT_OK) {
                        Log.d("Message","getSkuDetails() failed: " + getResponseDesc(response));
                        return response;
                    } else {
                        Log.e("Message","getSkuDetails() returned a bundle with neither an error nor a detail list.");
                        return Utils.IABHELPER_BAD_RESPONSE;
                    }
                }

                ArrayList<String> responseList = skuDetails.getStringArrayList(Utils.RESPONSE_GET_SKU_DETAILS_LIST);

                for (String thisResponse : responseList) {
                    SkuDetails d = null;
                    try {
                        d = new SkuDetails(itemType, thisResponse);
                    } catch (Exception e) {
                        throw new JSONException(e.getCause());
                    }
                    Log.d("Message","Got sku details: " + d);
                    inv.addSkuDetails(d);
                }
            }

            return Utils.BILLING_RESPONSE_RESULT_OK;
        }

        private int getResponseCodeFromBundle(Bundle b) {
            Object o = b.get(Utils.RESPONSE_CODE);
            if (o == null) {
                Log.d("Message","Bundle with null response code, assuming OK (known issue)");
                return Utils.BILLING_RESPONSE_RESULT_OK;
            } else if (o instanceof Integer) {
                return ((Integer) o).intValue();
            } else if (o instanceof Long) {
                return (int) ((Long) o).longValue();
            } else {
                Log.e("Message","Unexpected type for bundle response code.");
                Log.e("Message",o.getClass()
                        .getName());
                throw new RuntimeException("Unexpected type for bundle response code: " + o.getClass()
                        .getName());
            }
        }

        public void dispose() throws IabAsyncInProgressException {
            synchronized (mAsyncInProgressLock) {
                if (mAsyncInProgress) {
                    throw new IabAsyncInProgressException("Can't dispose because an async operation "
                            + "("
                            + mAsyncOperation
                            + ") is in progress.");
                }
            }
            Log.d("Message","Disposing.");

            if (mSetupDone) {
                Log.d("Message","Unbinding from service.");
                if (mContext != null) mContext.unbindService(this);
            }
            mSetupDone = false;
            mDisposed = true;
            mContext = null;
            mService = null;
        }

        public void launchPurchaseFlow(Activity act ,String sku, String itemType, List<String> oldSkus,
                                       int requestCode, OnIabPurchaseFinishedListener listener, String extraData)
                throws IabAsyncInProgressException {
            checkNotDisposed();
            checkSetupDone("launchPurchaseFlow");
            flagStartAsync("launchPurchaseFlow");
            IabResult result;

            if (itemType.equals(Utils.ITEM_TYPE_SUBS) && !mSubscriptionsSupported) {
                IabResult r =
                        new IabResult(Utils.IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE, "Subscriptions are not available.");
                flagEndAsync();
                if (listener != null) listener.onIabPurchaseFinished(r, null);
                return;
            }

            try {
                Log.d("Message","Constructing buy intent for " + sku + ", item type: " + itemType);
                Bundle buyIntentBundle;
                if (oldSkus == null || oldSkus.isEmpty()) {
                    // Purchasing a new item or subscription re-signup
                    buyIntentBundle =
                            mService.getBuyIntent(3, mContext.getPackageName(), sku, itemType, extraData);
                } else {
                    // Subscription upgrade/downgrade
                    if (!mSubscriptionUpdateSupported) {
                        IabResult r = new IabResult(Utils.IABHELPER_SUBSCRIPTION_UPDATE_NOT_AVAILABLE,
                                "Subscription updates are not available.");
                        flagEndAsync();
                        if (listener != null) listener.onIabPurchaseFinished(r, null);
                        return;
                    }
                    buyIntentBundle =
                           mService.getBuyIntentToReplaceSkus(5, mContext.getPackageName(), oldSkus, sku, itemType,
                                    extraData);
                }
                int response = getResponseCodeFromBundle(buyIntentBundle);
                Log.d("response code: ", response+"");
                if (response != Utils.BILLING_RESPONSE_RESULT_OK) {
                    Log.e("Error","Unable to buy item, Error response: " + getResponseDesc(response));
                    flagEndAsync();
                    result = new IabResult(response, "Unable to buy item");
                    if (listener != null) listener.onIabPurchaseFinished(result, null);
                    return;
                }

                PendingIntent pendingIntent = buyIntentBundle.getParcelable(Utils.RESPONSE_BUY_INTENT);
                Log.d("Message","Launching buy intent for " + sku + ". Request code: " + requestCode);
                mRequestCode = requestCode;
                mPurchaseListener = listener;
                mPurchasingItemType = itemType;
                act.startIntentSenderForResult(pendingIntent.getIntentSender(), requestCode, new Intent(),
                        Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));

                flagEndAsync();
            } catch (IntentSender.SendIntentException e) {
                Log.e("Error","SendIntentException while launching purchase flow for sku " + sku);
                e.printStackTrace();
                flagEndAsync();

                result = new IabResult(Utils.IABHELPER_SEND_INTENT_FAILED, "Failed to send intent.");
                if (listener != null) listener.onIabPurchaseFinished(result, null);
            } catch (RemoteException e) {
                Log.e("Error","RemoteException while launching purchase flow for sku " + sku);
                e.printStackTrace();
                flagEndAsync();

                result = new IabResult(Utils.IABHELPER_REMOTE_EXCEPTION,
                        "Remote exception while starting purchase flow");
                if (listener != null) listener.onIabPurchaseFinished(result, null);
            }
        }

        private void checkNotDisposed() {
            if (mDisposed) {
                throw new IllegalStateException("IabHelper was disposed of, so it cannot be used.");
            }
        }

    }

    private void checkNotDisposed() {
        if (Utils.mDisposed) {
            throw new IllegalStateException("IabHelper was disposed of, so it cannot be used.");
        }
    }

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



