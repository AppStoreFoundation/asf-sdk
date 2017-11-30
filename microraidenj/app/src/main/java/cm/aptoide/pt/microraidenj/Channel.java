package cm.aptoide.pt.microraidenj;

public class Channel {

  private static final String TAG = Channel.class.getSimpleName();

  public double balance;
  public Info info;
  public byte[] sign;

  public boolean isValid() {
    // TODO: 29-11-2017 neuro
    return false;
  }

  public static class Info {
    public Status status;
    public double deposit;

    enum Status {
      OPENED,
    }
  }
}
