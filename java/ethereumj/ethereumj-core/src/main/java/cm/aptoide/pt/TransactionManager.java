package cm.aptoide.pt;

import org.ethereum.core.Block;
import org.ethereum.core.Transaction;
import org.ethereum.core.TransactionReceipt;
import org.ethereum.crypto.ECKey;
import org.ethereum.db.ByteArrayWrapper;
import org.ethereum.facade.Ethereum;
import org.ethereum.listener.EthereumListenerAdapter;
import org.ethereum.util.ByteUtil;
import org.slf4j.Logger;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionManager {

	private final Ethereum ethereum;
	private final Logger logger;
	private Map<ByteArrayWrapper, TransactionReceipt> txWaiters = Collections.synchronizedMap(
					new HashMap<ByteArrayWrapper, TransactionReceipt>());

	public TransactionManager(Ethereum ethereum, Logger logger) {
		this.ethereum = ethereum;
		this.logger = logger;
	}

	public TransactionReceipt sendTxAndWait(byte[] receiveAddress, byte[] data, byte[]
					senderPrivateKey) throws InterruptedException {
		BigInteger nonce = ethereum.getRepository()
						.getNonce(ECKey.fromPrivate(senderPrivateKey)
										.getAddress());
		Transaction tx = new Transaction(ByteUtil.bigIntegerToBytes(nonce),
						ByteUtil.longToBytesNoLeadZeroes(ethereum.getGasPrice()),
						ByteUtil.longToBytesNoLeadZeroes(3_000_000), receiveAddress,
						ByteUtil.longToBytesNoLeadZeroes(0), data, ethereum.getChainIdForNextBlock());
		tx.sign(ECKey.fromPrivate(senderPrivateKey));
		logger.info("<=== Sending transaction: " + tx);
		ethereum.submitTransaction(tx);
		ethereum.addListener(new EthereumListenerAdapter() {
			@Override
			public void onBlock(Block block, List<TransactionReceipt> receipts) {
				super.onBlock(block, receipts);

				addReceiptIfTxWaiting(receipts);
			}
		});

		return waitForTx(tx.getHash());
	}

	private void addReceiptIfTxWaiting(List<TransactionReceipt> receipts) {
		for (TransactionReceipt receipt : receipts) {
			ByteArrayWrapper txHashW = new ByteArrayWrapper(receipt.getTransaction()
							.getHash());
			if (txWaiters.containsKey(txHashW)) {
				txWaiters.put(txHashW, receipt);
				synchronized (this) {
					notifyAll();
				}
			}
		}
	}

	private TransactionReceipt waitForTx(byte[] txHash) throws InterruptedException {
		ByteArrayWrapper txHashW = new ByteArrayWrapper(txHash);
		txWaiters.put(txHashW, null);
		long startBlock = ethereum.getBlockchain()
						.getBestBlock()
						.getNumber();
		while (true) {
			TransactionReceipt receipt = txWaiters.get(txHashW);
			if (receipt != null) {
				return receipt;
			} else {
				long curBlock = ethereum.getBlockchain()
								.getBestBlock()
								.getNumber();
				if (curBlock > startBlock + 16) {
					throw new RuntimeException(
									"The transaction was not included during last 16 blocks: " + txHashW.toString()
													.substring(0, 8));
				} else {
					logger.info("Current block: " + curBlock + ", Best Block: " + ethereum.getBlockchain()
									.getBestBlock()
									.getNumber());
					logger.info("Waiting for block with transaction 0x" + txHashW.toString()
									.substring(0,
													8) + " included (" + (curBlock - startBlock) + " blocks received so far)" +
									"" + "" + "" + " ...");
				}
			}
			synchronized (this) {
				wait(5000);
			}
		}
	}
}
