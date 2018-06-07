package com.asf.microraidenj;

import com.asf.microraidenj.type.Address;
import java.math.BigInteger;
import java.util.logging.Logger;
import org.spongycastle.util.encoders.Hex;

final class MicroRaidenLogger {

  private final Logger logger;

  MicroRaidenLogger(Logger logger) {
    this.logger = logger;
  }

  void logChannelCreationAttempt(Address receiverAddress, BigInteger deposit,
      byte[] senderAddress) {
    logger.fine("Going to create a channel between "
        + Hex.toHexString(senderAddress)
        + " and "
        + receiverAddress
        + " with a maximum of "
        + deposit
        + " tokens.");
  }

  void logChannelCreation(Address receiverAddress, BigInteger deposit, byte[] senderAddress,
      String txHash) {
    logger.fine("Created a channel between "
        + Hex.toHexString(senderAddress)
        + " and "
        + receiverAddress
        + " with a maximum of "
        + deposit
        + " tokens. txHash: "
        + txHash);
  }
}
