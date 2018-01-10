package cm.aptoide.pt.microraidenj;

import cm.aptoide.pt.ethereum.EthereumApi;
import cm.aptoide.pt.ethereum.ethereumj.core.CallTransaction.Function;
import cm.aptoide.pt.ethereum.ethereumj.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint192;

/**
 * Created by neuro on 06-01-2018.
 */
public class ChannelManager {

  private static final Function createChannelFunction =
      Function.fromSignature("createChannel", "address", "uint192");

  private final long gasPrice;
  private final long gasLimit;
  private final Address contractAddress;
  private final EthereumApi ethereumApi;

  public ChannelManager(long gasPrice, long gasLimit, Address contractAddress,
      EthereumApi ethereumApi) {
    this.gasPrice = gasPrice;
    this.gasLimit = gasLimit;
    this.contractAddress = contractAddress;
    this.ethereumApi = ethereumApi;
  }

  /**
   * Creates a new channel between `sender` and `receiver` and transfers the `deposit` token deposit
   * to this contract, compatibility with ERC20 tokens.
   *
   * @param ecKey ecKey used to sign and send the transaction.
   * @param receiver The address that receives tokens.
   * @param deposit The amount of tokens that the sender escrows.
   */
  void createChannel(ECKey ecKey, Address receiver, Uint192 deposit) {
    String senderAddress = Hex.toHexString(ecKey.getAddress());

    ethereumApi.getCurrentNonce(senderAddress)
        .flatMap(nonce -> ethereumApi.call(nonce, ecKey, gasPrice, gasLimit, contractAddress,
            encodeCreateChannelMethod(receiver, deposit)))
        .toBlocking()
        .first();

    waitForChannelCreation();
  }

  private byte[] encodeCreateChannelMethod(Address receiver, Uint192 deposit) {
    return createChannelFunction.encode(receiver, deposit);
  }

  private void waitForChannelCreation() {
    // TODO: 06-01-2018 neuro 
  }
}
