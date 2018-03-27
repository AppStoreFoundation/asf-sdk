package cm.aptoide.pt.ethereum;

import cm.aptoide.pt.ethereum.dependencies.RetrofitModule;
import cm.aptoide.pt.ethereum.ws.ApiFactory;
import cm.aptoide.pt.ethereum.ws.Network;
import cm.aptoide.pt.ethereum.ws.WebServiceFactory;
import cm.aptoide.pt.ethereum.ws.etherscan.BalanceResponse;
import cm.aptoide.pt.ethereum.ws.etherscan.EtherscanApi;
import cm.aptoide.pt.ethereum.ws.etherscan.TransactionByHashResponse;
import cm.aptoide.pt.ethereum.ws.etherscan.TransactionCountResponse;
import cm.aptoide.pt.ethereum.ws.etherscan.TransactionResultResponse;
import cm.aptoide.pt.ethereumj.Transaction;
import cm.aptoide.pt.ethereumj.crypto.ECKey;
import cm.aptoide.pt.web3j.abi.datatypes.Address;
import java.math.BigDecimal;
import org.spongycastle.util.encoders.Hex;
import rx.Observable;
import rx.functions.Func1;

class EthereumApiImpl implements EthereumApi {

	private final EtherscanApi etherscanApi;
	private final ContractTransactionFactory contractTransactionFactory;

	EthereumApiImpl(Network mainnet) {
		RetrofitModule retrofitModule = new RetrofitModule();
		ApiFactory apiFactory = new ApiFactory(
						new WebServiceFactory(retrofitModule.provideOkHttpClient(),
										retrofitModule.provideConverterFactory(),
										retrofitModule.provideRxJavaCallAdapterFactory()));
		this.etherscanApi = apiFactory.createEtherscanApi(mainnet);
		contractTransactionFactory = new ContractTransactionFactory();
	}

	@Override
	public Observable<Long> getCurrentNonce(String address) {
		return etherscanApi.getTransactionCount(address)
						.map(new Func1<TransactionCountResponse, String>() {
							@Override
							public String call(
									TransactionCountResponse transactionCountResponse) {
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

	@Override public Observable<TransactionResultResponse> sendRawTransaction(byte[] rawData) {
		return etherscanApi.sendRawTransaction(Hex.toHexString(rawData));
	}

	@Override public Observable<TransactionResultResponse> call(int nonce, ECKey ecKey, long gasPrice,
			long gasLimit, Address contractAddress, byte[] data) {
		Transaction transaction =
				contractTransactionFactory.createTransaction(nonce, contractAddress.getValue(), data, 1,
						gasPrice, gasLimit);
		transaction.sign(ecKey);
		return sendRawTransaction(transaction.getEncoded());
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
							public Boolean call(
									TransactionByHashResponse transactionByHashResponse) {
								return transactionByHashResponse.result.blockNumber != null;
							}
						});
	}

	@Override public Observable<TransactionResultResponse> send(Address receiver, BigDecimal amount,
			ECKey ecKey, long gasPrice, long gasLimit) {
		return null;
	}
}
