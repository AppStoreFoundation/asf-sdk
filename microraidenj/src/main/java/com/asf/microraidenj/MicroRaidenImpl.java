package com.asf.microraidenj;

import com.asf.microraidenj.eth.GetChannelBlock;
import com.asf.microraidenj.eth.TransactionSender;
import com.asf.microraidenj.exception.DepositTooHighException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import ethereumj.core.CallTransaction;
import ethereumj.crypto.ECKey;
import java.math.BigInteger;

public final class MicroRaidenImpl implements MicroRaiden {

  private final BigInteger maxDeposit;
  private final Address channelManagerAddr;
  private final Address tokenAddr;

  private final TransactionSender transactionSender;
  private final GetChannelBlock getChannelBlock;

  public MicroRaidenImpl(Address channelManagerAddr, Address tokenAddr, BigInteger maxDeposit,
      TransactionSender transactionSender, GetChannelBlock getChannelBlock) {
    this.channelManagerAddr = channelManagerAddr;
    this.tokenAddr = tokenAddr;
    this.maxDeposit = maxDeposit;
    this.transactionSender = transactionSender;
    this.getChannelBlock = getChannelBlock;
  }

  @Override
  public BigInteger createChannel(ECKey senderECKey, Address receiverAddress, BigInteger deposit)
      throws TransactionFailedException, DepositTooHighException {
    try {
      if (maxDeposit.compareTo(deposit) < 0) {
        throw new DepositTooHighException(maxDeposit);
      }

      String approveTxHash = callApprove(senderECKey, deposit);
      String createChannelTxHash = callCreateChannel(senderECKey, receiverAddress, deposit);

      return getChannelBlock.get(com.asf.microraidenj.type.ByteArray.from(createChannelTxHash));
    } catch (DepositTooHighException e) {
      throw e;
    } catch (Exception e) {
      throw new TransactionFailedException("Failed to create channel", e);
    }
  }

  @Override
  public void topUpChannel(ECKey senderECKey, Address receiverAddress, BigInteger depositToAdd,
      BigInteger openBlockNumber) throws TransactionFailedException {

    String approveTxHash = callApprove(senderECKey, depositToAdd);
    String topUpChannelTxHash =
        callChannelTopUp(senderECKey, receiverAddress, depositToAdd, openBlockNumber);
  }

  @Override
  public String closeChannelCooperativelySender(ECKey senderECKey, Address receiverAddress,
      BigInteger openBlockNum, BigInteger owedBalance, byte[] closingMsgSigned, ECKey ecKey)
      throws TransactionFailedException {

    return callCooperativeClose(ecKey, receiverAddress, openBlockNum, owedBalance,
        MicroRaidenUtils.createBalanceMsgHash(receiverAddress, openBlockNum, owedBalance,
            senderECKey, channelManagerAddr), closingMsgSigned);
  }

  @Override
  public String closeChannelCooperativelyReceiver(ECKey receiverECKey, Address senderAddress,
      BigInteger openBlockNum, BigInteger owedBalance, byte[] balanceMsgSigned, ECKey ecKey)
      throws TransactionFailedException {

    Address receiverAddress = Address.from(receiverECKey.getAddress());

    return callCooperativeClose(ecKey, receiverAddress, openBlockNum, owedBalance, balanceMsgSigned,
        MicroRaidenUtils.createClosingMsgHash(senderAddress, openBlockNum, owedBalance,
            receiverECKey, channelManagerAddr));
  }

  @Override public byte[] createBalanceProof(ECKey senderECKey, Address receiverAddress,
      BigInteger openBlockNum, BigInteger owedBalance) {

    return MicroRaidenUtils.createBalanceMsgHash(receiverAddress, openBlockNum, owedBalance,
        senderECKey, channelManagerAddr);
  }

  private String callChannelTopUp(ECKey senderECKey, Address receiverAddress,
      BigInteger depositToAdd, BigInteger openBlockNumber) throws TransactionFailedException {

    CallTransaction.Function approveFunction =
        CallTransaction.Function.fromSignature("topUp", "address", "uint32", "uint192");

    byte[] encoded =
        approveFunction.encode(receiverAddress.toHexString(), openBlockNumber, depositToAdd);

    return transactionSender.send(senderECKey, channelManagerAddr, BigInteger.ZERO, encoded);
  }

  private String callApprove(ECKey senderECKey, BigInteger deposit)
      throws TransactionFailedException {

    CallTransaction.Function approveFunction =
        CallTransaction.Function.fromSignature("approve", "address", "uint256");

    byte[] encoded = approveFunction.encode(channelManagerAddr.toHexString(), deposit);

    return transactionSender.send(senderECKey, tokenAddr, BigInteger.ZERO, encoded);
  }

  private String callCreateChannel(ECKey senderECKey, Address receiverAddress, BigInteger deposit)
      throws TransactionFailedException {

    CallTransaction.Function createChannelFunction =
        CallTransaction.Function.fromSignature("createChannel", "address", "uint192");

    byte[] encoded = createChannelFunction.encode(receiverAddress.toHexString(), deposit);

    return transactionSender.send(senderECKey, channelManagerAddr, BigInteger.ZERO, encoded);
  }

  private String callCooperativeClose(ECKey ecKey, Address receiverAddress,
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
