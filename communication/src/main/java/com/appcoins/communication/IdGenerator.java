package com.appcoins.communication;

class IdGenerator {
  public long generateId() {
    return System.currentTimeMillis() + Thread.currentThread()
        .getId();
  }
}
