package com.appcoins.sdk.billing.service.wallet;

import com.appcoins.sdk.billing.WalletInteractListener;
import com.appcoins.sdk.billing.models.WalletGenerationModel;
import com.appcoins.sdk.billing.service.RequestResponse;
import com.appcoins.sdk.billing.service.Service;
import com.appcoins.sdk.billing.service.ServiceResponseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                walletGenerationResponse.getEwt(), walletGenerationResponse.getSignature(),
                walletGenerationResponse.hasError());
        walletInteractListener.walletIdRetrieved(walletGenerationModel);
      }
    };
    service.makeRequest("/appc/guest_wallet", "GET", new ArrayList<String>(), queries, null, null,
        serviceResponseListener);
  }
}
