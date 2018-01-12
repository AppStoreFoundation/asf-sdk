package cm.aptoide.pt.ethereum;

import cm.aptoide.pt.ethereum.ethereumj.Transaction;
import cm.aptoide.pt.ethereum.ethereumj.util.ByteUtil;
import org.spongycastle.util.encoders.Hex;

import static cm.aptoide.pt.ethereum.ethereumj.util.ByteUtil.longToBytesNoLeadZeroes;

public class TransactionFactory {

  public Transaction createTransaction(byte[] nonce, byte[] gasPrice, byte[] gasLimit,
      byte[] receiverAddr, byte[] value, byte[] data, int chainIdForNextBlock) {
    return new Transaction(nonce, gasPrice, gasLimit, receiverAddr, value, data,
        chainIdForNextBlock);
  }

  public Transaction createTransaction(long nonce, long gasPrice, long gasLimit, String toAddress,
      long value, byte[] data, int chainId) {
    Transaction tx =
        new Transaction(longToBytesNoLeadZeroes(nonce), longToBytesNoLeadZeroes(gasPrice),
            longToBytesNoLeadZeroes(gasLimit), toAddress == null ? null : Hex.decode(toAddress),
            longToBytesNoLeadZeroes(value), data, chainId);
    return tx;
  }

  public Transaction createTransaction(int nonce, String receiverAddr, long value, byte[] data,
      long gasPrice, long gasLimit, int chainId) {
    return this.createTransaction(nonce, gasPrice, gasLimit, receiverAddr, value, data, chainId);
  }

  public Transaction createTransaction(int nonce, String receiverAddr, long value, long gasPrice,
      long gasLimit, int chainId) {
    return this.createTransaction(nonce, gasPrice, gasLimit, receiverAddr, value,
        ByteUtil.EMPTY_BYTE_ARRAY, chainId);
  }
}
