package cm.aptoide.pt.ethereum.example;

import cm.aptoide.pt.ethereum.EthereumApi;
import cm.aptoide.pt.ethereum.EthereumApiFactory;
import cm.aptoide.pt.ethereum.dependencies.RetrofitModule;
import cm.aptoide.pt.ethereum.erc20.Erc20Transfer;
import cm.aptoide.pt.ethereum.ethereumj.crypto.ECKey;
import cm.aptoide.pt.ethereum.ws.ApiFactory;
import cm.aptoide.pt.ethereum.ws.Network;
import cm.aptoide.pt.ethereum.ws.WebServiceFactory;
import cm.aptoide.pt.ethereum.ws.etherscan.BalanceResponse;
import cm.aptoide.pt.ethereum.ws.etherscan.EtherscanApi;
import cm.aptoide.pt.ethereum.ws.etherscan.TransactionResultResponse;
import org.spongycastle.util.encoders.Hex;

public class Example {

  private static final long gasPrice = 24_000_000_000L;
  private static final long gasLimit = 0xfffff;

  private static final String CONTRACT_ADDRESS = "8dbf4349cbeca08a02cc6b5b0862f9dd42c585b9";
  private static final String RECEIVER_ADDR = "62a5c1680554A61334F5c6f6D7dA6044b6AFbFe8";

  private final EthereumApi ethereumApi;

  public Example() {
    this.ethereumApi = EthereumApiFactory.createEthereumApi();
  }

  public static void main(String[] args) {
    new Example().run();
  }

  private void run() {
    RetrofitModule retrofitModule = new RetrofitModule();

    ApiFactory apiFactory = new ApiFactory(
        new WebServiceFactory(retrofitModule.provideOkHttpClient(),
            retrofitModule.provideConverterFactory(),
            retrofitModule.provideRxJavaCallAdapterFactory()));

    EtherscanApi etherscanApi = apiFactory.createEtherscanApi(Network.ROPSTEN);

    ECKey senderKey = ECKey.fromPrivate(
        Hex.decode("8dd23881d17799de1b1e67dd1da1c36842bf1058109e5048a2593b402a901127"));

    long nonce = this.ethereumApi.getCurrentNonce(Hex.toHexString(senderKey.getAddress()))
        .toBlocking()
        .first();

    TransactionResultResponse call =
        this.ethereumApi.call((int) nonce, CONTRACT_ADDRESS, new Erc20Transfer(RECEIVER_ADDR, 1),
            senderKey, gasPrice, gasLimit)
            .toBlocking()
            .first();

    System.out.println(call);
    String txHash = call.result;

    BalanceResponse balance = this.ethereumApi.getBalance(RECEIVER_ADDR)
        .toBlocking()
        .first();
    BalanceResponse tokenBalance = this.ethereumApi.getTokenBalance(CONTRACT_ADDRESS, RECEIVER_ADDR)
        .toBlocking()
        .first();

    Boolean accepted = this.ethereumApi.isTransactionAccepted(txHash)
        .toBlocking()
        .first();

    System.out.println("Balance: " + balance);
    System.out.println("Token Balance: " + tokenBalance);
    System.out.println(accepted);
  }
}
