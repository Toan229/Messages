package com.example.message.ui.base;

import android.content.Context;
import android.os.Bundle;
import com.example.message.utils.LocaleUtils;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        LocaleUtils.applyLocale(newBase);
        super.attachBaseContext(newBase);
    }
}
