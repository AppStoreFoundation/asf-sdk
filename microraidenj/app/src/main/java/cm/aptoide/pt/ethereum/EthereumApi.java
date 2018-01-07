package cm.aptoide.pt.ethereum;

import cm.aptoide.pt.ethereum.ethereumj.crypto.ECKey;
import cm.aptoide.pt.ethereum.ethereumj.solidity.contract.DeployedContract;
import cm.aptoide.pt.ethereum.ws.etherscan.BalanceResponse;
import cm.aptoide.pt.ethereum.ws.etherscan.TransactionResultResponse;
import rx.Observable;

public interface EthereumApi {

  Observable<Long> getCurrentNonce(String address);

  Observable<TransactionResultResponse> sendRawTransaction(String rawData);

  Observable<TransactionResultResponse> call(int nonce, DeployedContract contract, ECKey ecKey,
      long gasPrice, long gasLimit);

  Observable<BalanceResponse> getBalance(String address);

  Observable<BalanceResponse> getTokenBalance(String contractAddress, String address);

  Observable<Boolean> isTransactionAccepted(String txhash);
}
