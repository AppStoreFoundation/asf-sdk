package com.sdk.appcoins_lifecycle.lifecycle;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Internal class to initialize Lifecycles.
 *
 * @hide
 */
public class ProcessLifecycleOwnerInitializer extends ContentProvider {
  @Override public boolean onCreate() {
    LifecycleDispatcher.init(getContext());
    ProcessLifecycleOwner.init(getContext());
    return true;
  }

  @Override public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
    return null;
  }

  @Override public String getType(Uri uri) {
    return null;
  }

  @Override public Uri insert(Uri uri, ContentValues contentValues) {
    return null;
  }

  @Override public int delete(Uri uri, String s, String[] strings) {
    return 0;
  }

  @Override public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
    return 0;
  }
}
