package cm.aptoide.pt.microraidenj.example;

import cm.aptoide.pt.ethereum.EthereumApi;
import cm.aptoide.pt.ethereum.EthereumApiFactory;
import cm.aptoide.pt.ethereum.ws.Network;
import cm.aptoide.pt.ethereumj.crypto.ECKey;
import cm.aptoide.pt.microraidenj.ChannelManager;
import cm.aptoide.pt.microraidenj.TokenContract;
import cm.aptoide.pt.web3j.abi.datatypes.Address;
import cm.aptoide.pt.web3j.abi.datatypes.generated.Uint192;
import cm.aptoide.pt.web3j.abi.datatypes.generated.Uint256;
import cm.aptoide.pt.web3j.abi.datatypes.generated.Uint32;
import cm.aptoide.pt.web3j.utils.Convert.Unit;
import org.spongycastle.util.encoders.Hex;

import static cm.aptoide.pt.ethereumj.WorkInProgress.DESTINATION_WALLET;

/**
 * Created by neuro on 22-01-2018.
 */

public class CreateChannelExample {

  public static final Address SENDER_ADDRESS =
      new Address("0xD0d00C57e6198329d2d5c270276d9ca9f3fABaDE");
  public static final Address RECEIVER_ADDRESS =
      new Address("0xD0d00C57e6198329d2d5c270276d9ca9f3fABaDF");

  public static final Address TOKEN_ADDRESS =
      new Address("0xea99ec75cc5596c37b2a3319b2a9552cc05d23e4");
  public static final Address CHANNEL_MANAGER_ADDRESS =
      new Address("0xb9721df0e024114e7b25f2cf503d8cbe3d52b400");

  public static final ECKey ecKey = ECKey.fromPrivate(
      Hex.decode("0808fb96be6e847aec8e71542d84fc9b51a4c9237a0b8f0f12c8dfdab16aef37"));

  public static final long GAS_PRICE = 50 * Unit.GWEI.getWeiFactor()
      .longValue();
  public static final long GAS_LIMIT = 200_000;
  private static final int OPEN_BLOCK_NUMBER = 5509612;
  private final TokenContract tokenContract;

  public CreateChannelExample(TokenContract tokenContract) {
    this.tokenContract = tokenContract;
  }

  public static void main(String[] args) throws Exception {

    EthereumApi ethereumApi = EthereumApiFactory.createEthereumApi(Network.ROPSTEN);
    TokenContract tokenContract =
        new TokenContract(TOKEN_ADDRESS, CHANNEL_MANAGER_ADDRESS, ethereumApi);
    CreateChannelExample createChannelExample = new CreateChannelExample(tokenContract);
    ChannelManager channelManager =
        new ChannelManager(GAS_PRICE, GAS_LIMIT, CHANNEL_MANAGER_ADDRESS, ethereumApi);

    createChannelExample.createChannel();
    createChannelExample.topUpChannel();

    channelManager.uncooperativeClose(ecKey, RECEIVER_ADDRESS, new Uint32(OPEN_BLOCK_NUMBER),
        new Uint192(0));
    channelManager.settle(ecKey, RECEIVER_ADDRESS, new Uint32(OPEN_BLOCK_NUMBER));
  }

  public void createChannel() throws Exception {
    tokenContract.transfer(ecKey, DESTINATION_WALLET, new Uint256(1));
  }

  public void topUpChannel() throws Exception {
    tokenContract.topUp(ecKey, RECEIVER_ADDRESS, new Uint256(2), OPEN_BLOCK_NUMBER);
  }
}
