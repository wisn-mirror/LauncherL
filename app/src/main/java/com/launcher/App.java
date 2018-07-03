package com.launcher;

import android.app.Application;

import com.launcher.utils.Utils;

/**
 * Created by Wisn on 2018/7/3 上午11:10.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
