package com.bds.microraidenj.channel;

import com.asf.microraidenj.exception.DepositTooHighException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import ethereumj.crypto.ECKey;
import java.math.BigInteger;

public interface BDSChannelClient {

  void topUp(BigInteger depositToAdd) throws TransactionFailedException, DepositTooHighException;

  String closeCooperatively(ECKey ecKey) throws TransactionFailedException;

  void makePayment(BigInteger amount, Address devAddress, Address storeAddress, Address oemAddress)
      throws InsufficientFundsException, TransactionFailedException;

  BigInteger getOpenBlockNumber();

  BigInteger getBalance();

  Address getSenderAddress();

  Address getReceiverAddress();
}
