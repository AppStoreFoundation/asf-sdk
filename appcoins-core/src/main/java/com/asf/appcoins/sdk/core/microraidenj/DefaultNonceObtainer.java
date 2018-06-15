package com.asf.appcoins.sdk.core.microraidenj;

import com.asf.appcoins.sdk.core.web3.AsfWeb3j;
import com.asf.microraidenj.eth.NonceObtainer;
import com.asf.microraidenj.type.Address;
import java.math.BigInteger;

public class DefaultNonceObtainer implements NonceObtainer {

  private final AsfWeb3j asfWeb3j;

  private BigInteger nonce;

  public DefaultNonceObtainer(AsfWeb3j asfWeb3j) {
    this.asfWeb3j = asfWeb3j;
  }

  @Override public BigInteger getNonce(Address address) {
    if (nonce == null) {
      computeNonce(address.toHexString());
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
