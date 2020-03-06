package com.appcoins.communication.requester;

import android.content.Context;
import com.appcoins.communication.SyncIpcMessageRequester;

public class MessageRequesterFactory {
  /**
   * @param processorPackage package name of the app that you desire to connect to
   * @param processorActivityUri URI of the activity of the app that you desire to connect to
   * (usually
   * described in activity's intent-filter)
   * @param requesterActivityUri URI of the activity declared on manifest that extends
   * {@link MessageRequesterActivity } (usually described in activity's intent-filter)
   * @param timeout
   *
   * @return {@link SyncIpcMessageRequester} that allows you to communicate with the processor
   * application
   */
  public static SyncIpcMessageRequester create(Context context, String processorPackage,
      String processorActivityUri, String requesterActivityUri, int timeout) {
    return new IntentSyncIpcMessageSender(
        new MessageRequesterSender(context, processorPackage, processorActivityUri,
            requesterActivityUri), new MessageRequesterSynchronizer(), new IdGenerator(), timeout);
  }
}
