package com.appcoins.communication.requester;

import android.content.Intent;
import org.junit.Assert;
import org.junit.Test;

public class StaticMessageResponseSynchronizerTest {

  @Test public void init() {
    StaticMessageResponseSynchronizer.init();
    MessageRequesterListener messageListener =
        StaticMessageResponseSynchronizer.getMessageListener();
    Assert.assertNotEquals(null, messageListener);
  }

  @Test public void waitMessage() throws InterruptedException {
    StaticMessageResponseSynchronizer.init();
    MessageRequesterListener messageListener =
        StaticMessageResponseSynchronizer.getMessageListener();
    Intent returnValue = new Intent();
    messageListener.onMessageReceived(1, returnValue);
    Intent data = (Intent) StaticMessageResponseSynchronizer.waitMessage(1);
    Assert.assertEquals(returnValue, data);
  }
}