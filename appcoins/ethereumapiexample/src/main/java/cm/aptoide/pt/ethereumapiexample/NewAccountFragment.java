package cm.aptoide.pt.ethereumapiexample;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog.Builder;
import cm.aptoide.pt.ethereumapiexample.R.string;

public class NewAccountFragment extends DialogFragment {

  private OnDeleteAccountConfirmedListener listener;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    listener = (OnDeleteAccountConfirmedListener) context;
  }

  @Override
  public void onDetach() {
    listener = null;
    super.onDetach();
  }

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    Builder alertDialogBuilder = new Builder(getContext());
    alertDialogBuilder.setTitle(string.delete_account);
    alertDialogBuilder.setMessage(string.are_you_sure);
    //null should be your on click listener
    alertDialogBuilder.setPositiveButton(string.ok, new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        listener.onDeleteAccountConfirmed();
      }
    });
    alertDialogBuilder.setNegativeButton(string.cancel, new OnClickListener() {

      @Override public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });

    return alertDialogBuilder.create();
  }

  interface OnDeleteAccountConfirmedListener {
    void onDeleteAccountConfirmed();
  }
}
