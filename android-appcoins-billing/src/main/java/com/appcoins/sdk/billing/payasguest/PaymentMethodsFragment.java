package com.appcoins.sdk.billing.payasguest;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.appcoins.sdk.billing.SharedPreferencesRepository;
import com.appcoins.sdk.billing.WalletInteract;
import com.appcoins.sdk.billing.WalletInteractListener;
import com.appcoins.sdk.billing.models.WalletGenerationModel;

public class PaymentMethodsFragment extends Fragment {

  private WalletInteract walletInteract;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    walletInteract = new WalletInteract(new SharedPreferencesRepository(getActivity()));
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    RelativeLayout backgroundLayout = buildBackground();

    RelativeLayout dialogLayout = buildDialogLayout(getLayoutOrientation());
    backgroundLayout.addView(dialogLayout);
    return backgroundLayout;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    String id = walletInteract.retrieveId();
    WalletInteractListener walletInteractListener = createListener();
    walletInteract.requestWallet(id, walletInteractListener);
  }

  @SuppressLint("ResourceType") private RelativeLayout buildBackground() {
    int backgroundColor = Color.parseColor("#64000000");
    RelativeLayout backgroundLayout = new RelativeLayout(getActivity());
    backgroundLayout.setId(5);
    backgroundLayout.setBackgroundColor(backgroundColor);
    return backgroundLayout;
  }

  @SuppressLint("ResourceType") private RelativeLayout buildDialogLayout(int layoutOrientation) {
    RelativeLayout dialogLayout = new RelativeLayout(getActivity());
    dialogLayout.setClipToPadding(false);
    dialogLayout.setId(6);

    dialogLayout.setBackgroundColor(Color.WHITE);

    int dialogLayoutMargins = dpToPx(12);
    int cardWidth = RelativeLayout.LayoutParams.MATCH_PARENT;
    if (layoutOrientation == Configuration.ORIENTATION_LANDSCAPE) {
      cardWidth = dpToPx(384);
    }
    RelativeLayout.LayoutParams dialogLayoutParams =
        new RelativeLayout.LayoutParams(cardWidth, dpToPx(288));
    dialogLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    dialogLayoutParams.setMargins(dialogLayoutMargins, 0, dialogLayoutMargins, 0);
    dialogLayout.setLayoutParams(dialogLayoutParams);
    return dialogLayout;
  }

  private int dpToPx(int dp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem()
        .getDisplayMetrics());
  }

  private int getLayoutOrientation() {
    return getResources().getConfiguration().orientation;
  }

  private WalletInteractListener createListener() {
    return new WalletInteractListener() {
      @Override public void walletIdRetrieved(WalletGenerationModel walletGenerationModel) {
        Log.d("TAG123", walletGenerationModel.getWalletAddress()
            + "ewt: "
            + walletGenerationModel.getEwt()
            + " "
            + walletGenerationModel.hasError());
      }
    };
  }
}
