package cm.aptoide.pt.ethereum;

import cm.aptoide.pt.ethereum.dependencies.RetrofitModule;
import cm.aptoide.pt.ethereum.ethereumj.Transaction;
import cm.aptoide.pt.ethereum.ethereumj.crypto.ECKey;
import cm.aptoide.pt.ethereum.ethereumj.solidity.contract.DeployedContract;
import cm.aptoide.pt.ethereum.ws.ApiFactory;
import cm.aptoide.pt.ethereum.ws.Network;
import cm.aptoide.pt.ethereum.ws.WebServiceFactory;
import cm.aptoide.pt.ethereum.ws.etherscan.BalanceResponse;
import cm.aptoide.pt.ethereum.ws.etherscan.EtherscanApi;
import cm.aptoide.pt.ethereum.ws.etherscan.TransactionResultResponse;
import org.spongycastle.util.encoders.Hex;
import rx.Observable;

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

  @Override public Observable<Integer> getCurrentNonce(String address) {
    return etherscanApi.getTransactionCount(address)
        .map(transactionCountResponse -> transactionCountResponse.result)
        .map(result -> Integer.parseInt(HexUtils.fromPrefixString(result), 16));
  }

  @Override public Observable<TransactionResultResponse> sendRawTransaction(String rawData) {
    return etherscanApi.sendRawTransaction(rawData);
  }

  @Override public Observable<TransactionResultResponse> call(int nonce, DeployedContract contract,
      ECKey ecKey, long gasPrice, long gasLimit) {
    Transaction transaction = contractTransactionFactory.createTransaction(nonce,
        contract.getAddress()
            .getValue(), contract.encode(), 1, gasPrice, gasLimit);
    transaction.sign(ecKey);
    return sendRawTransaction(Hex.toHexString(transaction.getEncoded()));
  }

  @Override public Observable<BalanceResponse> getBalance(String address) {
    return etherscanApi.getBalance(address);
  }

  @Override
  public Observable<BalanceResponse> getTokenBalance(String contractAddress, String address) {
    return etherscanApi.getTokenBalance(contractAddress, address);
  }

  @Override public Observable<Boolean> isTransactionAccepted(String txhash) {
    return etherscanApi.getTransactionByHash(txhash)
        .map(transactionByHashResponse -> transactionByHashResponse.result.blockNumber != null);
  }
}
