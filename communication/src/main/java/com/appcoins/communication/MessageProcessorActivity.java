package com.appcoins.communication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.WindowManager;

public abstract class MessageProcessorActivity extends Activity {

  public static final String MESSAGE_ID = "MESSAGE_ID";
  public static final String METHOD_ID = "METHOD_ID";
  public static final String REQUESTER_PACKAGE_NAME = "REQUESTER_PACKAGE_NAME";
  public static final String ARGUMENTS = "ARGUMENTS";
  public static final String REQUESTER_ACTIVITY_URI = "REQUESTER_ACTIVITY_URI";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    String requesterUri = getIntent().getStringExtra(REQUESTER_ACTIVITY_URI);
    final ProcessedValueReturner processedValueReturner =
        new ProcessedValueReturner(this, requesterUri);

    new Thread(new Runnable() {
      @Override public void run() {
        Intent intent = getIntent();
        long requestCode = intent.getLongExtra(MESSAGE_ID, -1);
        int methodId = intent.getIntExtra(METHOD_ID, -1);
        String packageName = intent.getStringExtra(REQUESTER_PACKAGE_NAME);
        Parcelable arguments = intent.getParcelableExtra(ARGUMENTS);
        Parcelable returnValue = processValue(methodId, arguments);
        processedValueReturner.returnValue(packageName, requestCode, returnValue);
        finish();
      }
    }).start();
  }

  /**
   * This method will be called in to process data to be returned
   * <dl>
   *  <dt><b>Thread:</b></dt>
   *  <dd>{@code processValue} runs on a worker thread.</dd>
   * </dl>
   *
   * @param methodId method id sent by requester
   * @param arguments arguments send by requester
   *
   * @return data that should be returned to requester
   */
  public abstract Parcelable processValue(int methodId, Parcelable arguments);
}

