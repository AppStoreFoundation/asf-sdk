package com.asf.microraidenj.eth.interfaces;

import com.asf.microraidenj.exception.TransactionFailedException;
import ethereumj.crypto.ECKey;

public interface TransactionSender {

  String send(ECKey senderECKey, byte[] receiveAddress, byte[] value, byte[] data)
      throws TransactionFailedException;
}
