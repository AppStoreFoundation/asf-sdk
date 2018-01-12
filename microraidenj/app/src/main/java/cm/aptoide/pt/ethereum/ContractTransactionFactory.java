package cm.aptoide.pt.ethereum;

import cm.aptoide.pt.ethereum.ethereumj.Transaction;

public class ContractTransactionFactory {

  private final TransactionFactory transactionFactory;

  public ContractTransactionFactory() {
    transactionFactory = new TransactionFactory();
  }

  public Transaction createTransaction(int nonce, String contractAddress, byte[] encodedCall,
      int chainIdForNextBlock, long gasPrice, long gasLimit) {
    return this.transactionFactory.createTransaction(nonce, contractAddress, 0, encodedCall,
        gasPrice, gasLimit, chainIdForNextBlock);
  }
}
