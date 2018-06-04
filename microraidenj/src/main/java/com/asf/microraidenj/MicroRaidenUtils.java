package com.asf.microraidenj;

import com.asf.microraidenj.type.Address;
import com.asf.microraidenj.util.ByteUtils;
import ethereumj.crypto.ECKey;
import ethereumj.crypto.HashUtil;
import java.math.BigInteger;
import java.nio.charset.Charset;
import org.spongycastle.util.encoders.Hex;

public final class MicroRaidenUtils {

  private static byte[] getClosingMsgHashRaw(Address senderAddress, BigInteger openBlockNumber,
      BigInteger owedBalance, Address channelManagerAddr) {
    byte[] receiverAddressBytes = senderAddress.getBytes();
    byte[] channelAddressBytes = channelManagerAddr.getBytes();

    byte[] openBlockNumberBytes = ByteUtils.prependZeros(openBlockNumber.toByteArray(), 4);
    byte[] balanceBytes = ByteUtils.prependZeros(Hex.decode(prependZerosIfNeeded(owedBalance)), 24);

    byte[] dataTypeName =
        "string message_idaddress senderuint32 block_createduint192 balanceaddress contract".getBytes(
            Charset.forName("UTF-8"));
    byte[] dataValue =
        ByteUtils.concat("Receiver closing signature".getBytes(), receiverAddressBytes,
            openBlockNumberBytes, balanceBytes, channelAddressBytes);

    return HashUtil.sha3(ByteUtils.concat(HashUtil.sha3(dataTypeName), HashUtil.sha3(dataValue)));
  }

  public static byte[] createClosingMsgHash(Address senderAddress, BigInteger openBlockNumber,
      BigInteger owedBalance, ECKey receiverECKey, Address channelManagerAddr) {
    byte[] closingMsgHash =
        getClosingMsgHashRaw(senderAddress, openBlockNumber, owedBalance, channelManagerAddr);

    return receiverECKey.sign(closingMsgHash)
        .toByteArray();
  }

  private static byte[] getBalanceMsgHashRaw(Address receiverAddress, BigInteger openBlockNumber,
      BigInteger owedBalance, Address channelManagerAddr) {
    byte[] receiverAddressBytes = receiverAddress.getBytes();
    byte[] channelAddressBytes = channelManagerAddr.getBytes();

    byte[] openBlockNumberBytes = ByteUtils.prependZeros(openBlockNumber.toByteArray(), 4);
    byte[] balanceBytes = ByteUtils.prependZeros(Hex.decode(prependZerosIfNeeded(owedBalance)), 24);

    byte[] dataTypeName =
        "string message_idaddress receiveruint32 block_createduint192 balanceaddress contract".getBytes(
            Charset.forName("UTF-8"));
    byte[] dataValue =
        ByteUtils.concat("Sender balance proof signature".getBytes(), receiverAddressBytes,
            openBlockNumberBytes, balanceBytes, channelAddressBytes);

    return HashUtil.sha3(ByteUtils.concat(HashUtil.sha3(dataTypeName), HashUtil.sha3(dataValue)));
  }

  public static byte[] createBalanceMsgHash(Address receiverAddress, BigInteger openBlockNumber,
      BigInteger owedBalance, ECKey senderECKey, Address channelManagerAddr) {
    byte[] balanceMsgHash =
        getBalanceMsgHashRaw(receiverAddress, openBlockNumber, owedBalance, channelManagerAddr);

    return senderECKey.sign(balanceMsgHash)
        .toByteArray();
  }

  private static String prependZerosIfNeeded(BigInteger balance) {
    String s = balance.toString(16);
    return s.length() % 2 == 0 ? s : '0' + s;
  }
}
