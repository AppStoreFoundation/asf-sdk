package com.asf.microraidenj;

import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import ethereumj.crypto.ECKey;
import java.math.BigInteger;

public interface MicroRaidenServer {

  byte[] createClosingMessage(ECKey receiverECKey, Address senderAddress, BigInteger openBlockNum,
      BigInteger owedBalance);

  String closeChannelCooperatively(Address senderAddress, ECKey receiverECKey,
      BigInteger openBlockNum, BigInteger owedBalance, byte[] balanceMsgSigned, ECKey ecKey)
      throws TransactionFailedException;
}
