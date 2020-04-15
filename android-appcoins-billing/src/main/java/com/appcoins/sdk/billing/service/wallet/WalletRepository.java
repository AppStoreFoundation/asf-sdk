package com.appcoins.sdk.billing.service.wallet;

import com.appcoins.sdk.billing.WalletInteractListener;
import com.appcoins.sdk.billing.analytics.WalletAddressProvider;
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
  private WalletAddressProvider walletAddressProvider;

  public WalletRepository(Service service, WalletGenerationMapper walletGenerationMapper,
      WalletAddressProvider walletAddressProvider) {

    this.service = service;
    this.walletGenerationMapper = walletGenerationMapper;
    this.walletAddressProvider = walletAddressProvider;
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
        saveWalletAddress(walletGenerationModel);
        walletInteractListener.walletAddressRetrieved(walletGenerationModel);
      }
    };
    service.makeRequest("/appc/guest_wallet", "GET", new ArrayList<String>(), queries, null, null,
        serviceResponseListener);
  }

  public WalletGenerationModel requestWalletSync(String id) {
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    final WalletGenerationModel[] walletGenerationModel =
        { WalletGenerationModel.createErrorWalletGenerationModel() };

    Map<String, String> queries = new HashMap<>();
    queries.put("id", id);

    ServiceResponseListener serviceResponseListener = new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        WalletGenerationResponse walletGenerationResponse =
            walletGenerationMapper.mapWalletGenerationResponse(requestResponse);
        walletGenerationModel[0] = new WalletGenerationModel(walletGenerationResponse.getAddress(),
            walletGenerationResponse.getSignature(), walletGenerationResponse.hasError());
        saveWalletAddress(walletGenerationModel[0]);
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

  private void saveWalletAddress(WalletGenerationModel walletGenerationModel) {
    if (!walletGenerationModel.hasError()) {
      walletAddressProvider.saveWalletAddress(walletGenerationModel.getWalletAddress());
    }
  }
}
