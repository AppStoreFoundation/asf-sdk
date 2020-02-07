package com.appcoins.communication.sender;

import android.content.Context;
import android.os.Parcelable;
import com.appcoins.communication.SyncIpcMessageSender;
import java.util.HashMap;

public class MessageSenderBuilder {
  public static SyncIpcMessageSender build(Context context, String targetPackage, String targetUri,
      String receiverUri) {
    return new IntentSyncIpcMessageSender(new MessageSender(context, targetPackage, targetUri),
        new MessageResponseSynchronizer(new MessageReceiver(context, receiverUri),
            new HashMap<Long, Object>(), new HashMap<Long, Parcelable>()), new IdGenerator());
  }
}
