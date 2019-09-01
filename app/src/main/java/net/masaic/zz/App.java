package net.masaic.zz;

import android.app.Application;

import net.masaic.zz.utils.SPUtils;
import net.masaic.zz.utils.T;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 提示信息
        T.init(this);
        // 本地存储
        SPUtils.init(this, "setting.scan");
    }
}
