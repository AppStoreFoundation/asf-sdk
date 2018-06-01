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
import java.util.logging.Logger;
import org.spongycastle.util.encoders.Hex;

public final class MicroRaidenImpl implements MicroRaiden {

  private final BigInteger maxDeposit;
  private final Address channelManagerAddr;
  private final Address tokenAddr;

  private final TransactionSender transactionSender;
  private final GetChannelBlock getChannelBlock;

  public MicroRaidenImpl(Address channelManagerAddr, Address tokenAddr, Logger log,
      BigInteger maxDeposit, TransactionSender transactionSender, GetChannelBlock getChannelBlock) {
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
      byte[] senderAddress = senderECKey.getAddress();

      if (maxDeposit.compareTo(deposit) < 0) {
        throw new DepositTooHighException(maxDeposit);
      }

      String approveTxHash = callApprove(senderECKey, deposit);
      String createChannelTxHash = callCreateChannel(senderECKey, receiverAddress, deposit);

      return getChannelBlock.get(createChannelTxHash);
    } catch (DepositTooHighException e) {
      throw e;
    } catch (Exception e) {
      throw new TransactionFailedException("Failed to create channel", e);
    }
  }

  @Override
  public void topUpChannel(ECKey senderECKey, Address receiverAddress, BigInteger depositToAdd,
      BigInteger openBlockNumber) {

    String approveTxHash = callApprove(senderECKey, depositToAdd);
    String topUpChannelTxHash =
        callChannelTopUp(senderECKey, receiverAddress, depositToAdd, openBlockNumber);

    BigInteger bigInteger = getChannelBlock.get(topUpChannelTxHash);
  }

  public byte[] getClosingMsgHash(Address senderAddress, BigInteger openBlockNumber,
      BigInteger owedBalance) {
    byte[] receiverAddressBytes = senderAddress.getDecoded();
    byte[] channelAddressBytes = channelManagerAddr.getDecoded();

    byte[] openBlockNumberBytes = ByteArray.prependZeros(openBlockNumber.toByteArray(), 4);
    byte[] balanceBytes = ByteArray.prependZeros(Hex.decode(prependZerosIfNeeded(owedBalance)), 24);

    byte[] dataTypeName =
        "string message_idaddress senderuint32 block_createduint192 balanceaddress contract".getBytes(
            Charset.forName("UTF-8"));
    byte[] dataValue =
        ByteArray.concat("Receiver closing signature".getBytes(), receiverAddressBytes,
            openBlockNumberBytes, balanceBytes, channelAddressBytes);
    byte[] result =
        HashUtil.sha3(ByteArray.concat(HashUtil.sha3(dataTypeName), HashUtil.sha3(dataValue)));

    return result;
  }

  public byte[] getClosingMsgHashSigned(Address senderAddress, BigInteger openBlockNumber,
      BigInteger owedBalance, ECKey receiverECKey) {
    byte[] closingMsgHash = getClosingMsgHash(senderAddress, openBlockNumber, owedBalance);

    return receiverECKey.sign(closingMsgHash)
        .toByteArray();
  }

  public byte[] getBalanceMsgHash(Address receiverAddress, BigInteger openBlockNumber,
      BigInteger owedBalance) {
    byte[] receiverAddressBytes = receiverAddress.getDecoded();
    byte[] channelAddressBytes = channelManagerAddr.getDecoded();

    byte[] openBlockNumberBytes = ByteArray.prependZeros(openBlockNumber.toByteArray(), 4);
    byte[] balanceBytes = ByteArray.prependZeros(Hex.decode(prependZerosIfNeeded(owedBalance)), 24);

    byte[] dataTypeName =
        "string message_idaddress receiveruint32 block_createduint192 balanceaddress contract".getBytes(
            Charset.forName("UTF-8"));
    byte[] dataValue =
        ByteArray.concat("Sender balance proof signature".getBytes(), receiverAddressBytes,
            openBlockNumberBytes, balanceBytes, channelAddressBytes);
    byte[] result =
        HashUtil.sha3(ByteArray.concat(HashUtil.sha3(dataTypeName), HashUtil.sha3(dataValue)));

    return result;
  }

  public byte[] getBalanceMsgHashSigned(Address receiverAddress, BigInteger openBlockNumber,
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
      BigInteger depositToAdd,
      BigInteger openBlockNumber) {

    CallTransaction.Function approveFunction =
        CallTransaction.Function.fromSignature("topUp", "address", "uint32", "uint192");

    byte[] encoded = approveFunction.encode(receiverAddress.get(), openBlockNumber, depositToAdd);

    return transactionSender.send(senderECKey, channelManagerAddr, BigInteger.ZERO.longValue(),
        encoded);
  }

  private String callApprove(ECKey senderECKey, BigInteger deposit) {

    CallTransaction.Function approveFunction =
        CallTransaction.Function.fromSignature("approve", "address", "uint256");

    byte[] encoded = approveFunction.encode(channelManagerAddr.get(), deposit);

    return transactionSender.send(senderECKey, tokenAddr, BigInteger.ZERO.longValue(), encoded);
  }

  private String callCreateChannel(ECKey senderECKey, Address receiverAddress, BigInteger deposit) {

    CallTransaction.Function createChannelFunction =
        CallTransaction.Function.fromSignature("createChannel", "address", "uint192");

    byte[] encoded = createChannelFunction.encode(receiverAddress.get(), deposit);

    return transactionSender.send(senderECKey, channelManagerAddr,
        new BigInteger("0", 10).longValue(), encoded);
  }

  private String callCooperativeClose(ECKey ecKey, Address receiverAddress,
      BigInteger openBlockNumber, BigInteger owedBalance, byte[] balanceMsgSigned,
      byte[] closingMsgSigned) {

    CallTransaction.Function createChannelFunction =
        CallTransaction.Function.fromSignature("cooperativeClose", "address", "uint32", "uint192",
            "bytes", "bytes");

    byte[] encoded =
        createChannelFunction.encode(receiverAddress.get(true), openBlockNumber, owedBalance,
            balanceMsgSigned, closingMsgSigned);

    return transactionSender.send(ecKey, channelManagerAddr, 0, encoded);
  }

  public String closeChannelCooperatively(ECKey ecKey, Address receiverAddress,
      BigInteger openBlockNum, BigInteger owedBalance, byte[] balanceMsgHashSig,
      byte[] closingMsgHashSig) {

    if (BigInteger.ZERO.equals(owedBalance)) {
      throw new IllegalArgumentException("Owed balance cannot be zero!");
    }

    return callCooperativeClose(ecKey, receiverAddress, openBlockNum, owedBalance,
        balanceMsgHashSig, closingMsgHashSig);
  }
}
