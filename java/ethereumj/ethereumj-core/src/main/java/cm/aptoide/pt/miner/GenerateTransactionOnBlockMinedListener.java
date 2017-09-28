package cm.aptoide.pt.miner;

import org.ethereum.core.Block;
import org.ethereum.core.Transaction;
import org.ethereum.crypto.ECKey;
import org.ethereum.facade.Ethereum;
import org.ethereum.mine.MinerListener;
import org.ethereum.util.ByteUtil;
import org.slf4j.Logger;

import cm.aptoide.pt.AptoideAccounts;

public class GenerateTransactionOnBlockMinedListener implements MinerListener {

	private final Logger logger;
	private final Ethereum ethereum;
	private AptoideAccounts.Account source;
	private AptoideAccounts.Account dest;

	public GenerateTransactionOnBlockMinedListener(Logger logger, Ethereum ethereum, AptoideAccounts
					.Account source, AptoideAccounts.Account dest) {
		this.logger = logger;
		this.ethereum = ethereum;
		this.source = source;
		this.dest = dest;
	}

	@Override
	public void miningStarted() {
		logger.info("Miner started");
	}

	@Override
	public void miningStopped() {
		logger.info("Miner stopped");
	}

	@Override
	public void blockMiningStarted(Block block) {
		logger.info("Start mining block: " + block.getShortDescr());
	}

	@Override
	public void blockMined(Block block) {
		logger.info("Block mined! : \n" + block);
		try {
			generateTransactions();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void blockMiningCanceled(Block block) {
		logger.info("Cancel mining block: " + block.getShortDescr());
	}

	private void generateTransactions() throws Exception {
		logger.info("Start generating transactions...");

		// the sender which some coins from the genesis
		ECKey senderKey = ECKey.fromPrivate(source.getPrivateKey());
		byte[] receiverAddr = dest.getAddress();

		int nonce = ethereum.getRepository()
						.getNonce(senderKey.getAddress())
						.intValue();

		Transaction tx = new Transaction(ByteUtil.intToBytesNoLeadZeroes(nonce),
						ByteUtil.longToBytesNoLeadZeroes(50_000_000_000L),
						ByteUtil.longToBytesNoLeadZeroes(0xfffff), receiverAddr, new byte[]{77}, new byte[0],
						ethereum.getChainIdForNextBlock());
		tx.sign(senderKey);
		logger.info("<== Submitting tx: " + tx);
		ethereum.submitTransaction(tx);
	}
}
