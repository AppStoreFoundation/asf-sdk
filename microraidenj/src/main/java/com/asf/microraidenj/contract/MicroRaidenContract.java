package com.asf.microraidenj.contract;

import com.asf.microraidenj.eth.TransactionSender;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import ethereumj.core.CallTransaction;
import ethereumj.crypto.ECKey;
import java.math.BigInteger;

public final class MicroRaidenContract {

  private final Address channelManagerAddr;
  private final Address tokenAddr;

  private final TransactionSender transactionSender;

  public MicroRaidenContract(Address channelManagerAddr, Address tokenAddr,
      TransactionSender transactionSender) {
    this.channelManagerAddr = channelManagerAddr;
    this.tokenAddr = tokenAddr;
    this.transactionSender = transactionSender;
  }

  public String callChannelTopUp(ECKey senderECKey, Address receiverAddress,
      BigInteger depositToAdd, BigInteger openBlockNumber) throws TransactionFailedException {

    CallTransaction.Function approveFunction =
        CallTransaction.Function.fromSignature("topUp", "address", "uint32", "uint192");

    byte[] encoded =
        approveFunction.encode(receiverAddress.toHexString(), openBlockNumber, depositToAdd);

    return transactionSender.send(senderECKey, channelManagerAddr, BigInteger.ZERO, encoded);
  }

  public String callApprove(ECKey senderECKey, BigInteger deposit)
      throws TransactionFailedException {

    CallTransaction.Function approveFunction =
        CallTransaction.Function.fromSignature("approve", "address", "uint256");

    byte[] encoded = approveFunction.encode(channelManagerAddr.toHexString(), deposit);

    return transactionSender.send(senderECKey, tokenAddr, BigInteger.ZERO, encoded);
  }

  public String callCreateChannel(ECKey senderECKey, Address receiverAddress, BigInteger deposit)
      throws TransactionFailedException {

    CallTransaction.Function createChannelFunction =
        CallTransaction.Function.fromSignature("createChannel", "address", "uint192");

    byte[] encoded = createChannelFunction.encode(receiverAddress.toHexString(), deposit);

    return transactionSender.send(senderECKey, channelManagerAddr, BigInteger.ZERO, encoded);
  }

  public String callCooperativeClose(ECKey ecKey, Address receiverAddress,
      BigInteger openBlockNumber, BigInteger owedBalance, byte[] balanceMsgSigned,
      byte[] closingMsgSigned) throws TransactionFailedException {

    CallTransaction.Function createChannelFunction =
        CallTransaction.Function.fromSignature("cooperativeClose", "address", "uint32", "uint192",
            "bytes", "bytes");

    byte[] encoded =
        createChannelFunction.encode(receiverAddress.toHexString(true), openBlockNumber,
            owedBalance, balanceMsgSigned, closingMsgSigned);

    return transactionSender.send(ecKey, channelManagerAddr, BigInteger.ZERO, encoded);
  }
}
