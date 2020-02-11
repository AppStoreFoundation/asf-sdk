package com.appcoins.communication.sender;

import android.content.Context;
import com.appcoins.communication.SyncIpcMessageSender;

public class MessageSenderBuilder {
  /**
   * @param processorPackage package name of the app that you desire to connect to
   * @param processorUri URI of the activity of the app that you desire to connect to (usually
   * described in activity's intent-filter)
   *
   * @return {@link SyncIpcMessageSender} that allows you to communicate with the processor
   * application
   */
  public static SyncIpcMessageSender build(Context context, String processorPackage,
      String processorUri) {
    return new IntentSyncIpcMessageSender(
        new MessageSender(context, processorPackage, processorUri),
        new MessageResponseSynchronizer(), new IdGenerator());
  }
}
