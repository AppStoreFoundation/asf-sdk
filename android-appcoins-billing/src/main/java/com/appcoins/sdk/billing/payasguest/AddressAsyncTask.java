package com.appcoins.sdk.billing.payasguest;

import android.os.AsyncTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AddressAsyncTask extends AsyncTask {

  private String oemAddress;
  private String storeAddress;
  private String developerAddress;
  private AddressService addressService;
  private AddressRetrievedListener addressRetrievedListener;
  private String packageName;

  public AddressAsyncTask(AddressService addressService,
      AddressRetrievedListener addressRetrievedListener, String packageName) {

    this.addressService = addressService;
    this.addressRetrievedListener = addressRetrievedListener;
    this.packageName = packageName;
  }

  @Override protected Object doInBackground(Object[] objects) {
    CountDownLatch countDownLatch = new CountDownLatch(3);
    getOemAddress(countDownLatch);
    getStoreAddress(countDownLatch);
    getDeveloperAddress(countDownLatch);
    try {
      countDownLatch.await(30, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    addressRetrievedListener.onAddressRetrieved(oemAddress, storeAddress, developerAddress);
    return null;
  }

  private void getOemAddress(final CountDownLatch countDownLatch) {
    AdyenPaymentInteract.AddressListener addressListener =
        new AdyenPaymentInteract.AddressListener() {
          @Override public void onResponse(AddressModel addressModel) {
            oemAddress = addressModel.getAddress();
            countDownLatch.countDown();
          }
        };
    addressService.getOemAddressForPackage(packageName, addressListener);
  }

  private void getStoreAddress(final CountDownLatch countDownLatch) {
    AdyenPaymentInteract.AddressListener addressListener =
        new AdyenPaymentInteract.AddressListener() {
          @Override public void onResponse(AddressModel addressModel) {
            storeAddress = addressModel.getAddress();
            countDownLatch.countDown();
          }
        };
    addressService.getStoreAddressForPackage(packageName, addressListener);
  }

  private void getDeveloperAddress(final CountDownLatch countDownLatch) {
    AdyenPaymentInteract.AddressListener addressListener =
        new AdyenPaymentInteract.AddressListener() {
          @Override public void onResponse(AddressModel addressModel) {
            developerAddress = addressModel.getAddress();
            countDownLatch.countDown();
          }
        };
    addressService.getDeveloperAddress(packageName, addressListener);
  }
}
