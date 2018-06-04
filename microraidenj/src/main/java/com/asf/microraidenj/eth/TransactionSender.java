package com.asf.microraidenj.eth;

import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import ethereumj.crypto.ECKey;
import java.math.BigInteger;

public interface TransactionSender {

  String send(ECKey senderECKey, Address receiveAddress, BigInteger value, byte[] data)
      throws TransactionFailedException;
}
