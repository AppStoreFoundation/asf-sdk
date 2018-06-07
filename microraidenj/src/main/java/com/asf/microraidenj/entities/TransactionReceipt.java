package com.asf.microraidenj.entities;

import com.asf.microraidenj.type.Address;
import java.math.BigInteger;

public class TransactionReceipt {

  private final Address address;
  private final String blockHash;
  private final BigInteger blockNumber;
  private final byte[] data;
  private final BigInteger gasUsed;
  private final String transactionHash;
  private final BigInteger transactionIndex;

  public TransactionReceipt(Address address, String blockHash, BigInteger blockNumber, byte[] data,
      BigInteger gasUsed, String transactionHash, BigInteger transactionIndex) {
    this.address = address;
    this.blockHash = blockHash;
    this.blockNumber = blockNumber;
    this.data = data;
    this.gasUsed = gasUsed;
    this.transactionHash = transactionHash;
    this.transactionIndex = transactionIndex;
  }

  public Address getAddress() {
    return address;
  }

  public String getBlockHash() {
    return blockHash;
  }

  public BigInteger getBlockNumber() {
    return blockNumber;
  }

  public byte[] getData() {
    return data;
  }

  public BigInteger getGasUsed() {
    return gasUsed;
  }

  public String getTransactionHash() {
    return transactionHash;
  }

  public BigInteger getTransactionIndex() {
    return transactionIndex;
  }
}
