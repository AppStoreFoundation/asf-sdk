package com.appcoins.communication.sender;

import com.appcoins.communication.Data;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;

public class MessageReceiverTest {

  private MessageReceivedListener listener;

  @Before public void setUp() {
    listener = StaticMessageResponseSynchronizer.getMessageListener();
    StaticMessageResponseSynchronizer.init();
  }

  @Test public void onReceive() {
    Data returnValue = new Data("");
    listener.onMessageReceived(1L, returnValue);

    Mockito.verify(listener, times(1))
        .onMessageReceived(1L, returnValue);
  }
}