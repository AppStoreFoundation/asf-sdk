package com.sdk.appcoins_adyen;

import com.sdk.appcoins_adyen.encryption.CardEncryptorImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CardEncryptorTest {

  private CardEncryptorImpl cardEncryptor;

  @Before public void setupTest() {
    cardEncryptor = new CardEncryptorImpl();
  }

  @Test public void encryptionTest() {
    //Not possible due to dependencies on android security libs
  }
}