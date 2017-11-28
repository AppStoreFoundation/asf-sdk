package cm.aptoide.pt.contract;

import org.ethereum.core.CallTransaction;
import org.ethereum.core.TransactionReceipt;
import org.ethereum.facade.Ethereum;
import org.ethereum.solidity.compiler.CompilationResult;
import org.ethereum.solidity.compiler.SolidityCompiler;
import org.slf4j.Logger;
import org.spongycastle.util.encoders.Hex;

import java.io.IOException;

import cm.aptoide.pt.TransactionManager;

public class SolidityContractWrapper {

	protected final Logger logger;
	protected final Ethereum ethereum;
	protected final SolidityCompiler compiler;
	protected final ContractUtils contractUtils;
	protected final TransactionManager transactionManager;
	private final String contractPath;

	//region Compiled Stuff
	protected CompilationResult.ContractMetadata metadata;
	protected CallTransaction.Contract contract;
	//endregion

	public SolidityContractWrapper(Logger logger, Ethereum ethereum, SolidityCompiler compiler,
																 TransactionManager transactionManager, String contractPath) {
		this.logger = logger;
		this.ethereum = ethereum;
		this.compiler = compiler;
		this.transactionManager = transactionManager;
		this.contractPath = contractPath;
		this.contractUtils = new ContractUtils(logger, compiler);
	}

	public void compile() throws IOException {
		this.metadata = contractUtils.compileContract(contractPath);
		this.contract = new CallTransaction.Contract(metadata.abi);
	}

	protected CallTransaction.Function getFuncion(String functionName) {
		return contract.getByName(functionName);
	}

	protected void confirmReceipt(TransactionReceipt receipt) {
		if (!receipt.isSuccessful()) {
			logger.error("Some troubles invoking the contract: " + receipt.getError());
			return;
		}
		logger.info("Contract modified! called successfully!");
	}

	public byte[] deploy(byte[] senderPrivateKey) {

		assertContractIsCompiled();

		try {
			logger.info("Sending contract to net and waiting for inclusion");
			TransactionReceipt receipt = transactionManager.sendTxAndWait(new byte[0],
							Hex.decode(metadata.bin), senderPrivateKey);

			if (!receipt.isSuccessful()) {
				logger.error("Some troubles creating a contract: " + receipt.getError());
				return senderPrivateKey;
			}

			byte[] contractAddress = receipt.getTransaction()
							.getContractAddress();
			logger.info("Contract created: " + Hex.toHexString(contractAddress));
			return contractAddress;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return senderPrivateKey;
	}

	private void assertContractIsCompiled() {
		if (metadata == null) {
			throw new RuntimeException("Contract not compiled!");
		}
	}
}
