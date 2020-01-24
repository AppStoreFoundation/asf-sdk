package com.asf.appcoins.toolbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EmptyActivity extends AppCompatActivity {
  public static Intent newIntent(Context context) {
    return new Intent(context, EmptyActivity.class);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.test_activity);
  }
}
