package com.asf.microraidenj;

import com.asf.microraidenj.eth.interfaces.GetChannelBlock;
import com.asf.microraidenj.eth.interfaces.TransactionSender;
import com.asf.microraidenj.exception.DepositTooHighException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import com.asf.microraidenj.util.ByteArray;
import ethereumj.core.CallTransaction;
import ethereumj.crypto.ECKey;
import ethereumj.crypto.HashUtil;
import java.math.BigInteger;
import java.nio.charset.Charset;
import org.spongycastle.util.encoders.Hex;

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

  public String closeChannelCooperatively(ECKey senderECKey, ECKey receiverECKey,
      BigInteger openBlockNum, BigInteger owedBalance) throws TransactionFailedException {
    if (BigInteger.ZERO.equals(owedBalance)) {
      throw new IllegalArgumentException("Owed balance cannot be zero!");
    }

    Address senderAddress = Address.from(senderECKey.getAddress());
    Address receiverAddress = Address.from(receiverECKey.getAddress());

    return callCooperativeClose(senderECKey, receiverAddress, openBlockNum, owedBalance,
        getBalanceMsgHashSigned(receiverAddress, openBlockNum, owedBalance, senderECKey),
        getClosingMsgHashSigned(senderAddress, openBlockNum, owedBalance, receiverECKey));
  }

  private byte[] getClosingMsgHash(Address senderAddress, BigInteger openBlockNumber,
      BigInteger owedBalance) {
    byte[] receiverAddressBytes = senderAddress.getBytes();
    byte[] channelAddressBytes = channelManagerAddr.getBytes();

    byte[] openBlockNumberBytes = ByteArray.prependZeros(openBlockNumber.toByteArray(), 4);
    byte[] balanceBytes = ByteArray.prependZeros(Hex.decode(prependZerosIfNeeded(owedBalance)), 24);

    byte[] dataTypeName =
        "string message_idaddress senderuint32 block_createduint192 balanceaddress contract".getBytes(
            Charset.forName("UTF-8"));
    byte[] dataValue =
        ByteArray.concat("Receiver closing signature".getBytes(), receiverAddressBytes,
            openBlockNumberBytes, balanceBytes, channelAddressBytes);

    return HashUtil.sha3(ByteArray.concat(HashUtil.sha3(dataTypeName), HashUtil.sha3(dataValue)));
  }

  private byte[] getClosingMsgHashSigned(Address senderAddress, BigInteger openBlockNumber,
      BigInteger owedBalance, ECKey receiverECKey) {
    byte[] closingMsgHash = getClosingMsgHash(senderAddress, openBlockNumber, owedBalance);

    return receiverECKey.sign(closingMsgHash)
        .toByteArray();
  }

  private byte[] getBalanceMsgHash(Address receiverAddress, BigInteger openBlockNumber,
      BigInteger owedBalance) {
    byte[] receiverAddressBytes = receiverAddress.getBytes();
    byte[] channelAddressBytes = channelManagerAddr.getBytes();

    byte[] openBlockNumberBytes = ByteArray.prependZeros(openBlockNumber.toByteArray(), 4);
    byte[] balanceBytes = ByteArray.prependZeros(Hex.decode(prependZerosIfNeeded(owedBalance)), 24);

    byte[] dataTypeName =
        "string message_idaddress receiveruint32 block_createduint192 balanceaddress contract".getBytes(
            Charset.forName("UTF-8"));
    byte[] dataValue =
        ByteArray.concat("Sender balance proof signature".getBytes(), receiverAddressBytes,
            openBlockNumberBytes, balanceBytes, channelAddressBytes);

    return HashUtil.sha3(ByteArray.concat(HashUtil.sha3(dataTypeName), HashUtil.sha3(dataValue)));
  }

  private byte[] getBalanceMsgHashSigned(Address receiverAddress, BigInteger openBlockNumber,
      BigInteger owedBalance, ECKey senderECKey) {
    byte[] balanceMsgHash = getBalanceMsgHash(receiverAddress, openBlockNumber, owedBalance);

    return senderECKey.sign(balanceMsgHash)
        .toByteArray();
  }

  private String prependZerosIfNeeded(BigInteger balance) {
    String s = balance.toString(16);
    return s.length() % 2 == 0 ? s : '0' + s;
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
            owedBalance,
            balanceMsgSigned, closingMsgSigned);

    return transactionSender.send(ecKey, channelManagerAddr, BigInteger.ZERO, encoded);
  }
}
