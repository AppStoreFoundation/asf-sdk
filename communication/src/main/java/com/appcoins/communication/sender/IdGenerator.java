package com.appcoins.communication.sender;

class IdGenerator {
  public long generateId() {
    return System.currentTimeMillis() + Thread.currentThread()
        .getId();
  }
}
