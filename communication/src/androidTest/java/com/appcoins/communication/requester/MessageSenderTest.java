package com.appcoins.communication.requester;

import android.content.Context;
import android.content.Intent;
import com.appcoins.communication.MessageProcessorActivity;
import com.appcoins.communication.test.BuildConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class MessageSenderTest {

  public static final long MESSAGE_ID = 0L;
  public static final int TYPE = 0;
  public static final String TARGET_URI = "appcoins://send/data";
  public static final String RECEIVE_TARGET_URI = "appcoins://receive/data";
  public static final String PACKAGE = BuildConfig.APPLICATION_ID;
  private MessageRequesterSender messageSender;
  private Context context;

  @Before public void setUp() {
    context = Mockito.mock(Context.class);
    messageSender = new MessageRequesterSender(context, PACKAGE, TARGET_URI, RECEIVE_TARGET_URI);
  }

  @Test public void sendMessage() {
    ArgumentCaptor<Intent> argumentCaptor = ArgumentCaptor.forClass(Intent.class);
    Mockito.doNothing()
        .when(context)
        .startActivity(argumentCaptor.capture());

    Mockito.when(context.getPackageName())
        .thenReturn(PACKAGE);

    Intent arguments = new Intent("");
    messageSender.sendMessage(MESSAGE_ID, TYPE, arguments);

    Intent intent = argumentCaptor.getValue();

    assertEquals("wrong action view", Intent.ACTION_VIEW, intent.getAction());
    assertEquals("wrong action view", TARGET_URI, intent.getDataString());
    assertEquals("wrong target package", PACKAGE, intent.getPackage());
    assertEquals("wrong message id argument", MESSAGE_ID,
        intent.getLongExtra(MessageProcessorActivity.MESSAGE_ID, -1));
    assertEquals("wrong method type argument", TYPE,
        intent.getIntExtra(MessageProcessorActivity.TYPE, -1));
    assertEquals("wrong sender package name", BuildConfig.APPLICATION_ID,
        intent.getStringExtra(MessageProcessorActivity.REQUESTER_PACKAGE_NAME));
    assertEquals("wrong sender package name", RECEIVE_TARGET_URI,
        intent.getStringExtra(MessageProcessorActivity.REQUESTER_ACTIVITY_URI));
    assertEquals("wrong argument", arguments,
        intent.getParcelableExtra(MessageProcessorActivity.ARGUMENTS));
  }
}