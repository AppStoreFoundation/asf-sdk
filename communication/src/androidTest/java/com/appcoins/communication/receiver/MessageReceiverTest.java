package com.appcoins.communication.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.appcoins.communication.Person;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;

public class MessageReceiverTest {

  public static final String RECEIVER_URI = "appcoins://testing";
  private MessageReceiver messageReceiver;
  private Context context;
  private MessageReceiver.MessageReceivedListener listener;

  @Before public void setUp() throws Exception {
    context = Mockito.mock(Context.class);
    listener = Mockito.mock(MessageReceiver.MessageReceivedListener.class);
    messageReceiver = new MessageReceiver(context, RECEIVER_URI);
    messageReceiver.setListener(listener);
  }

  @Test public void onReceive() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(RECEIVER_URI));

    intent.putExtra("MESSAGE_ID", 1L);
    intent.putExtra("TYPE", 1);
    Person argument = new Person("");
    intent.putExtra("ARGUMENTS", argument);

    messageReceiver.onReceive(context, intent);

    Mockito.verify(listener, times(1))
        .onReceived(1L, 1, argument);
  }
}