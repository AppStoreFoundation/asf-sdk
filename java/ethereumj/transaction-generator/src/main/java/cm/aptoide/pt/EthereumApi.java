package cm.aptoide.pt;

import cm.aptoide.pt.erc20.Erc20;
import cm.aptoide.pt.ethereumj.crypto.ECKey;
import cm.aptoide.pt.ws.etherscan.BalanceResponse;
import rx.Observable;

public interface EthereumApi {

	Observable<Long> getCurrentNonce(String address);

	Observable<Object> sendRawTransaction(String rawData);

	Observable<Object> call(int nonce, String contractAddress, Erc20 erc20, ECKey ecKey);

	Observable<BalanceResponse> getBalance(String address);

	Observable<BalanceResponse> getTokenBalance(String contractAddress, String address);
}
