package com.appcoins.sdk.android_appcoins_billing;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class WalletCommunication implements ServiceConnection {

    private CatappultAppCoinsBillingService mService;

    public WalletCommunication(){

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public void onBindingDied(ComponentName name) {

    }
}
