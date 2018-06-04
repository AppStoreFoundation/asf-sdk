package com.asf.appcoins.sdk.core.microraidenj;

import com.asf.appcoins.sdk.core.web3.AsfWeb3j;
import com.asf.microraidenj.eth.interfaces.GetNonce;
import com.asf.microraidenj.type.Address;

public class GetNonceImpl implements GetNonce {

  private final AsfWeb3j asfWeb3j;

  private Long nonce;

  public GetNonceImpl(AsfWeb3j asfWeb3j) {
    this.asfWeb3j = asfWeb3j;
  }

  @Override public long get(Address address) {
    if (nonce == null) {
      computeNonce(address.get());
    }

    return nonce++;
  }

  private void computeNonce(String hexValue) {
    nonce = asfWeb3j.getNonce(new org.web3j.abi.datatypes.Address(hexValue))
        .blockingFirst();
  }
}
