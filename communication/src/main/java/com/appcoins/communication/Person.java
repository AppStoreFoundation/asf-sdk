package com.appcoins.communication;

import android.os.Parcel;
import android.os.Parcelable;

class Person implements Parcelable {
  public static final Creator<Person> CREATOR = new Creator<Person>() {
    @Override public Person createFromParcel(Parcel in) {
      return new Person(in);
    }

    @Override public Person[] newArray(int size) {
      return new Person[size];
    }
  };
  private final String name;

  Person(String name) {
    this.name = name;
  }

  protected Person(Parcel in) {
    name = in.readString();
  }

  @Override public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Person)) return false;

    Person person = (Person) o;

    return name != null ? name.equals(person.name) : person.name == null;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
  }
}
