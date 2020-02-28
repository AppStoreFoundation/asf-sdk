package com.appcoins.sdk.billing.payasguest;

import com.appcoins.sdk.billing.service.RequestResponse;
import com.appcoins.sdk.billing.service.Service;
import com.appcoins.sdk.billing.service.ServiceResponseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeveloperAddressService {

  private Service service;

  public DeveloperAddressService(Service service) {
    this.service = service;
  }

  void getDeveloperAddressForPackage(String packageName,
      final AdyenPaymentInteract.AddressListener addressListener) {
    Map<String, String> queries = new HashMap<>();
    queries.put("package.name", packageName);
    ServiceResponseListener serviceResponseListener = new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        AddressResponseMapper addressResponseMapper = new AddressResponseMapper();
        AddressModel addressModel = addressResponseMapper.mapDeveloper(requestResponse);
        addressListener.onResponse(addressModel);
      }
    };
    service.makeRequest("/7/bds/apks/package/getOwnerWallet", "GET", new ArrayList<String>(),
        queries, null, new HashMap<String, Object>(), serviceResponseListener);
  }
}
