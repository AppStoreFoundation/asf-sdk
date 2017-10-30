package cm.aptoide.pt;

import cm.aptoide.pt.ethereumj.Transaction;

public class ContractTransactionFactory {

	private final TransactionFactory transactionFactory;

	public ContractTransactionFactory() {
		this.transactionFactory = new TransactionFactory();
	}

	public Transaction createTransaction(int nonce, String contractAddress, byte[] encodedCall, int
					chainIdForNextBlock) {
		return transactionFactory.createTransaction(nonce, contractAddress, 0, encodedCall,
						chainIdForNextBlock);
	}
}
