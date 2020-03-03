package com.appcoins.communication.requester;

class IdGenerator {
  public long generateRequestCode() {
    return System.currentTimeMillis() + Thread.currentThread()
        .getId();
  }
}
