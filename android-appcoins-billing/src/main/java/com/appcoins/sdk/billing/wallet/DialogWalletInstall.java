package com.appcoins.sdk.billing.wallet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appcoins.sdk.android.billing.R;

public class DialogWalletInstall extends Dialog {

    private final static String WALLET_PACKAGE = "com.appcoins.wallet";
    private final static String GOOGLEPLAY_URI = "https://play.google.com/store/apps/details?id=" + WALLET_PACKAGE;


    private Button dialog_wallet_install_button_cancel;
    private Button dialog_wallet_install_button_download;
    private TextView dialog_wallet_install_text_message;
    private ImageView dialog_wallet_install_image_icon;
    private ImageView dialog_wallet_install_image_graphic;

    private boolean hasImage;

    public static DialogWalletInstall with(Context context) {
        return new DialogWalletInstall(context);
    }

    public DialogWalletInstall(Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.dialog_wallet_install_parent_background);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.wallet_install_dialog);
        setCancelable(false);

        buildTop();
        buildMessage();
        buildCancelButton();
        buildDownloadButton();
    }

    private void buildTop() {
        dialog_wallet_install_image_icon = findViewById(R.id.dialog_wallet_install_image_icon);
        dialog_wallet_install_image_graphic = findViewById(R.id.dialog_wallet_install_image_graphic);

        hasImage = getContext().getResources().getBoolean(R.bool.dialog_wallet_install_has_image);

        if (hasImage) {
            dialog_wallet_install_image_icon.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(dp(296), dp(144));
            dialog_wallet_install_image_graphic.setLayoutParams(lp);
            dialog_wallet_install_image_graphic.setImageDrawable(getContext().getDrawable(R.drawable.dialog_wallet_install_graphic));


        } else {
            dialog_wallet_install_image_icon.setVisibility(View.VISIBLE);
            dialog_wallet_install_image_icon.setImageDrawable(getContext().getDrawable(R.drawable.dialog_wallet_install_icon));
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(dp(296), dp(100));
            dialog_wallet_install_image_graphic.setLayoutParams(lp);
            dialog_wallet_install_image_graphic.setImageDrawable(getContext().getDrawable(R.drawable.dialog_wallet_install_empty_image));

        }
    }

    private void buildMessage() {
        dialog_wallet_install_text_message = findViewById(R.id.dialog_wallet_install_text_message);
        String dialog_message = getContext().getString(R.string.app_wallet_install_wallet_from_ads);

        Spanned messageStylized = Html.fromHtml(dialog_message);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            messageStylized = Html.fromHtml(dialog_message, Html.FROM_HTML_MODE_LEGACY);
        }

        dialog_wallet_install_text_message.setText(messageStylized);
    }

    private void buildDownloadButton() {
        dialog_wallet_install_button_download = findViewById(R.id.dialog_wallet_install_button_download);
        dialog_wallet_install_button_download.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                redirectToStore();
            }
        });
    }

    private void buildCancelButton() {
        dialog_wallet_install_button_cancel = findViewById(R.id.dialog_wallet_install_button_cancel);
        dialog_wallet_install_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogWalletInstall.this.dismiss();
            }
        });
    }

    private void redirectToStore() {
        //https://developer.android.com/distribute/marketing-tools/linking-to-google-play
        getContext().startActivity(buildStoreViewIntent(GOOGLEPLAY_URI));
    }

    private Intent buildStoreViewIntent(String action) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(action));
    }

    private int dp(int px) {
        int pixels = (int) (px * getContext().getResources().getDisplayMetrics().density);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(pixels / (displayMetrics.xdpi / displayMetrics.densityDpi));
    }


}
