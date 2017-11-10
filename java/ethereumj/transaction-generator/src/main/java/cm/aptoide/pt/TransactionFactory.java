package cm.aptoide.pt;

import cm.aptoide.pt.ethereumj.Transaction;
import cm.aptoide.pt.ethereumj.util.ByteUtil;

public class TransactionFactory {

	private static final long gasPrice = 24_000_000_000L;
	private static final long gasLimit = 0xfffff;

	public Transaction createTransaction(byte[] nonce, byte[] gasPrice, byte[] gasLimit, byte[]
					receiverAddr, byte[] value, byte[] data, int chainIdForNextBlock) {
		return new Transaction(nonce, gasPrice, gasLimit, receiverAddr, value, data,
						chainIdForNextBlock);
	}

	public Transaction createTransaction(int nonce, String receiverAddr, int value, byte[] data, int
					chainIdForNextBlock) {
		return createTransaction(ByteUtil.intToBytesNoLeadZeroes(nonce),
						ByteUtil.longToBytesNoLeadZeroes(gasPrice), ByteUtil.longToBytesNoLeadZeroes(gasLimit),
						HexUtils.decode(receiverAddr), HexUtils.decode(Long.toHexString(value)), data,
						chainIdForNextBlock);
	}
}
