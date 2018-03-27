package cm.aptoide.pt.ethereumapiexample;

import android.content.Intent;

/**
 * Created by neuro on 31-01-2018.
 */

public class PaymentManager {

  private static final String PAYMENT_AMMOUNT_KEY = "PAYMENT_AMMOUNT";

  private final double paymentAmmount;

  public PaymentManager(Intent intent) {
    paymentAmmount = intent.getDoubleExtra(PAYMENT_AMMOUNT_KEY, -1);
  }

  public double getPaymentAmmount() {
    return paymentAmmount;
  }
}
