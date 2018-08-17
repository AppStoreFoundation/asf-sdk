package com.asf.appcoins.sdk.contractproxy;

import com.asf.appcoins.sdk.contractproxy.repository.RemoteRepository;
import io.reactivex.Single;

public class BdsAppCoinsAddressProxySdk implements AppCoinsAddressProxySdk {
  private final RemoteRepository repository;

  public BdsAppCoinsAddressProxySdk(RemoteRepository repository) {
    this.repository = repository;
  }

  @Override public Single<String> getAppCoinsAddress(int chainId) {
    return repository.getAddress(RemoteRepository.Contracts.APPCOINS, chainId);
  }

  @Override public Single<String> getIabAddress(int chainId) {
    return repository.getAddress(RemoteRepository.Contracts.APPCOINS_IAB, chainId);
  }

  @Override public Single<String> getAdsAddress(int chainId) {
    return repository.getAddress(RemoteRepository.Contracts.APPCOINS_ADS, chainId);
  }
}
