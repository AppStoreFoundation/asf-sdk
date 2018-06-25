package com.asf.appcoins.sdk.contractproxy.proxy;

public interface ProxyContract {
  String getContractAddressById(String fromAddress, int chainId, String id);
}
