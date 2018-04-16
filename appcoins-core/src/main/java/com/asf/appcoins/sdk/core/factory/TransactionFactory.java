package com.asf.appcoins.sdk.core.factory;

import com.asf.appcoins.sdk.core.transaction.Transaction;
import com.asf.appcoins.sdk.core.transaction.Transaction.Status;
import java.math.BigInteger;
import java.util.Arrays;
import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

/**
 * Created by neuro on 28-02-2018.
 */

public final class TransactionFactory {
  private TransactionFactory() {
  }

  public static Transaction fromEthGetTransactionReceipt(
      EthGetTransactionReceipt ethGetTransactionReceipt) {
    TransactionReceipt transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt();

    String hash = transactionReceipt.getTransactionHash();
    String from = transactionReceipt.getFrom();
    Log log = transactionReceipt.getLogs()
        .get(0);
    String to = log.getAddress();
    String value = extractValueFromEthGetTransactionReceipt(log.getData());
    Status status = parseStatus(transactionReceipt.getStatus());
    String contractAddress = ethGetTransactionReceipt.getTransactionReceipt()
        .getTo();

    return new Transaction(hash, from, to, value, status);
  }

  public static Transaction fromEthTransaction(EthTransaction ethTransaction, Status status) {
    org.web3j.protocol.core.methods.response.Transaction transaction =
        ethTransaction.getTransaction();
    String hash = transaction.getHash();
    String from = transaction.getFrom();
    String input = ethTransaction.getTransaction()
        .getInput();
    String to = extractToFromEthTransaction(input);
    String value = extractValueFromEthTransaction(input);
    String contractAddress = ethTransaction.getTransaction()
        .getTo();

    return new Transaction(hash, from, to, value, status);
  }

  private static Status parseStatus(String status) {
    if (status.equals("0x1")) {
      return Status.ACCEPTED;
    } else {
      return Status.FAILED;
    }
  }

  static String extractValueFromEthGetTransactionReceipt(String data) {
    if (data.startsWith("0x")) {
      data = data.substring(2);
    }

    return decodeInt(Hex.decode(data), 0).toString();
  }

  static String extractValueFromEthTransaction(String input) {
    String valueHex = input.substring(input.length() - ((256 >> 2)));
    BigInteger value = decodeInt(Hex.decode(valueHex), 0);
    return value.toString();
  }

  static String extractToFromEthTransaction(String input) {
    String valueHex = input.substring(10, input.length() - ((256 >> 2)));
    Address address = new Address(decodeInt(Hex.decode(valueHex), 0));

    return address.toString();
  }

  public static BigInteger decodeInt(byte[] encoded, int offset) {
    return new BigInteger(Arrays.copyOfRange(encoded, offset, offset + 32));
  }
}
