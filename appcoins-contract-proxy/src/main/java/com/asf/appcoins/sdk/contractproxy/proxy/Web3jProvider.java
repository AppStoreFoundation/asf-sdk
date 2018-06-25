package com.asf.appcoins.sdk.contractproxy.proxy;

import org.web3j.protocol.Web3j;

public interface Web3jProvider {
  Web3j get(int chainId);
}
