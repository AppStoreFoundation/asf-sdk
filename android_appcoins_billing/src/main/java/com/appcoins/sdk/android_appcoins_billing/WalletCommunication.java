package com.appcoins.sdk.android_appcoins_billing;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.util.Log;

import com.appcoins.sdk.billing.AppcoinsBilling;

import java.util.List;

public class WalletCommunication implements ServiceConnection {

    private WalletBillingService mService;

    private Context mContext;

    public WalletCommunication(Context ctx){
        this.mContext = ctx;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d("CONNECTION","Connected");
        mService = new WalletBillingService(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d("CONNECTION","Disconnected");
    }

    @Override
    public void onBindingDied(ComponentName name) {
        Log.d("CONNECTION","Died");
    }

    private void checkBillingV3(WalletBillingService service , String packageName){
        //int response = mService.isBillingSupported(Utils.API_VERSION, packageName, Utils.ITEM_TYPE_INAPP);

    }

    public void startService(){
        Intent serviceIntent = new Intent(Utils.IAB_BIND_ACTION);
        serviceIntent.setPackage(Utils.IAB_BIND_PACKAGE);
        List<ResolveInfo> intentServices = mContext.getPackageManager().queryIntentServices(serviceIntent, 0);
        if (intentServices != null && !intentServices.isEmpty()) {
            mContext.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
        }else{
            Log.d("CONNECTION","Erro");
        }
    }

    public AppcoinsBilling getService(){
        return (AppcoinsBilling) mService;
    }
}
