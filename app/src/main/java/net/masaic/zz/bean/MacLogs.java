package net.masaic.zz.bean;

public class MacLogs {
    private String name;
    private int icon;
    private int safety;

    public MacLogs() {

    }

    public MacLogs(String name, int icon, int safety) {
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

    public int getSafety() {
        return safety;
    }

    public void setSafety(int safety) {
        this.safety = safety;
    }
}
