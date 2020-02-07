package com.appcoins.communication.sender;

public class MainThreadException extends Exception {
  public MainThreadException(String methodName) {
    super(methodName + " method can't run on main thread");
  }
}
