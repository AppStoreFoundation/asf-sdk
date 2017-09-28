package cm.aptoide.pt.contract;

import org.ethereum.core.CallTransaction;
import org.ethereum.core.TransactionReceipt;
import org.ethereum.facade.Ethereum;
import org.ethereum.solidity.compiler.SolidityCompiler;
import org.ethereum.vm.program.ProgramResult;
import org.slf4j.Logger;
import org.spongycastle.util.encoders.Hex;

import cm.aptoide.pt.TransactionManager;

public class CoffeeMachineContract extends SolidityContractWrapper {

	private static final String CONTRACT_PATH = "ethereumj-core/src/main/resources/coffee_token.sol";

	public CoffeeMachineContract(Logger logger, Ethereum ethereum, SolidityCompiler compiler,
															 TransactionManager transactionManager) {
		super(logger, ethereum, compiler, transactionManager, CONTRACT_PATH);
	}

	public void createAccount(byte[] address, byte[] contractAddress, byte[] senderPrivateKey) {
		transfer(address, contractAddress, senderPrivateKey, 100);
	}

	public void transfer(byte[] address, byte[] contractAddress, byte[] senderPrivateKey, int
					value) {
		try {
			byte[] functionCallBytes = getFuncion("transfer").encode(address, value);
			TransactionReceipt receipt = transactionManager.sendTxAndWait(contractAddress,
							functionCallBytes, senderPrivateKey);
			confirmReceipt(receipt);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean buyCoffee(byte[] contractAddress, byte[] senderPrivateKey) {
		try {

			byte[] functionCallBytes = getFuncion("buyCoffee").encode();
			TransactionReceipt receipt = transactionManager.sendTxAndWait(contractAddress,
							functionCallBytes, senderPrivateKey);
			confirmReceipt(receipt);
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void balanceOf(byte[] contractAddress, byte[] addressOwner) {

		CallTransaction.Function balanceOf = getFuncion("balanceOf");
		logger.info("Calling the contract function 'balanceOf'");
		ProgramResult r = ethereum.callConstantFunction(Hex.toHexString(contractAddress), balanceOf,
						addressOwner);
		Object[] ret = balanceOf.decodeResult(r.getHReturn());
		logger.info("Buyers address balance: " + ret[0]);
	}
}
