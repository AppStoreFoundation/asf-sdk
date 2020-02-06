package com.appcoins.communication;

import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class) public class IntentSyncIpcExecutorTest {

  private IntentSyncIpcExecutor service;

  @Before public void setup() {
    service = new IntentSyncIpcExecutor();
  }

  @Test public void sendMessageTest() {
    Person person = (Person) service.sendMessage(1, new Person("Fábio"));
    assertEquals("not same person", new Person("José"), person);
  }
}

