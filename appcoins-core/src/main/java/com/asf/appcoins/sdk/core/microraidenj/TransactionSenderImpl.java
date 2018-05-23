package com.asf.appcoins.sdk.core.microraidenj;

import com.asf.appcoins.sdk.core.web3.AsfWeb3j;
import com.asf.microraidenj.eth.interfaces.TransactionSender;
import ethereumj.Transaction;
import ethereumj.core.CallTransaction;
import ethereumj.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.datatypes.Address;

public class TransactionSenderImpl implements TransactionSender {

  private final AsfWeb3j asfWeb3j;
  private final int gasPrice;
  private final int gasLimit;

  private Long nonce;

  public TransactionSenderImpl(AsfWeb3j asfWeb3j, int gasPrice, int gasLimit) {
    this.asfWeb3j = asfWeb3j;
    this.gasPrice = gasPrice;
    this.gasLimit = gasLimit;
  }

  @Override public String send(ECKey senderECKey, com.asf.microraidenj.type.Address receiveAddress,
      long value, byte[] data) {

    if (nonce == null) {
      computeNonce();
    }

    Transaction transaction =
        CallTransaction.createRawTransaction(nonce++, gasPrice, gasLimit, receiveAddress.get(),
            value, data);

    transaction.sign(senderECKey);

    return asfWeb3j.sendRawTransaction("0x" + Hex.toHexString(transaction.getEncoded()))
        .blockingFirst();
  }

  private void computeNonce() {
    nonce = asfWeb3j.getNonce(new Address("0x82c8af156413d7c51af09590749EfcBC508ecc5e"))
        .blockingFirst();
  }
}
