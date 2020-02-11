package com.appcoins.communication.sender;

import android.content.Context;
import com.appcoins.communication.SyncIpcMessageSender;

public class MessageSenderBuilder {
  public static SyncIpcMessageSender build(Context context, String targetPackage,
      String targetUri) {
    return new IntentSyncIpcMessageSender(new MessageSender(context, targetPackage, targetUri),
        new MessageResponseSynchronizer(), new IdGenerator());
  }
}
