package com.appcoins.sdk.billing.helpers;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import java.io.FileDescriptor;

public class IBinderWalletNotInstalled implements IBinder {
  @Override public String getInterfaceDescriptor() {
    return null;
  }

  @Override public boolean pingBinder() {
    return false;
  }

  @Override public boolean isBinderAlive() {
    return false;
  }

  @Override public IInterface queryLocalInterface(String descriptor) {
    return null;
  }

  @Override public void dump(FileDescriptor fd, String[] args) {

  }

  @Override public void dumpAsync(FileDescriptor fd, String[] args) {

  }

  @Override public boolean transact(int code, Parcel data, Parcel reply, int flags) {
    return false;
  }

  @Override public void linkToDeath(IBinder.DeathRecipient recipient, int flags) {

  }

  @Override
  public boolean unlinkToDeath(IBinder.DeathRecipient recipient,
      int flags) {
    return false;
  }
}
