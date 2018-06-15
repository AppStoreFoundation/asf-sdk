package com.bds.microraidenj.util;

import com.asf.microraidenj.eth.GasLimitEstimator;
import com.asf.microraidenj.eth.GasPriceObtainer;
import com.asf.microraidenj.eth.NonceObtainer;
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

public class DefaultTransactionSender implements TransactionSender {

  private final Web3j web3j;
  private final GasPriceObtainer gasPriceObtainer;
  private final NonceObtainer nonceObtainer;
  private final GasLimitEstimator gasLimitEstimator;

  public DefaultTransactionSender(Web3j web3j, GasPriceObtainer gasPriceObtainer,
      NonceObtainer nonceObtainer, GasLimitEstimator gasLimitEstimator) {
    this.web3j = web3j;
    this.gasPriceObtainer = gasPriceObtainer;
    this.nonceObtainer = nonceObtainer;
    this.gasLimitEstimator = gasLimitEstimator;
  }

  @Override
  public String send(ECKey senderECKey, Address receiveAddress, BigInteger value, byte[] data)
      throws TransactionFailedException {

    BigInteger nonce = nonceObtainer.getNonce(Address.from(senderECKey.getAddress()));

    Transaction transaction;
    try {
      transaction =
          CallTransaction.createRawTransaction(nonce.longValue(), gasPriceObtainer.getGasPrice()
              .longValue(),
              gasLimitEstimator.estimate(Address.from(senderECKey.getAddress()), receiveAddress,
                  data)
              .longValue(), receiveAddress.toHexString(), value.longValue(), data);
    } catch (EstimateGasException e) {
      throw new TransactionFailedException(e);
    }

    transaction.sign(senderECKey);

    String transactionHash;
    try {
      transactionHash =
          web3j.ethSendRawTransaction("0x" + Hex.toHexString(transaction.getEncoded()))
          .send()
          .getTransactionHash();
    } catch (IOException e) {
      throw new TransactionFailedException(e);
    }

    if (transactionHash != null) {
      return transactionHash;
    } else {
      throw new TransactionFailedException("ethSendRawTransaction returned null!");
    }
  }
}
