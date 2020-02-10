package com.appcoins.communication;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {
  public static final Creator<Data> CREATOR = new Creator<Data>() {
    @Override public Data createFromParcel(Parcel in) {
      return new Data(in);
    }

    @Override public Data[] newArray(int size) {
      return new Data[size];
    }
  };
  private final String data;

  public Data(String data) {
    this.data = data;
  }

  protected Data(Parcel in) {
    data = in.readString();
  }

  @Override public int hashCode() {
    return data != null ? data.hashCode() : 0;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Data)) return false;

    Data data = (Data) o;

    return this.data != null ? this.data.equals(data.data) : data.data == null;
  }

  @SuppressWarnings("NullableProblems") @Override public String toString() {
    return "Data{" + "data='" + data + '\'' + '}';
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(data);
  }

  public String getData() {
    return data;
  }
}
