package com.asf.microraidenj;

import com.asf.microraidenj.exception.DepositTooHighException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import ethereumj.crypto.ECKey;
import java.math.BigInteger;

public interface MicroRaiden {

  BigInteger createChannel(ECKey senderECKey, Address receiverAddress, BigInteger deposit)
      throws TransactionFailedException, DepositTooHighException;

  void topUpChannel(ECKey senderECKey, Address receiverAddress, BigInteger deposit,
      BigInteger openBlockNumber) throws TransactionFailedException, DepositTooHighException;

  String closeChannelCooperativelySender(ECKey senderECKey, Address receiverAddress,
      BigInteger openBlockNum, BigInteger owedBalance, byte[] closingMsgSigned, ECKey ecKey)
      throws TransactionFailedException;

  String closeChannelCooperativelyReceiver(ECKey receiverECKey, Address senderAddress,
      BigInteger openBlockNum, BigInteger owedBalance, byte[] balanceMsgSigned, ECKey ecKey)
      throws TransactionFailedException;

  byte[] createBalanceProof(ECKey senderECKey, Address receiverAddress, BigInteger openBlockNum,
      BigInteger owedBalance);
}
