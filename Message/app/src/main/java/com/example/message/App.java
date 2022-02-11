package com.example.message;

import androidx.multidex.MultiDexApplication;
import dagger.hilt.android.HiltAndroidApp;
import io.realm.BuildConfig;
import timber.log.Timber;

@HiltAndroidApp
public class App extends MultiDexApplication {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    private void initLog() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
