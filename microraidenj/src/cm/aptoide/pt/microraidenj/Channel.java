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
    private Status status;
    /**
     * Channel current available balance.
     */
    private double deposit;

    public Info(Status status, double deposit) {
      this.status = status;
      this.deposit = deposit;
    }

    public Status getStatus() {
      return status;
    }

    public void setStatus(Status status) {
      this.status = status;
    }

    public double getDeposit() {
      return deposit;
    }

    public void setDeposit(double deposit) {
      this.deposit = deposit;
    }

    enum Status {
      OPENED, SETTLED,
    }
  }
}
