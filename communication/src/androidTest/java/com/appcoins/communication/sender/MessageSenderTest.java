package com.appcoins.communication.sender;

import android.content.Context;
import android.content.Intent;
import com.appcoins.communication.Person;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class MessageSenderTest {

  public static final long MESSAGE_ID = 0L;
  public static final int TYPE = 0;
  public static final String TARGET_URI = "uri";
  public static final String PACKAGE = "package";
  private MessageSender messageSender;
  private Context context;

  @Before public void setUp() {
    context = Mockito.mock(Context.class);
    messageSender = new MessageSender(context, PACKAGE, TARGET_URI);
  }

  @Test public void sendMessage() {
    ArgumentCaptor<Intent> argumentCaptor = ArgumentCaptor.forClass(Intent.class);
    Mockito.doNothing()
        .when(context)
        .sendBroadcast(argumentCaptor.capture());

    Person arguments = new Person("");
    messageSender.sendMessage(MESSAGE_ID, TYPE, arguments);

    Intent intent = argumentCaptor.getValue();

    assertEquals("wrong action view", Intent.ACTION_VIEW, intent.getAction());
    assertEquals("wrong target uri", TARGET_URI, intent.getDataString());
    assertEquals("wrong target package", PACKAGE, intent.getPackage());
    assertEquals("wrong message id argument", MESSAGE_ID, intent.getLongExtra("MESSAGE_ID", -1));
    assertEquals("wrong method type argument", TYPE, intent.getIntExtra("TYPE", -1));
    assertEquals("wrong argument", arguments, intent.getParcelableExtra("ARGUMENTS"));
  }
}