package com.bds.microraidenj.channel;

import com.asf.microraidenj.exception.DepositTooHighException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import com.bds.microraidenj.ws.ChannelHistoryResponse;
import ethereumj.crypto.ECKey;
import io.reactivex.Single;
import java.math.BigInteger;
import java.util.List;

public interface BDSChannel {

  void topUp(BigInteger depositToAdd) throws TransactionFailedException, DepositTooHighException;

  String closeCooperatively(ECKey ecKey) throws TransactionFailedException;

  Single<String> makePayment(BigInteger amount, Address devAddress, Address storeAddress,
      Address oemAddress)
      throws InsufficientFundsException, TransactionFailedException;

  Single<List<ChannelHistoryResponse.MicroTransaction>> listTransactions();

  BigInteger getOpenBlockNumber();

  BigInteger getBalance();

  Address getSenderAddress();

  Address getReceiverAddress();
}
