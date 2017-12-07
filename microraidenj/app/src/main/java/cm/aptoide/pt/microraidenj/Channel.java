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

    /**
     * Class representing the Channel State.
     */
    public static class Info {
        /**
         * Status of the channel (Opened, Settled)
         */
        public Status status;
        /**
         * Channel current available balance.
         */
        public double deposit;

        enum Status {
            OPENED,
            SETTLED,
        }
    }
}
