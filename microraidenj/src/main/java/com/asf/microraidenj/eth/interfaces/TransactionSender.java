package com.asf.microraidenj.eth.interfaces;

import com.asf.microraidenj.type.Address;
import ethereumj.crypto.ECKey;

public interface TransactionSender {

  String send(ECKey senderECKey, Address receiveAddress, long value, byte[] data);
}
