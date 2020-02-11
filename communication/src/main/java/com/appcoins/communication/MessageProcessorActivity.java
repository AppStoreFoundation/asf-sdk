package com.appcoins.communication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

public abstract class MessageProcessorActivity extends Activity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final ProcessedValueReturner processedValueReturner =
        new ProcessedValueReturner(this, getRequesterUri());

    new Thread(new Runnable() {
      @Override public void run() {
        Intent intent = getIntent();
        long requestCode = intent.getLongExtra("MESSAGE_ID", -1);
        int methodId = intent.getIntExtra("TYPE", -1);
        String packageName = intent.getStringExtra("REQUESTER_PACKAGE_NAME");
        Parcelable arguments = intent.getParcelableExtra("ARGUMENTS");
        Parcelable returnValue = processValue(methodId, arguments);
        processedValueReturner.returnValue(packageName, requestCode, returnValue);
        finish();
      }
    }).start();
  }

  protected abstract String getRequesterUri();

  public abstract Parcelable processValue(int methodId, Parcelable arguments);
}

