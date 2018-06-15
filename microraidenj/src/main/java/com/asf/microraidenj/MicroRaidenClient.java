package com.asf.microraidenj;

import com.asf.microraidenj.exception.DepositTooHighException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import ethereumj.crypto.ECKey;
import java.math.BigInteger;

public interface MicroRaidenClient {

  BigInteger createChannel(ECKey senderECKey, Address receiverAddress, BigInteger deposit)
      throws TransactionFailedException, DepositTooHighException;

  void topUpChannel(ECKey senderECKey, Address receiverAddress, BigInteger openBlockNumber,
      BigInteger deposit) throws TransactionFailedException, DepositTooHighException;

  String closeChannelCooperatively(ECKey senderECKey, Address receiverAddress,
      BigInteger openBlockNum, BigInteger owedBalance, byte[] closingMsgSigned, ECKey ecKey)
      throws TransactionFailedException;

  byte[] createBalanceProof(ECKey senderECKey, Address receiverAddress, BigInteger openBlockNum,
      BigInteger owedBalance);
}
