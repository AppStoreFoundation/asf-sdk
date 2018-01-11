package cm.aptoide.pt.ethereum;

import cm.aptoide.pt.ethereum.ethereumj.crypto.ECKey;
import cm.aptoide.pt.ethereum.ws.etherscan.BalanceResponse;
import cm.aptoide.pt.ethereum.ws.etherscan.TransactionResultResponse;
import org.web3j.abi.datatypes.Address;
import rx.Observable;

public interface EthereumApi {

  Observable<Integer> getCurrentNonce(String address);

  Observable<TransactionResultResponse> sendRawTransaction(byte[] rawData);

  Observable<TransactionResultResponse> call(int nonce, ECKey ecKey, long gasPrice, long gasLimit,
      Address contractAddress, byte[] data);

  Observable<BalanceResponse> getBalance(String address);

  Observable<BalanceResponse> getTokenBalance(String contractAddress, String address);

  Observable<Boolean> isTransactionAccepted(String txhash);
}
