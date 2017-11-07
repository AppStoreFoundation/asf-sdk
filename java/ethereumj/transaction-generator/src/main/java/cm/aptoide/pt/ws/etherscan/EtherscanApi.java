package cm.aptoide.pt.ws.etherscan;

import cm.aptoide.pt.ws.etherscan.transactionreceipt.TransactionReceipt;
import cm.aptoide.pt.ws.etherscan.transactionslist.TransactionsList;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface EtherscanApi {

	@GET("api?module=account&action=tokenbalance&tag=latest")
	Observable<BalanceResponse> getTokenBalance(@Query("contractaddress") String contractaddress,
																							@Query("address") String address);

	@GET("api?module=account&action=balance&tag=latest")
	Observable<BalanceResponse> getBalance(@Query("address") String address);

	@POST("api?module=proxy&action=eth_sendRawTransaction")
	Observable<TransactionResultResponse> sendRawTransaction(@Query("hex") String rawData);

	@GET("api?module=proxy&action=eth_getTransactionCount&tag=latest")
	Observable<TransactionCountResponse> getTransactionCount(@Query("address") String address);

	@GET("api?module=proxy&action=eth_getTransactionByHash")
	Observable<TransactionByHashResponse> getTransactionByHash(@Query("txhash") String txhash);

	@GET("api?module=account&action=txlist&startblock=0&endblock=99999999&sort=asc")
	Observable<TransactionsList> getTransactionsList(@Query("address") String address);

	@GET("https://api.etherscan.io/api?module=proxy&action=eth_getTransactionReceipt")
	Observable<TransactionReceipt> getTransactionReceipt(@Query("txhash") String address);
}
