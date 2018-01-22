package cm.aptoide.pt.microraidenj;

import cm.aptoide.pt.ethereum.EthereumApi;
import cm.aptoide.pt.ethereum.ethereumj.core.CallTransaction.Function;
import cm.aptoide.pt.ethereum.ethereumj.crypto.ECKey;
import cm.aptoide.pt.ethereum.ws.etherscan.TransactionResultResponse;
import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint192;
import org.web3j.abi.datatypes.generated.Uint32;

/**
 * Created by neuro on 06-01-2018.
 */
public class ChannelManager {

  private static final Function createChannelFunction =
      Function.fromSignature("createChannel", "address", "uint192");
  private static final Function uncooperativeCloseFunction =
      Function.fromSignature("uncooperativeClose", "address", "uint32", "uint192");

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

  private byte[] encodeCreateChannelMethod(Address receiver, Uint192 deposit) {
    return createChannelFunction.encode(receiver.getValue(), deposit.getValue());
  }

  private void waitForChannelCreation() {
    // TODO: 06-01-2018 neuro
  }

  private byte[] encodeUncooperativeCloseMethod(Address receiverAddress, Uint32 openBlockNumber,
      Uint192 balance) {
    return uncooperativeCloseFunction.encode(receiverAddress.getValue(), openBlockNumber.getValue(),
        balance.getValue());
  }

  public void uncooperativeClose(ECKey ecKey, Address receiverAddress, Uint32 openBlockNumber,
      Uint192 balance) {
    String senderAddress = Hex.toHexString(ecKey.getAddress());

    TransactionResultResponse first = ethereumApi.getCurrentNonce(senderAddress)
        .flatMap(nonce -> ethereumApi.call(nonce, ecKey, gasPrice, gasLimit, contractAddress,
            encodeUncooperativeCloseMethod(receiverAddress, openBlockNumber, balance)))
        .toBlocking()
        .first();

    System.out.println(first);
  }
}
