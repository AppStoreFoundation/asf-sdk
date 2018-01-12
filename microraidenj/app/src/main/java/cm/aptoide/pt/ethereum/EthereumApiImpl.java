package cm.aptoide.pt.ethereum;

import android.support.annotation.NonNull;
import cm.aptoide.pt.ethereum.dependencies.RetrofitModule;
import cm.aptoide.pt.ethereum.ethereumj.Transaction;
import cm.aptoide.pt.ethereum.ethereumj.crypto.ECKey;
import cm.aptoide.pt.ethereum.ethereumj.util.ByteUtil;
import cm.aptoide.pt.ethereum.ws.ApiFactory;
import cm.aptoide.pt.ethereum.ws.Network;
import cm.aptoide.pt.ethereum.ws.WebServiceFactory;
import cm.aptoide.pt.ethereum.ws.etherscan.BalanceResponse;
import cm.aptoide.pt.ethereum.ws.etherscan.EtherscanApi;
import cm.aptoide.pt.ethereum.ws.etherscan.TransactionResultResponse;
import java.math.BigDecimal;
import java.math.MathContext;
import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.datatypes.Address;
import org.web3j.utils.Convert.Unit;
import rx.Observable;

public class EthereumApiImpl implements EthereumApi {

  private final EtherscanApi etherscanApi;
  private final ContractTransactionFactory contractTransactionFactory;

  public EthereumApiImpl() {
    this(Network.MAINNET);
  }

  public EthereumApiImpl(Network network) {
    RetrofitModule retrofitModule = new RetrofitModule();
    ApiFactory apiFactory = new ApiFactory(
        new WebServiceFactory(retrofitModule.provideOkHttpClient(),
            retrofitModule.provideConverterFactory(),
            retrofitModule.provideRxJavaCallAdapterFactory()));
    this.etherscanApi = apiFactory.createEtherscanApi(network);
    this.contractTransactionFactory = new ContractTransactionFactory();
  }

  @Override public Observable<Integer> getCurrentNonce(String address) {
    return etherscanApi.getTransactionCount(address)
        .map(transactionCountResponse -> transactionCountResponse.result)
        .map(result -> Integer.parseInt(HexUtils.fromPrefixString(result), 16));
  }

  @Override public Observable<TransactionResultResponse> sendRawTransaction(byte[] rawData) {
    return etherscanApi.sendRawTransaction(Hex.toHexString(rawData));
  }

  @Override public Observable<TransactionResultResponse> call(int nonce, ECKey ecKey, long gasPrice,
      long gasLimit, Address contractAddress, byte[] data) {
    Transaction transaction =
        contractTransactionFactory.createTransaction(nonce, preProcessAddress(contractAddress),
            data, 4, gasPrice, gasLimit);
    transaction.sign(ecKey);
    return sendRawTransaction(transaction.getEncoded());
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

  @Override public Observable<TransactionResultResponse> send(Address receiver, BigDecimal amount,
      ECKey ecKey, long gasPrice, long gasLimit) {
    return getCurrentNonce(Hex.toHexString(ecKey.getAddress())).map(
        nonce -> contractTransactionFactory.createTransaction(nonce, preProcessAddress(receiver),
            ByteUtil.EMPTY_BYTE_ARRAY, 4, gasPrice, gasLimit))
        .doOnNext(transaction -> transaction.sign(ecKey))
        .flatMap(transaction -> etherscanApi.sendRawTransaction(
            Hex.toHexString(transaction.getEncoded())));
  }

  @NonNull private String preProcessAddress(Address contractAddress) {
    return contractAddress.getValue()
        .substring(2, contractAddress.getValue()
            .length());
  }

  private long etherToWei(BigDecimal amount) {
    return amount.multiply(Unit.ETHER.getWeiFactor(), MathContext.DECIMAL128)
        .longValueExact();
  }
}
