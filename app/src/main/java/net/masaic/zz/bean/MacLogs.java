package net.masaic.zz.bean;

import java.io.Serializable;

public class MacLogs implements Serializable {

    private int is_marked;
    private int checktime;
    private double latitude;
    private double longitude;
    private String address;
    private String mac;
    private String name;
    private int icon;
    private String safety;

    public MacLogs() {
    }

    public MacLogs(int is_marked, int checktime, double latitude, double longitude, String address, String mac, String name, int icon, String safety) {
        this.is_marked = is_marked;
        this.checktime = checktime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.mac = mac;
        this.name = name;
        this.icon = icon;
        this.safety = safety;
    }

    public int getIs_marked() {
        return is_marked;
    }

    public void setIs_marked(int is_marked) {
        this.is_marked = is_marked;
    }

    public int getChecktime() {
        return checktime;
    }

    public void setChecktime(int checktime) {
        this.checktime = checktime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getSafety() {
        return safety;
    }

    public void setSafety(String safety) {
        this.safety = safety;
    }
}
