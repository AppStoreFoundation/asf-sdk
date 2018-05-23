package com.asf.microraidenj;

import com.asf.microraidenj.exception.DepositTooHighException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import ethereumj.crypto.ECKey;
import java.math.BigInteger;

public interface MicroRaiden {

  void createChannel(ECKey privKey, Address receiverAddress, BigInteger deposit)
      throws TransactionFailedException, DepositTooHighException;
}
