package cm.aptoide.pt.ethereum;

import cm.aptoide.pt.ethereum.ethereumj.Transaction;
import cm.aptoide.pt.ethereum.ethereumj.util.ByteUtil;

public class TransactionFactory {

  public Transaction createTransaction(byte[] nonce, byte[] gasPrice, byte[] gasLimit,
      byte[] receiverAddr, byte[] value, byte[] data, int chainIdForNextBlock) {
    return new Transaction(nonce, gasPrice, gasLimit, receiverAddr, value, data,
        chainIdForNextBlock);
  }

  public Transaction createTransaction(int nonce, String receiverAddr, int value, byte[] data,
      int chainIdForNextBlock, long gasPrice, long gasLimit) {
    return this.createTransaction(ByteUtil.intToBytesNoLeadZeroes(nonce),
        ByteUtil.longToBytesNoLeadZeroes(gasPrice), ByteUtil.longToBytesNoLeadZeroes(gasLimit),
        HexUtils.decode(receiverAddr), HexUtils.decode(Long.toHexString(value)), data,
        chainIdForNextBlock);
  }

  public Transaction createTransaction(int nonce, String receiverAddr, int value,
      int chainIdForNextBlock, long gasPrice, long gasLimit) {
    return this.createTransaction(ByteUtil.intToBytesNoLeadZeroes(nonce),
        ByteUtil.longToBytesNoLeadZeroes(gasPrice), ByteUtil.longToBytesNoLeadZeroes(gasLimit),
        HexUtils.decode(receiverAddr), HexUtils.decode(Long.toHexString(value)), new byte[0],
        chainIdForNextBlock);
  }
}
