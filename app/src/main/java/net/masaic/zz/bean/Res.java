package net.masaic.zz.bean;


import android.support.annotation.NonNull;

public class Res {

    private int code;
    private String info;

    public Res(int code ,String info){
        this.code = code;
        this.info = info;
    }
    public int getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
