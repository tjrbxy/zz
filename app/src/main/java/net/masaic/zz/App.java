package net.masaic.zz;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.vondear.rxtool.RxTool;

import net.masaic.zz.utils.SPUtils;
import net.masaic.zz.utils.T;


public class App extends Application {
    private static final String TAG = "App-app";

    @Override
    public void onCreate() {
        // 程序创建的时候执行
        super.onCreate();
        // 工具
        RxTool.init(this);
        // 提示信息
        T.init(this);
        // 本地存储
        SPUtils.init(this, "setting.scan");
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.d(TAG, "onTerminate: ");
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Log.d(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

}
