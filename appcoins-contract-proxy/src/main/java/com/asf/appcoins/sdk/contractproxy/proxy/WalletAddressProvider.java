package com.asf.appcoins.sdk.contractproxy.proxy;

import io.reactivex.Single;

public interface WalletAddressProvider {
  Single<String> get();
}
