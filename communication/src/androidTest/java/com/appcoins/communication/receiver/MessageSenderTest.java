package com.appcoins.communication.receiver;

import android.content.Context;
import android.content.Intent;
import com.appcoins.communication.Person;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class MessageSenderTest {

  public static final String SENDER_URI = "appcoins://testing";
  private MessageSender messageSender;
  private Context context;

  @Before public void setUp() {
    context = Mockito.mock(Context.class);
    messageSender = new MessageSender(context, SENDER_URI);
  }

  @Test public void sendMessage() {
    ArgumentCaptor<Intent> argumentCaptor = ArgumentCaptor.forClass(Intent.class);
    Mockito.doNothing()
        .when(context)
        .sendBroadcast(argumentCaptor.capture());

    Person response = new Person("");
    messageSender.sendMessage(1L, response);

    Intent intent = argumentCaptor.getValue();

    assertEquals(1L, intent.getLongExtra("MESSAGE_ID", -1));
    assertEquals(response, intent.getParcelableExtra("RETURN_VALUE"));
  }
}