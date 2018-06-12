package com.bds.microraidenj;

import com.asf.microraidenj.MicroRaidenClient;
import com.asf.microraidenj.contract.MicroRaidenContract;
import com.asf.microraidenj.eth.ChannelBlockObtainer;
import com.asf.microraidenj.exception.DepositTooHighException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import com.asf.microraidenj.util.ByteUtils;
import ethereumj.crypto.ECKey;
import ethereumj.crypto.HashUtil;
import java.math.BigInteger;
import java.nio.charset.Charset;
import org.spongycastle.util.encoders.Hex;

public final class DefaultMicroRaidenClient implements MicroRaidenClient {

  private final BigInteger maxDeposit;
  private final Address channelManagerAddr;

  private final ChannelBlockObtainer channelBlockObtainer;

  private final MicroRaidenContract microRaidenContract;

  public DefaultMicroRaidenClient(Address channelManagerAddr, BigInteger maxDeposit,
      ChannelBlockObtainer channelBlockObtainer, MicroRaidenContract microRaidenContract) {
    this.channelManagerAddr = channelManagerAddr;
    this.maxDeposit = maxDeposit;
    this.channelBlockObtainer = channelBlockObtainer;
    this.microRaidenContract = microRaidenContract;
  }

  @Override
  public BigInteger createChannel(ECKey senderECKey, Address receiverAddress, BigInteger deposit)
      throws TransactionFailedException, DepositTooHighException {
    try {
      if (maxDeposit.compareTo(deposit) < 0) {
        throw new DepositTooHighException(maxDeposit);
      }

      String approveTxHash = microRaidenContract.callApprove(senderECKey, deposit);
      String createChannelTxHash =
          microRaidenContract.callCreateChannel(senderECKey, receiverAddress, deposit);

      return channelBlockObtainer.get(
          com.asf.microraidenj.type.ByteArray.from(createChannelTxHash));
    } catch (DepositTooHighException e) {
      throw e;
    } catch (Exception e) {
      throw new TransactionFailedException("Failed to create channel", e);
    }
  }

  @Override
  public void topUpChannel(ECKey senderECKey, Address receiverAddress, BigInteger openBlockNumber,
      BigInteger depositToAdd) throws TransactionFailedException {

    String approveTxHash = microRaidenContract.callApprove(senderECKey, depositToAdd);
    String topUpChannelTxHash =
        microRaidenContract.callChannelTopUp(senderECKey, receiverAddress, depositToAdd,
            openBlockNumber);
  }

  @Override public String closeChannelCooperatively(ECKey senderECKey, Address receiverAddress,
      BigInteger openBlockNum, BigInteger owedBalance, byte[] closingMsgSigned, ECKey ecKey)
      throws TransactionFailedException {

    return microRaidenContract.callCooperativeClose(ecKey, receiverAddress, openBlockNum,
        owedBalance, createBalanceMsgHash(receiverAddress, openBlockNum, owedBalance, senderECKey,
            channelManagerAddr), closingMsgSigned);
  }

  @Override public byte[] createBalanceProof(ECKey senderECKey, Address receiverAddress,
      BigInteger openBlockNum, BigInteger owedBalance) {

    return createBalanceMsgHash(receiverAddress, openBlockNum, owedBalance, senderECKey,
        channelManagerAddr);
  }

  private byte[] createBalanceMsgHash(Address receiverAddress, BigInteger openBlockNumber,
      BigInteger owedBalance, ECKey senderECKey, Address channelManagerAddr) {
    byte[] balanceMsgHash =
        createBalanceMsgHashRaw(receiverAddress, openBlockNumber, owedBalance, channelManagerAddr);

    return senderECKey.sign(balanceMsgHash)
        .toByteArray();
  }

  private byte[] createBalanceMsgHashRaw(Address receiverAddress, BigInteger openBlockNumber,
      BigInteger owedBalance, Address channelManagerAddr) {
    byte[] receiverAddressBytes = receiverAddress.getBytes();
    byte[] channelAddressBytes = channelManagerAddr.getBytes();

    byte[] openBlockNumberBytes = ByteUtils.prependZeros(openBlockNumber.toByteArray(), 4);
    byte[] balanceBytes = ByteUtils.prependZeros(Hex.decode(prependZeroIfNeeded(owedBalance)), 24);

    byte[] dataTypeName =
        "string message_idaddress receiveruint32 block_createduint192 balanceaddress contract".getBytes(
            Charset.forName("UTF-8"));
    byte[] dataValue =
        ByteUtils.concat("Sender balance proof signature".getBytes(), receiverAddressBytes,
            openBlockNumberBytes, balanceBytes, channelAddressBytes);

    return HashUtil.sha3(ByteUtils.concat(HashUtil.sha3(dataTypeName), HashUtil.sha3(dataValue)));
  }

  private String prependZeroIfNeeded(BigInteger balance) {
    String s = balance.toString(16);
    return s.length() % 2 == 0 ? s : '0' + s;
  }
}
