package net.masaic.zz.biz;

import com.zhy.http.okhttp.OkHttpUtils;

import net.masaic.zz.bean.Res;
import net.masaic.zz.bean.User;
import net.masaic.zz.net.CommonCallback;

import java.util.Map;

public class UserBiz extends BaseBiz {

    private static final String TAG = "UserBiz-app";

    public void login(Map parmas, CommonCallback<User> commonCallback) {

        String url = this.API_URL + "api/login";
        OkHttpUtils
                .post()
                .url(url)
                .params(parmas)
                .build()
                .execute(commonCallback);
    }

    public void register(Map parmas, CommonCallback<Res> commonCallback){
        String url = this.API_URL + "api/v7/user";
        OkHttpUtils
                .post()
                .url(url)
                .params(parmas)
                .build()
                .execute(commonCallback);
    }
}
