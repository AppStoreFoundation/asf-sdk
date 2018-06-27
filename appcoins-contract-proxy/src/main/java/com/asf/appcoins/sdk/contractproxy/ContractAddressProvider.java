package com.asf.appcoins.sdk.contractproxy;

import com.asf.appcoins.sdk.contractproxy.proxy.Web3jProxyContract;
import io.reactivex.Single;
import java.util.Map;

public class ContractAddressProvider implements AppCoinsAddressProxySdk {
  private int NETWORK_ROPSTEN = 3;

  static final String APPCOINS_CONTRACT_ID = "appcoins";
  static final String IAB_CONTRACT_ID = "appcoinsiab";
  static final String ADVERTISEMENT_CONTRACT_ID = "advertisement";

  private final Single<String> walletAddress;
  private final Web3jProxyContract web3jProxyContract;
  private final Map<String, String> cache;

  public ContractAddressProvider(Single<String> walletAddress,
      Web3jProxyContract web3jProxyContract, Map<String, String> cache) {
    this.walletAddress = walletAddress;
    this.web3jProxyContract = web3jProxyContract;
    this.cache = cache;
  }

  @Override
  public Single<String> getAppCoinsAddress(int chainId) {
    return getAddress(chainId, APPCOINS_CONTRACT_ID);
  }

  @Override
  public Single<String> getIabAddress(int chainId) {
    return getAddress(chainId, IAB_CONTRACT_ID);
  }

  @Override
  public Single<String> getAdsAddress(int chainId) {
    return getAddress(chainId, ADVERTISEMENT_CONTRACT_ID);
  }

  private Single<String> getAddress(int chainId, String contractId) {
    return walletAddress.map(walletAddress -> syncGetContractAddress(chainId, walletAddress, contractId));
  }

  private synchronized String syncGetContractAddress(int chainId, String wallet,
      String contractId) {
    String key = contractId + chainId;
    if (cache.get(key) == null) {
      cache.put(key, web3jProxyContract.getContractAddressById(wallet, chainId, contractId));
    }
    return cache.get(key);
  }
}
