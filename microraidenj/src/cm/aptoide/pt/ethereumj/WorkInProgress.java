package cm.aptoide.pt.ethereumj;

import cm.aptoide.pt.ethereum.EthereumApi;
import cm.aptoide.pt.ethereum.EthereumApiFactory;
import cm.aptoide.pt.ethereum.ws.Network;
import cm.aptoide.pt.ethereumj.crypto.ECKey;
import cm.aptoide.pt.microraidenj.ChannelManager;
import cm.aptoide.pt.microraidenj.TokenContract;
import cm.aptoide.pt.web3j.abi.datatypes.Address;
import cm.aptoide.pt.web3j.abi.datatypes.generated.Uint256;
import cm.aptoide.pt.web3j.utils.Convert.Unit;
import org.spongycastle.util.encoders.Hex;

/**
 * Created by neuro on 12-01-2018.
 */

public class WorkInProgress {

  public static final Address WALLET_ADDRESS =
      new Address("0xD0d00C57e6198329d2d5c270276d9ca9f3fABaDE");
  public static final Address DESTINATION_WALLET =
      new Address("0xD0d00C57e6198329d2d5c270276d9ca9f3fABaDF");

  public static final Address TOKEN_ADDRESS =
      new Address("0xf01c9ed31c396dca09a1deeecbe2f12fc819e3be");
  public static final Address CHANNEL_MANAGER_ADDRESS =
      new Address("0x161a0d7726eb8b86eb587d8bd483be1ce87b0609");
  //public static final ECKey ecKey = ECKey.fromPrivate(
  //    Hex.decode("0808fb96be6e847aec8e71542d84fc9b51a4c9237a0b8f0f12c8dfdab16aef37"));
  public static final ECKey ecKey = ECKey.fromPrivate(
      Hex.decode("0808fb96be6e847aec8e71542d84fc9b51a4c9237a0b8f0f12c8dfdab16aef37"));

  public static final long GAS_PRICE = 50 * Unit.GWEI.getWeiFactor()
      .longValue();
  public static final long GAS_LIMIT = 200_000_0;

  private static ChannelManager channelManager;
  private static TokenContract tokenContract;
  private static EthereumApi ethereumApi;

  public static void main(String[] args) throws Exception {
    initClass();
  }

  /**
   * mas olha se te der jeito eu nao me importo de passar algures, se nao fica para amanha se lá
   * chegar lo0l
   *
   * @throws Exception
   */
  public static void initClass() throws Exception {
    ethereumApi = EthereumApiFactory.createEthereumApi(Network.ROPSTEN);
    channelManager = new ChannelManager(GAS_PRICE, GAS_LIMIT, CHANNEL_MANAGER_ADDRESS, ethereumApi);
    tokenContract = new TokenContract(TOKEN_ADDRESS, CHANNEL_MANAGER_ADDRESS, ethereumApi);
    //ethereumApi.send(DESTINATION_WALLET, BigDecimal.valueOf(0.001), ecKey, GAS_PRICE, GAS_LIMIT)
    //    .subscribe(transactionResultResponse -> System.out.println(transactionResultResponse),
    //        Throwable::printStackTrace);
  }

  public void createChannel() throws Exception {
    ECKey ecKey = ECKey.fromPrivate(
        Hex.decode("0808fb96be6e847aec8e71542d84fc9b51a4c9237a0b8f0f12c8dfdab16aef37"));
    //channelManager.createChannel(ecKey, DESTINATION_WALLET, new Uint192(1));
    tokenContract.transfer(ecKey, DESTINATION_WALLET, new Uint256(1));
    //tokenContract.topUp(ecKey, DESTINATION_WALLET, new Uint256(2), 5460299);
  }
}
