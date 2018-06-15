package com.bds.microraidenj.channel;

import com.asf.microraidenj.exception.DepositTooHighException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import ethereumj.crypto.ECKey;
import io.reactivex.Single;
import java.math.BigInteger;

public interface ChannelClient {

  void topUp(BigInteger depositToAdd) throws TransactionFailedException, DepositTooHighException;

  String closeCooperatively(BigInteger owedBalance, ECKey ecKey) throws TransactionFailedException;

  Single<byte[]> transfer(BigInteger amount)
      throws InsufficientFundsException, TransactionFailedException;

  BigInteger getOpenBlockNumber();

  BigInteger getBalance();

  Address getSenderAddress();

  Address getReceiverAddress();
}
