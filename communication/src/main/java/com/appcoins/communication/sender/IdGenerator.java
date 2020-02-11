package com.appcoins.communication.sender;

class IdGenerator {
  public long generateRequestCode() {
    return System.currentTimeMillis() + Thread.currentThread()
        .getId();
  }
}
