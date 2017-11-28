package cm.aptoide.pt;

import cm.aptoide.pt.dependencies.module.RetrofitModule;
import cm.aptoide.pt.erc20.Erc20;
import cm.aptoide.pt.ethereumj.Transaction;
import cm.aptoide.pt.ethereumj.crypto.ECKey;
import cm.aptoide.pt.ws.ApiFactory;
import cm.aptoide.pt.ws.Network;
import cm.aptoide.pt.ws.WebServiceFactory;
import cm.aptoide.pt.ws.etherscan.*;
import org.spongycastle.util.encoders.Hex;
import rx.Observable;
import rx.functions.Func1;

class EthereumApiImpl implements EthereumApi {

	private final EtherscanApi etherscanApi;
	private final ContractTransactionFactory contractTransactionFactory;

	EthereumApiImpl() {
		RetrofitModule retrofitModule = new RetrofitModule();
		ApiFactory apiFactory = new ApiFactory(
						new WebServiceFactory(retrofitModule.provideOkHttpClient(),
										retrofitModule.provideConverterFactory(),
										retrofitModule.provideRxJavaCallAdapterFactory()));
		this.etherscanApi = apiFactory.createEtherscanApi(Network.MAINNET);
		contractTransactionFactory = new ContractTransactionFactory();
	}

	@Override
	public Observable<Long> getCurrentNonce(String address) {
		return etherscanApi.getTransactionCount(address)
						.map(new Func1<TransactionCountResponse, String>() {
							@Override
							public String call(TransactionCountResponse transactionCountResponse) {
								return transactionCountResponse.result;
							}
						})
						.map(new Func1<String, Long>() {
							@Override
							public Long call(String s) {
								return Long.parseLong(HexUtils.fromPrefixString(s), 16);
							}
						});
	}

	@Override
	public Observable<TransactionResultResponse> sendRawTransaction(String rawData) {
		return etherscanApi.sendRawTransaction(rawData);
	}

	@Override
	public Observable<TransactionResultResponse> call(int nonce, String contractAddress, Erc20
			erc20, ECKey ecKey, long gasPrice, long gasLimit) {
		Transaction transaction = contractTransactionFactory.createTransaction(nonce,
				contractAddress, erc20.encode(), 1, gasPrice, gasLimit);
		transaction.sign(ecKey);
		return sendRawTransaction(Hex.toHexString(transaction.getEncoded()));
	}

	@Override
	public Observable<BalanceResponse> getBalance(String address) {
		return etherscanApi.getBalance(address);
	}

	@Override
	public Observable<BalanceResponse> getTokenBalance(String contractAddress, String address) {
		return etherscanApi.getTokenBalance(contractAddress, address);
	}

	@Override
	public Observable<Boolean> isTransactionAccepted(String txhash) {
		return etherscanApi.getTransactionByHash(txhash)
						.map(new Func1<TransactionByHashResponse, Boolean>() {
							@Override
							public Boolean call(TransactionByHashResponse transactionByHashResponse) {
								return transactionByHashResponse.result.blockNumber != null;
							}
						});
	}
}
