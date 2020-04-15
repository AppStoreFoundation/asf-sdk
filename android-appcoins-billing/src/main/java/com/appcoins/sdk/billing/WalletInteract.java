package com.appcoins.sdk.billing;

import com.appcoins.sdk.billing.service.wallet.WalletRepository;
import java.util.Random;

public class WalletInteract {

  private static int ID_LENGTH = 40;
  private WalletRepository walletRepository;
  private SharedPreferencesRepository sharedPreferencesRepository;

  public WalletInteract(SharedPreferencesRepository sharedPreferencesRepository,
      WalletRepository walletRepository) {
    this.sharedPreferencesRepository = sharedPreferencesRepository;
    this.walletRepository = walletRepository;
  }

  public String retrieveWalletId() {
    String savedId = sharedPreferencesRepository.getWalletId();
    if (savedId != null) {
      return savedId;
    } else {
      String generatedId = generateId();
      sharedPreferencesRepository.setWalletId(generatedId);
      return generatedId;
    }
  }

  public void requestWallet(String id, WalletInteractListener walletInteractListener) {
    walletRepository.requestWallet(id, walletInteractListener);
  }

  private String generateId() {
    Random r = new Random();
    StringBuilder sb = new StringBuilder();
    while (sb.length() < ID_LENGTH) {
      sb.append(Integer.toHexString(r.nextInt()));
    }
    sb.setLength(ID_LENGTH);
    return sb.toString();
  }

  public void cancelRequests() {
    walletRepository.cancelRequests();
  }
}
