package com.asf.appcoins.sdk.core.microraidenj;

import com.asf.appcoins.sdk.core.web3.AsfWeb3j;
import com.asf.microraidenj.eth.interfaces.GetNonce;
import com.asf.microraidenj.type.Address;
import java.math.BigInteger;

public class GetNonceImpl implements GetNonce {

  private final AsfWeb3j asfWeb3j;

  private BigInteger nonce;

  public GetNonceImpl(AsfWeb3j asfWeb3j) {
    this.asfWeb3j = asfWeb3j;
  }

  @Override public BigInteger get(Address address) {
    if (nonce == null) {
      computeNonce(address.get());
    }

    BigInteger tmp = nonce;

    nonce = nonce.add(BigInteger.valueOf(1));

    return tmp;
  }

  private void computeNonce(String hexValue) {
    nonce = BigInteger.valueOf(asfWeb3j.getNonce(new org.web3j.abi.datatypes.Address(hexValue))
        .blockingFirst());
  }
}
