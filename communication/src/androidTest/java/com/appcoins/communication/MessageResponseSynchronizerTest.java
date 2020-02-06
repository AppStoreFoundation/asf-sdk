package com.appcoins.communication;

import android.os.Parcelable;
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

    final Person testPerson = new Person("");
    new Thread(new Runnable() {
      @Override public void run() {
        argumentCaptor.getValue()
            .onMessageReceived(0, testPerson);
      }
    }).start();

    Person person = (Person) messageResponseSynchronizer.waitMessage(0);
    assertEquals("Person not returned as expected", testPerson, person);
  }
}