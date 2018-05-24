package com.asf.microraidenj;

import com.asf.microraidenj.entities.TransactionReceipt;
import com.asf.microraidenj.eth.interfaces.GetTransactionReceipt;
import com.asf.microraidenj.eth.interfaces.TransactionSender;
import com.asf.microraidenj.exception.DepositTooHighException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import ethereumj.core.CallTransaction;
import ethereumj.crypto.ECKey;
import java.math.BigInteger;
import java.util.logging.Logger;

public final class MicroRaidenImpl implements MicroRaiden {

  private final MicroRaidenLogger log;

  private final BigInteger maxDeposit;
  private final Address channelManagerAddr;
  private final Address tokenAddr;

  private final TransactionSender transactionSender;
  private final GetTransactionReceipt getTransactionReceipt;

  public MicroRaidenImpl(Address channelManagerAddr, Address tokenAddr, Logger log,
      BigInteger maxDeposit, TransactionSender transactionSender,
      GetTransactionReceipt getTransactionReceipt) {
    this.log = new MicroRaidenLogger(log);

    this.channelManagerAddr = channelManagerAddr;
    this.tokenAddr = tokenAddr;
    this.maxDeposit = maxDeposit;
    this.transactionSender = transactionSender;
    this.getTransactionReceipt = getTransactionReceipt;
  }

  @Override
  public BigInteger createChannel(ECKey ecKey, Address receiverAddress, BigInteger deposit)
      throws TransactionFailedException, DepositTooHighException {
    try {
      byte[] senderAddress = ecKey.getAddress();

      if (maxDeposit.compareTo(deposit) < 0) {
        throw new DepositTooHighException(maxDeposit);
      }

      log.logChannelCreationAttempt(receiverAddress, deposit, senderAddress);

      String approveTxHash = callApprove(ecKey, deposit);
      String createChannelTxHash = callCreateChannel(ecKey, receiverAddress, deposit);

      TransactionReceipt transactionReceipt = getTransactionReceipt.get(createChannelTxHash)
          .blockingGet();

      log.logChannelCreation(receiverAddress, deposit, senderAddress,
          transactionReceipt.getTransactionHash());

      return transactionReceipt.getBlockNumber();
    } catch (DepositTooHighException e) {
      throw e;
    } catch (Exception e) {
      throw new TransactionFailedException("Failed to create channel", e);
    }
  }

  @Override public void topUpChannel(ECKey ecKey, Address receiverAddress, BigInteger depositToAdd,
      BigInteger openBlockNumber) {
    byte[] senderAddress = ecKey.getAddress();

    String approveTxHash = callApprove(ecKey, depositToAdd);
    String topUpChannelTxHash =
        callChannelTopUp(ecKey, receiverAddress, depositToAdd, openBlockNumber);

    TransactionReceipt transactionReceipt = getTransactionReceipt.get(topUpChannelTxHash)
        .blockingGet();
  }

  private String callChannelTopUp(ECKey ecKey, Address receiverAddress, BigInteger depositToAdd,
      BigInteger openBlockNumber) {

    CallTransaction.Function approveFunction =
        CallTransaction.Function.fromSignature("topUp", "address", "uint32", "uint192");

    byte[] encoded = approveFunction.encode(receiverAddress.get(), openBlockNumber, depositToAdd);

    return transactionSender.send(ecKey, channelManagerAddr, BigInteger.ZERO.longValue(), encoded);
  }

  private String callApprove(ECKey ecKey, BigInteger deposit) {
    CallTransaction.Function approveFunction =
        CallTransaction.Function.fromSignature("approve", "address", "uint256");

    byte[] encoded = approveFunction.encode(channelManagerAddr.get(), deposit);

    return transactionSender.send(ecKey, tokenAddr, BigInteger.ZERO.longValue(), encoded);
  }

  private String callCreateChannel(ECKey ecKey, Address receiverAddress, BigInteger deposit) {

    CallTransaction.Function createChannelFunction =
        CallTransaction.Function.fromSignature("createChannel", "address", "uint192");

    byte[] encoded = createChannelFunction.encode(receiverAddress.get(), deposit);

    return transactionSender.send(ecKey, channelManagerAddr, new BigInteger("0", 10).longValue(),
        encoded);
  }
}
