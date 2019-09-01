package net.masaic.zz.biz;

import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;

import net.masaic.zz.bean.MacLogs;
import net.masaic.zz.net.CommonCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MacLogsBiz extends BaseBiz {

    private static final String TAG = "MacLogsBiz-app";

    public void insertMac(Map parmas, CommonCallback<List<MacLogs>> commonCallback) {

        String url = this.API_URL + "api/v7/mac";
        Log.d(TAG, "insertMac: " + url);
        OkHttpUtils
                .post()
                .url(url)
                .params(parmas)
                .build()
                .execute(commonCallback);
    }
}
