package com.appcoins.sdk.billing.listeners;

public interface AddressRetrievedListener {

  void onAddressRetrieved(String oemAddress, String storeAddress, String developerAddress);
}
