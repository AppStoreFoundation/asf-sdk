package com.appcoins.communication.requester;

import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

@RunWith(AndroidJUnit4.class) public class IntentSyncIpcExecutorTest {

  public static final long MESSAGE_ID = 0L;
  public static final int TIMEOUT = 10000;
  private final int TYPE = 0;
  private IntentSyncIpcMessageSender service;
  private MessageRequesterSynchronizer messageResponseSynchronizer;
  private MessageRequesterSender messageSender;

  @Before public void setup() {
    messageSender = Mockito.mock(MessageRequesterSender.class);
    messageResponseSynchronizer = Mockito.mock(MessageRequesterSynchronizer.class);
    IdGenerator idGenerator = Mockito.mock(IdGenerator.class);
    Mockito.when(idGenerator.generateRequestCode())
        .thenReturn(0L);

    service =
        new IntentSyncIpcMessageSender(messageSender, messageResponseSynchronizer, idGenerator,
            TIMEOUT);
  }

  @Test public void sendMessageTest() throws InterruptedException, MainThreadException {
    Mockito.when(messageResponseSynchronizer.waitMessage(0L, TIMEOUT))
        .thenReturn(new Intent("intent1"));
    Intent arguments = new Intent("intent2");
    Intent intent = (Intent) service.sendMessage(TYPE, arguments);
    Mockito.verify(messageSender, times(1))
        .sendMessage(MESSAGE_ID, TYPE, arguments);
    assertEquals("not same intent", "intent1", intent.getAction());
  }
}

