package com.appcoins.sdk.billing.service.wallet;

import com.appcoins.sdk.billing.WalletInteractListener;
import com.appcoins.sdk.billing.models.payasguest.WalletGenerationModel;
import com.appcoins.sdk.billing.service.BdsService;
import com.appcoins.sdk.billing.service.RequestResponse;
import com.appcoins.sdk.billing.service.Service;
import com.appcoins.sdk.billing.service.ServiceResponseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WalletRepository {

  private final Service service;
  private WalletGenerationMapper walletGenerationMapper;

  public WalletRepository(Service service, WalletGenerationMapper walletGenerationMapper) {

    this.service = service;
    this.walletGenerationMapper = walletGenerationMapper;
  }

  public void requestWallet(String id, final WalletInteractListener walletInteractListener) {
    Map<String, String> queries = new HashMap<>();
    queries.put("id", id);
    ServiceResponseListener serviceResponseListener = new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        WalletGenerationResponse walletGenerationResponse =
            walletGenerationMapper.mapWalletGenerationResponse(requestResponse);
        WalletGenerationModel walletGenerationModel =
            new WalletGenerationModel(walletGenerationResponse.getAddress(),
                walletGenerationResponse.getSignature(), walletGenerationResponse.hasError());
        walletInteractListener.walletAddressRetrieved(walletGenerationModel);
      }
    };
    service.makeRequest("/appc/guest_wallet", "GET", new ArrayList<String>(), queries, null, null,
        serviceResponseListener);
  }

  public WalletGenerationModel requestWalletSync(String id) {
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    final WalletGenerationModel[] walletGenerationModel = { new WalletGenerationModel() };

    Map<String, String> queries = new HashMap<>();
    queries.put("id", id);

    ServiceResponseListener serviceResponseListener = new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        WalletGenerationResponse walletGenerationResponse =
            walletGenerationMapper.mapWalletGenerationResponse(requestResponse);
        walletGenerationModel[0] = new WalletGenerationModel(walletGenerationResponse.getAddress(),
            walletGenerationResponse.getSignature(), walletGenerationResponse.hasError());
        countDownLatch.countDown();
      }
    };
    service.makeRequest("/appc/guest_wallet", "GET", new ArrayList<String>(), queries, null, null,
        serviceResponseListener);

    try {
      countDownLatch.await(BdsService.TIME_OUT_IN_MILLIS, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return walletGenerationModel[0];
  }

  public void cancelRequests() {
    service.cancelRequests();
  }
}
