package cm.aptoide.pt.microraidenj;

import java.io.IOException;
import org.web3j.protocol.Web3j;

public class MicroRaiden {

  public static final String MY_ADDRESS = "0x";
  private static final String TAG = MicroRaiden.class.getSimpleName();

  private final Web3j web3;
  private final Channel channel;

  public MicroRaiden(Web3j web3, Channel channel) {
    this.web3 = web3;
    this.channel = channel;
  }

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

  byte[] incrementBalanceAndSign(double amount, Runnable callback) {
    if (!channel.isValid()) {
      throw new IllegalStateException("No valid channelInfo");
    }

    double newBalance = channel.balance + amount;

    if (channel.info.status != Channel.Info.Status.OPENED) {
      throw new IllegalStateException("Tried signing on closed channel");
    } else if (newBalance > channel.info.deposit) {
      throw new IllegalStateException(
          "Insuficient funds: current = " + channel.info.deposit + ", required = " + newBalance);
    }

    byte[] bytes = signBalance(newBalance);
    channel.balance = newBalance;
    channel.sign = bytes;

    return bytes;
  }
}