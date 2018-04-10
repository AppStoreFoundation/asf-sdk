package com.asf.appcoins.sdk.ads.poa;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import com.asf.appcoins.sdk.ads.BuildConfig;
import com.asf.appcoins.sdk.ads.LifeCycleListener;

import static com.asf.appcoins.sdk.ads.poa.MessageListener.MSG_SEND_PROOF;

/**
 * Class that will manage the PoA process, by sending the proofs on the correct time. By handling
 * when the process is finished or is stopped.
 *
 * Created by Joao Raimundo on 06/04/2018.
 */

public class PoAManager implements LifeCycleListener.Listener {

  public static final String TAG = PoAManager.class.getName();

  /** The instance of the manager*/
  private static PoAManager instance;
  /** The connector with the wallet service, receiver of the messaages of the PoA. */
  private static PoAServiceConnector poaConnector;
  /** The application context */
  private static Context appContext;
  /** boolean indicating if we are already processing a PoA*/
  private boolean processing;
  /** The handle to keep the runnable tasks that we be running within a certain period */
  private Handler handler = new Handler();
  /** The runnnable taks that will be trigger periodically */
  private Runnable sendProof;
  /** integer used to track how many proof were already sent*/
  private int proofsSent = 0;

  /**
   *  Initialisation method for the manager
   *  @param context The context of the application.
   *  @param connector The PoA service connector used on the communication of the proof of attention.
   */
  public static PoAManager init(Context context, PoAServiceConnector connector){
    if (instance == null) {
      instance = new PoAManager();
      poaConnector = connector;
      appContext = context;
    }
    return instance;
  }

  /**
   * Getter for the instance of the manager.
   * @param context The application context
   * @param connector The onnector to the wallet service.
   */
  public static PoAManager get(Context context, PoAServiceConnector connector){
    if (instance == null) {
      init(context, connector);
    }
    return instance;
  }

  /**
   * Method that starts the process. This will trigger the handshake required to communicate
   * with the wallet service, if not already done.
   * Then it will trigger the first proof sent.
   */
  private void startProcess(){
    // If starting the PoA process do handshake
    if (!processing) {
      processing = true;
      poaConnector.startHandshake(appContext);
    }
    sendProof();
  }

  /**
   * Method that stops the process. It will remove any running tasks and disconnect from the waller
   * service.
   */
  private void stopProcess(){
    processing = false;

    if (sendProof != null) {
      handler.removeCallbacks(sendProof);
    }
    poaConnector.disconnectFromService(appContext);
  }

  /**
   * Method that send a proof. The checks how many were sent and if bellow the number of proofs
   * required to be sent, schedules a new send proof to be triggered after defined delay.
   * If all proofs were sent, it stops the process.
   */
  private void sendProof() {
    // send proof
    if (poaConnector.connectToService(appContext)) {
      long timestamp = System.currentTimeMillis();
      Bundle bundle = new Bundle();
      bundle.putString("packageName", appContext.getPackageName());
      bundle.putLong("timeStamp", timestamp);
      poaConnector.sendMessage(appContext, MSG_SEND_PROOF, bundle);
      proofsSent++;
      // schedule the next proof sending
      if (proofsSent < BuildConfig.ADS_POA_NUMBER_OF_PROOFS) {
        handler.postDelayed(sendProof = this::sendProof, BuildConfig.ADS_POA_PROOFS_INTERVAL_IN_MILIS);
      } else {
        // or stop the process
        processing = false;
        stopProcess();
      }
    }
  }

  @Override public void onBecameForeground() {
    startProcess();
  }

  @Override public void onBecameBackground() {
    stopProcess();
  }
}
