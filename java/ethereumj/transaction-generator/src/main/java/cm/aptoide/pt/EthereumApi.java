package cm.aptoide.pt;

import cm.aptoide.pt.erc20.Erc20;
import cm.aptoide.pt.ethereumj.crypto.ECKey;
import cm.aptoide.pt.ws.etherscan.BalanceResponse;
import cm.aptoide.pt.ws.etherscan.TransactionResultResponse;
import rx.Observable;

public interface EthereumApi {

	Observable<Long> getCurrentNonce(String address);

	Observable<TransactionResultResponse> sendRawTransaction(String rawData);

	Observable<TransactionResultResponse> call(int nonce, String contractAddress, Erc20 erc20, ECKey
			ecKey, long gasPrice, long gasLimit);

	Observable<BalanceResponse> getBalance(String address);

	Observable<BalanceResponse> getTokenBalance(String contractAddress, String address);

	Observable<Boolean> isTransactionAccepted(String txhash);
}
