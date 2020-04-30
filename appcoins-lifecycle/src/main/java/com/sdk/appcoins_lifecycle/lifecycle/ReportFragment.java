package com.sdk.appcoins_lifecycle.lifecycle;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

/**
 * Internal class that dispatches initialization events.
 *
 * @hide
 */
@SuppressWarnings("UnknownNullness") // TODO https://issuetracker.google.com/issues/112197238
public class ReportFragment extends Fragment {
  private static final String REPORT_FRAGMENT_TAG =
      "androidx.lifecycle" + ".LifecycleDispatcher.report_fragment_tag";
  private ActivityInitializationListener mProcessListener;

  public static void injectIfNeededIn(Activity activity) {
    // ProcessLifecycleOwner should always correctly work and some activities may not extend
    // FragmentActivity from support lib, so we use framework fragments for activities
    FragmentManager manager = activity.getFragmentManager();
    if (manager.findFragmentByTag(REPORT_FRAGMENT_TAG) == null) {
      manager.beginTransaction()
          .add(new ReportFragment(), REPORT_FRAGMENT_TAG)
          .commit();
      // Hopefully, we are the first to make a transaction.
      manager.executePendingTransactions();
    }
  }

  static ReportFragment get(Activity activity) {
    return (ReportFragment) activity.getFragmentManager()
        .findFragmentByTag(REPORT_FRAGMENT_TAG);
  }

  private void dispatchCreate(ActivityInitializationListener listener) {
    if (listener != null) {
      listener.onCreate();
    }
  }

  private void dispatchStart(ActivityInitializationListener listener) {
    if (listener != null) {
      listener.onStart();
    }
  }

  private void dispatchResume(ActivityInitializationListener listener) {
    if (listener != null) {
      listener.onResume();
    }
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    dispatchCreate(mProcessListener);
    dispatch(Lifecycle.Event.ON_CREATE);
  }

  @Override public void onStart() {
    super.onStart();
    dispatchStart(mProcessListener);
    dispatch(Lifecycle.Event.ON_START);
  }

  @Override public void onResume() {
    super.onResume();
    dispatchResume(mProcessListener);
    dispatch(Lifecycle.Event.ON_RESUME);
  }

  @Override public void onPause() {
    super.onPause();
    dispatch(Lifecycle.Event.ON_PAUSE);
  }

  @Override public void onStop() {
    super.onStop();
    dispatch(Lifecycle.Event.ON_STOP);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    dispatch(Lifecycle.Event.ON_DESTROY);
    // just want to be sure that we won't leak reference to an activity
    mProcessListener = null;
  }

  private void dispatch(Lifecycle.Event event) {
    Activity activity = getActivity();
    if (activity instanceof LifecycleRegistryOwner) {
      ((LifecycleRegistryOwner) activity).getLifecycle()
          .handleLifecycleEvent(event);
      return;
    }

    if (activity instanceof LifecycleOwner) {
      Lifecycle lifecycle = ((LifecycleOwner) activity).getLifecycle();
      if (lifecycle instanceof LifecycleRegistry) {
        ((LifecycleRegistry) lifecycle).handleLifecycleEvent(event);
      }
    }
  }

  void setProcessListener(ActivityInitializationListener processListener) {
    mProcessListener = processListener;
  }

  interface ActivityInitializationListener {
    void onCreate();

    void onStart();

    void onResume();
  }
}
