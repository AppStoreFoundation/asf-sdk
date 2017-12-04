package cm.aptoide.pt.microraidenj;

import java.io.IOException;
import org.web3j.protocol.Web3j;

public class MicroRaiden {

  public static final String MY_ADDRESS = "0x";
  private static final String TAG = MicroRaiden.class.getSimpleName();
  private Web3j web3;
  private Channel channel;

  private String signMessage(String msg) {
    if (!channel.isValid()) {
      throw new IllegalStateException("No valid channelInfo");
    }
    byte[] hex = Utils.encodeHex(msg);
    System.out.println("Signing " + msg);

    try {
      return web3.ethSign(MY_ADDRESS, Utils.sha3Hash(hex))
          .send()
          .getSignature();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Failed to sign " + msg);
    }
  }

  private byte[] signBalance(double newBalance) {
    if (!channel.isValid()) {
      throw new IllegalStateException("No valid channelInfo");
    }

    System.out.println("signBalance " + newBalance + ", channel" + this.channel);
    if (newBalance == -1) {
      newBalance = this.channel.balance;
    }

    if (newBalance == this.channel.balance && this.channel.sign != null) {
      return channel.sign;
    }

    // FIXME: 29-11-2017 call contract getBalanceMessage with parms to sign the new balance
    return null;
  }
}
