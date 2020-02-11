package com.appcoins.communication.requester;

import android.content.Context;
import com.appcoins.communication.SyncIpcMessageRequester;

public class MessageRequesterFactory {
  /**
   * @param processorPackage package name of the app that you desire to connect to
   * @param processorUri URI of the activity of the app that you desire to connect to (usually
   * described in activity's intent-filter)
   *
   * @return {@link SyncIpcMessageRequester} that allows you to communicate with the processor
   * application
   */
  public static SyncIpcMessageRequester create(Context context, String processorPackage,
      String processorUri) {
    return new IntentSyncIpcMessageSender(
        new MessageRequesterSender(context, processorPackage, processorUri),
        new MessageRequesterSynchronizer(), new IdGenerator());
  }
}
