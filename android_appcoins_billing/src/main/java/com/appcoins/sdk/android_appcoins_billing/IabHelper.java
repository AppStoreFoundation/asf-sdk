    package com.appcoins.sdk.android_appcoins_billing;

    import android.content.ComponentName;
    import android.content.Context;
    import android.content.Intent;
    import android.content.ServiceConnection;
    import android.content.pm.ResolveInfo;
    import android.os.IBinder;
    import android.os.RemoteException;
    import android.util.Log;

    import com.appcoins.sdk.billing.AppcoinsBilling;

    import java.util.List;

    public class IabHelper implements ServiceConnection {

        private WalletBillingService mService;

        private Context mContext;

        private OnIabSetupFinishedListener listener;

        public IabHelper(Context ctx){
            this.mContext = ctx;
        }

        private boolean mSubscriptionsSupported;

        private boolean mSubscriptionUpdateSupported;


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
    }
