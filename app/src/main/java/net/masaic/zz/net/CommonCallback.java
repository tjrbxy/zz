package net.masaic.zz.net;

import android.util.Log;


import com.vondear.rxtool.view.RxToast;
import com.zhy.http.okhttp.callback.StringCallback;

import net.masaic.zz.utils.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;

public abstract class CommonCallback<T> extends StringCallback {

    private static final String TAG = "CommonCallback-app";
    Type mType;

    public CommonCallback() {
        Class<? extends CommonCallback> clazz = getClass();
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            throw new RuntimeException("Miss Type Params");
        }
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;

        mType = parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        Log.d(TAG, "onError: call" + call.toString() + ",e:" + e + ",id:" + id);
        onError(e);
    }

    @Override
    public void onResponse(String response, int id) {
        Log.d(TAG, "onResponse: " + response + "mType" +mType);
        try {
            JSONObject resp = new JSONObject(response);
            int code = resp.getInt("code");
            String info = resp.getString("info");
            if (code == 200) {
                if(resp.has("data")){
                    String data = resp.getString("data");
                    onSuccess((T) GsonUtil.getGson().fromJson(data, mType), info);
                }else {
                    onSuccess((T) GsonUtil.getGson().fromJson(response, mType), info);
                }

            } else if (201 == code){
                RxToast.error(info);
                return;
            }else {
                onError(new RuntimeException(info));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onError(e);
        }
    }

    public abstract void onError(Exception e);

    public abstract void onSuccess(T response, String info);
}
