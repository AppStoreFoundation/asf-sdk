package com.asf.appcoins.sdk.contractproxy;

import io.reactivex.Single;

/**
 * AppCoins Address proxy SDK interface.
 */
public interface AppCoinsAddressProxySdk {

  Single<String> getAppCoinsAddress(int chainId);

  Single<String> getIabAddress(int chainId);

  Single<String> getAdsAddress(int chainId);
}