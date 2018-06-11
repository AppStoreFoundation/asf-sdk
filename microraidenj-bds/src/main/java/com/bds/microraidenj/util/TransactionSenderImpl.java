package com.bds.microraidenj.util;

import com.asf.microraidenj.eth.GasLimit;
import com.asf.microraidenj.eth.GasPrice;
import com.asf.microraidenj.eth.GetNonce;
import com.asf.microraidenj.eth.TransactionSender;
import com.asf.microraidenj.exception.EstimateGasException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import ethereumj.Transaction;
import ethereumj.core.CallTransaction;
import ethereumj.crypto.ECKey;
import java.io.IOException;
import java.math.BigInteger;
import org.spongycastle.util.encoders.Hex;
import org.web3j.protocol.Web3j;

public class TransactionSenderImpl implements TransactionSender {

  private final Web3j web3j;
  private final GasPrice gasPrice;
  private final GetNonce getNonce;
  private final GasLimit gasLimit;

  public TransactionSenderImpl(Web3j web3j, GasPrice gasPrice, GetNonce getNonce,
      GasLimit gasLimit) {
    this.web3j = web3j;
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
              .longValue(), receiveAddress.toHexString(), value.longValue(), data);
    } catch (EstimateGasException e) {
      throw new TransactionFailedException(e);
    }

    transaction.sign(senderECKey);

    try {
      return web3j.ethSendRawTransaction("0x" + Hex.toHexString(transaction.getEncoded()))
          .send()
          .getTransactionHash();
    } catch (IOException e) {
      throw new TransactionFailedException(e);
    }
  }
}
