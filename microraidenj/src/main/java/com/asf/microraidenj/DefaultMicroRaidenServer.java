package com.asf.microraidenj;

import com.asf.microraidenj.contract.MicroRaidenContract;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import com.asf.microraidenj.util.ByteUtils;
import ethereumj.crypto.ECKey;
import ethereumj.crypto.HashUtil;
import java.math.BigInteger;
import java.nio.charset.Charset;
import org.spongycastle.util.encoders.Hex;

public final class DefaultMicroRaidenServer implements MicroRaidenServer {

  private final Address channelManagerAddr;

  private final MicroRaidenContract microRaidenContract;

  public DefaultMicroRaidenServer(Address channelManagerAddr,
      MicroRaidenContract microRaidenContract) {
    this.channelManagerAddr = channelManagerAddr;
    this.microRaidenContract = microRaidenContract;
  }

  @Override public byte[] createClosingMessage(ECKey receiverECKey, Address senderAddress,
      BigInteger openBlockNum, BigInteger owedBalance) {
    return createClosingMsgHash(senderAddress, openBlockNum, owedBalance, receiverECKey,
        channelManagerAddr);
  }

  @Override public String closeChannelCooperatively(Address senderAddress, ECKey receiverECKey,
      BigInteger openBlockNum, BigInteger owedBalance, byte[] balanceMsgSigned, ECKey ecKey)
      throws TransactionFailedException {

    Address receiverAddress = Address.from(receiverECKey.getAddress());

    return microRaidenContract.callCooperativeClose(ecKey, receiverAddress, openBlockNum,
        owedBalance, balanceMsgSigned,
        createClosingMsgHash(senderAddress, openBlockNum, owedBalance, receiverECKey,
            channelManagerAddr));
  }

  private byte[] createClosingMsgHashRaw(Address senderAddress, BigInteger openBlockNumber,
      BigInteger owedBalance, Address channelManagerAddr) {
    byte[] receiverAddressBytes = senderAddress.getBytes();
    byte[] channelAddressBytes = channelManagerAddr.getBytes();

    byte[] openBlockNumberBytes = ByteUtils.prependZeros(openBlockNumber.toByteArray(), 4);
    byte[] balanceBytes = ByteUtils.prependZeros(Hex.decode(prependZeroIfNeeded(owedBalance)), 24);

    byte[] dataTypeName =
        "string message_idaddress senderuint32 block_createduint192 balanceaddress contract".getBytes(
            Charset.forName("UTF-8"));
    byte[] dataValue =
        ByteUtils.concat("Receiver closing signature".getBytes(), receiverAddressBytes,
            openBlockNumberBytes, balanceBytes, channelAddressBytes);

    return HashUtil.sha3(ByteUtils.concat(HashUtil.sha3(dataTypeName), HashUtil.sha3(dataValue)));
  }

  private byte[] createClosingMsgHash(Address senderAddress, BigInteger openBlockNumber,
      BigInteger owedBalance, ECKey receiverECKey, Address channelManagerAddr) {
    byte[] closingMsgHash =
        createClosingMsgHashRaw(senderAddress, openBlockNumber, owedBalance, channelManagerAddr);

    return receiverECKey.sign(closingMsgHash)
        .toByteArray();
  }

  private String prependZeroIfNeeded(BigInteger balance) {
    String s = balance.toString(16);
    return s.length() % 2 == 0 ? s : '0' + s;
  }
}
