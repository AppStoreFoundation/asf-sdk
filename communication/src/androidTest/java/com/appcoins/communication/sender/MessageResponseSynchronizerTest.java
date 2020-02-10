package com.appcoins.communication.sender;

import android.os.Parcelable;
import com.appcoins.communication.Data;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class MessageResponseSynchronizerTest {

  private MessageResponseSynchronizer messageResponseSynchronizer;
  private MessageReceiver messageReceiver;
  private ArgumentCaptor<MessageReceiver.MessageReceivedListener> argumentCaptor;

  @Before public void setUp() {
    messageReceiver = Mockito.mock(MessageReceiver.class);
    argumentCaptor = ArgumentCaptor.forClass(MessageReceiver.MessageReceivedListener.class);
    Mockito.doNothing()
        .when(messageReceiver)
        .setListener(argumentCaptor.capture());
    messageResponseSynchronizer =
        new MessageResponseSynchronizer(messageReceiver, new HashMap<Long, Object>(),
            new HashMap<Long, Parcelable>());
  }

  @Test public void waitForResponse() throws InterruptedException {

    final Data testPerson = new Data("");
    new Thread(new Runnable() {
      @Override public void run() {
        argumentCaptor.getValue()
            .onMessageReceived(0, testPerson);
      }
    }).start();

    Data person = (Data) messageResponseSynchronizer.waitMessage(0);
    assertEquals("Person not returned as expected", testPerson, person);
  }
}