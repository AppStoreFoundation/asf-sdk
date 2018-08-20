package com.asf.appcoins.sdk.contractproxy.repository;

public interface ApiProvider {
  RemoteRepository.Api getApi(int chainId);
}
