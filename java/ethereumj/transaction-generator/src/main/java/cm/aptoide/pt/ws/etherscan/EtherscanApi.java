package cm.aptoide.pt.ws.etherscan;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface EtherscanApi {

	@GET("api?module=account&action=tokenbalance")
	Observable<TokenBalanceResponse> getTokenBalance(@Query("contractaddress") String
																													 contractaddress, @Query("address")
					String address);
}
