package com.appcoins.communication.sender;

import android.support.test.runner.AndroidJUnit4;
import com.appcoins.communication.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

@RunWith(AndroidJUnit4.class) public class IntentSyncIpcExecutorTest {

  public static final long MESSAGE_ID = 0L;
  private final int TYPE = 1;
  private IntentSyncIpcMessageSender service;
  private MessageResponseSynchronizer messageResponseSynchronizer;
  private MessageSender messageSender;

  @Before public void setup() {
    messageSender = Mockito.mock(MessageSender.class);
    messageResponseSynchronizer = Mockito.mock(MessageResponseSynchronizer.class);
    IdGenerator idGenerator = Mockito.mock(IdGenerator.class);
    Mockito.when(idGenerator.generateRequestCode())
        .thenReturn(0L);

    service =
        new IntentSyncIpcMessageSender(messageSender, messageResponseSynchronizer, idGenerator);
  }

  @Test public void sendMessageTest() throws InterruptedException, MainThreadException {
    Mockito.when(messageResponseSynchronizer.waitMessage(0L))
        .thenReturn(new Data("José"));
    Data arguments = new Data("Fábio");
    Data person = (Data) service.sendMessage(TYPE, arguments);
    Mockito.verify(messageSender, times(1))
        .sendMessage(MESSAGE_ID, TYPE, arguments);
    assertEquals("not same person", new Data("José"), person);
  }
}

