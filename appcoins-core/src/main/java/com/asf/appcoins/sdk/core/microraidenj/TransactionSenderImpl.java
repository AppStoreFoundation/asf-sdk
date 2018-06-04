package com.asf.appcoins.sdk.core.microraidenj;

import com.asf.appcoins.sdk.core.web3.AsfWeb3j;
import com.asf.microraidenj.eth.interfaces.GasPrice;
import com.asf.microraidenj.eth.interfaces.GetNonce;
import com.asf.microraidenj.eth.interfaces.TransactionSender;
import com.asf.microraidenj.type.Address;
import ethereumj.Transaction;
import ethereumj.core.CallTransaction;
import ethereumj.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

public class TransactionSenderImpl implements TransactionSender {

  private final AsfWeb3j asfWeb3j;
  private final GasPrice gasPrice;
  private final GetNonce getNonce;
  private final int gasLimit;

  public TransactionSenderImpl(AsfWeb3j asfWeb3j, GasPrice gasPrice, int gasLimit,
      GetNonce getNonce) {
    this.asfWeb3j = asfWeb3j;
    this.gasPrice = gasPrice;
    this.gasLimit = gasLimit;
    this.getNonce = getNonce;
  }

  @Override public String send(ECKey senderECKey, Address receiveAddress, long value, byte[] data) {

    Transaction transaction = CallTransaction.createRawTransaction(
        getNonce.get(com.asf.microraidenj.type.Address.from(senderECKey.getAddress())),
        gasPrice.get(), gasLimit, receiveAddress.get(), value, data);

    transaction.sign(senderECKey);

    return asfWeb3j.sendRawTransaction("0x" + Hex.toHexString(transaction.getEncoded()))
        .blockingFirst();
  }
}
