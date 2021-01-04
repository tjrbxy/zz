package net.masaic.zz.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MacLogs {

    private int is_marked;
    private int checktime;
    private double latitude;
    private double longitude;
    private String address;
    private String mac;
    private String name;
    private int icon;
    private String safety;
}
