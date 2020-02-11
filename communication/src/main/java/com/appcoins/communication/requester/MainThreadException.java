package com.appcoins.communication.requester;

public class MainThreadException extends Exception {
  public MainThreadException(String methodName) {
    super(methodName + " method can't run on main thread");
  }
}
