package com.asf.appcoins.sdk.core.microraidenj;

import com.asf.microraidenj.eth.GasLimit;
import com.asf.microraidenj.exception.EstimateGasException;
import com.asf.microraidenj.type.Address;
import java.io.IOException;
import java.math.BigInteger;
import org.spongycastle.util.encoders.Hex;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;

public class GasLimitImpl implements GasLimit {

  private final Web3j web3j;

  public GasLimitImpl(Web3j web3j) {
    this.web3j = web3j;
  }

  @Override public BigInteger estimate(Address from, Address to, byte[] data)
      throws EstimateGasException {

    Transaction transaction =
        Transaction.createEthCallTransaction(from.toHexString(true), to.toHexString(true),
            Hex.toHexString(data));

    try {
      return web3j.ethEstimateGas(transaction)
          .send()
          .getAmountUsed();
    } catch (IOException e) {
      throw new EstimateGasException("Failed to estimate gas!", e);
    }
  }
}
