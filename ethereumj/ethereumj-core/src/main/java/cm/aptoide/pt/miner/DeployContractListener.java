package cm.aptoide.pt.miner;

import org.ethereum.core.Block;
import org.ethereum.facade.Ethereum;
import org.ethereum.mine.MinerListener;
import org.ethereum.solidity.compiler.SolidityCompiler;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.prefs.Preferences;

import cm.aptoide.pt.TransactionManager;
import cm.aptoide.pt.contract.CoffeeMachineContract;

public class DeployContractListener implements MinerListener {

	private static final String CONTRACT_ADDRESS = "ContractAddress";

	private final Logger logger;
	private final Ethereum ethereum;
	private final SolidityCompiler compiler;
	private final TransactionManager transactionManager;
	private final byte[] privateKey;

	public DeployContractListener(Logger logger, Ethereum ethereum, SolidityCompiler compiler,
																TransactionManager transactionManager, byte[] privateKey) {
		this.logger = logger;
		this.ethereum = ethereum;
		this.compiler = compiler;
		this.transactionManager = transactionManager;
		this.privateKey = privateKey;
	}

	@Override
	public void miningStarted() {
		if (getContractAddress() == null || ethereum.getBlockchain()
						.getBestBlock()
						.getNumber() == 0) {
			try {
				CoffeeMachineContract coffeeMachineContract = new CoffeeMachineContract(logger, ethereum,
								compiler, transactionManager);
				coffeeMachineContract.compile();
				setContractAddress(coffeeMachineContract.deploy(privateKey));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void miningStopped() {
	}

	@Override
	public void blockMiningStarted(Block block) {
	}

	@Override
	public void blockMined(Block block) {
	}

	@Override
	public void blockMiningCanceled(Block block) {
	}

	private byte[] getContractAddress() {
		return Preferences.userRoot()
						.getByteArray(CONTRACT_ADDRESS, null);
	}

	private void setContractAddress(byte[] addr) {
		Preferences.userRoot()
						.putByteArray(CONTRACT_ADDRESS, addr);
	}
}
