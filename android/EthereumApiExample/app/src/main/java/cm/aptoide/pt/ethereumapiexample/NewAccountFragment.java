package cm.aptoide.pt.ethereumapiexample;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class NewAccountFragment extends DialogFragment {
  private Context mContext;
  private DialogInterface.OnClickListener okListener;

  public static DialogFragment newInstance(Context mContext,
      DialogInterface.OnClickListener clickListener) {
    NewAccountFragment paySomethingFragment = new NewAccountFragment();
    paySomethingFragment.mContext = mContext;
    paySomethingFragment.okListener = clickListener;
    return paySomethingFragment;
  }

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
    alertDialogBuilder.setTitle("Delete Account");
    alertDialogBuilder.setMessage("Are you sure?");
    //null should be your on click listener
    alertDialogBuilder.setPositiveButton("OK", okListener);
    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

      @Override public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });

    return alertDialogBuilder.create();
  }
}
