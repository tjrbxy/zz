package net.masaic.zz.bean;

public class MacLogs {
    private String name;
    private int icon;
    private String safety;

    public MacLogs() {

    }

    public MacLogs(String name, int icon, String safety) {
        this.name = name;
        this.icon = icon;
        this.safety = safety;
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
