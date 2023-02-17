package net.masaic.zz.bean;


import java.io.Serializable;

public class Res implements Serializable {
    private int code;
    private String info;

    public Res() {
    }

    public Res(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
