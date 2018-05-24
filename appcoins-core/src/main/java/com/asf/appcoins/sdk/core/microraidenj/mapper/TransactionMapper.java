package com.asf.appcoins.sdk.core.microraidenj.mapper;

import com.asf.microraidenj.entities.TransactionReceipt;
import com.asf.microraidenj.type.Address;
import java.math.BigInteger;
import org.spongycastle.util.encoders.Hex;

public class TransactionMapper {

  public static TransactionReceipt from(
      org.web3j.protocol.core.methods.response.TransactionReceipt transactionReceipt) {

    Address address = Address.from(transactionReceipt.getTo());
    String blockHash = transactionReceipt.getBlockHash();
    BigInteger blockNumber = transactionReceipt.getBlockNumber();
    byte[] data = Hex.decode(transactionReceipt.getLogs()
        .get(0)
        .getData()
        .substring(2));
    BigInteger gasUsed = transactionReceipt.getGasUsed();
    String transactionHash = transactionReceipt.getTransactionHash();
    BigInteger transactionIndex = transactionReceipt.getTransactionIndex();

    return new TransactionReceipt(address, blockHash, blockNumber, data, gasUsed, transactionHash,
        transactionIndex);
  }
}
