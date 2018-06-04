package com.asf.appcoins.sdk.core.microraidenj;

import com.asf.appcoins.sdk.core.web3.AsfWeb3j;
import com.asf.microraidenj.eth.interfaces.GasLimit;
import com.asf.microraidenj.eth.interfaces.GasPrice;
import com.asf.microraidenj.eth.interfaces.GetNonce;
import com.asf.microraidenj.eth.interfaces.TransactionSender;
import com.asf.microraidenj.exception.EstimateGasException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import ethereumj.Transaction;
import ethereumj.core.CallTransaction;
import ethereumj.crypto.ECKey;
import java.math.BigInteger;
import org.spongycastle.util.encoders.Hex;

public class TransactionSenderImpl implements TransactionSender {

  private final AsfWeb3j asfWeb3j;
  private final GasPrice gasPrice;
  private final GetNonce getNonce;
  private final GasLimit gasLimit;

  public TransactionSenderImpl(AsfWeb3j asfWeb3j, GasPrice gasPrice, GetNonce getNonce,
      GasLimit gasLimit) {
    this.asfWeb3j = asfWeb3j;
    this.gasPrice = gasPrice;
    this.getNonce = getNonce;
    this.gasLimit = gasLimit;
  }

  @Override
  public String send(ECKey senderECKey, Address receiveAddress, BigInteger value, byte[] data)
      throws TransactionFailedException {

    BigInteger nonce = getNonce.get(Address.from(senderECKey.getAddress()));

    Transaction transaction;
    try {
      transaction = CallTransaction.createRawTransaction(nonce.longValue(), gasPrice.get()
              .longValue(),
          gasLimit.estimate(Address.from(senderECKey.getAddress()), receiveAddress, data)
              .longValue(), receiveAddress.get(), value.longValue(), data);
    } catch (EstimateGasException e) {
      throw new TransactionFailedException(e);
    }

    transaction.sign(senderECKey);

    return asfWeb3j.sendRawTransaction("0x" + Hex.toHexString(transaction.getEncoded()))
        .blockingFirst();
  }
}
