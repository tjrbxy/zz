package net.masaic.zz.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import net.masaic.zz.R;
import net.masaic.zz.adapter.MyRecyclerViewAdapter;
import net.masaic.zz.bean.MacLogs;
import net.masaic.zz.biz.MacLogsBiz;
import net.masaic.zz.net.CommonCallback;
import net.masaic.zz.utils.T;
import net.masaic.zz.wifi_probe.WifiProbeManager;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements TencentLocationListener {

    private static final String TAG = "MainActivity-app";
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mAdapter;
    private MacLogsBiz mMacLogsBiz = new MacLogsBiz();
    private double lat = 0;
    private double lng = 0;
    private boolean flag = true;

    //wifi探针
    private WifiProbeManager mProbe;

    // 定位
    private TencentLocationManager mLocationManager;
    final RxPermissions rxPermissions = new RxPermissions(this);

    // 地址
    private TextView tvLocation;
    private TextView tvAddress;
    private Geocoder geocoder;
    private List<Address> addressList;
    private StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 权限
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission.requestEach(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 已经同意该权限
                            initTencentLocationRequest();
                            initData();
                        } else {
                            // 拒绝了该权限
                        }
                    }
                });
        //this.requestPermissions();

        // 定位
        mLocationManager = TencentLocationManager.getInstance(this);
        /* 保证调整坐标系前已停止定位 */
        mLocationManager.removeUpdates(null);
        // 设置 wgs84 坐标系
        mLocationManager
                .setCoordinateType(TencentLocationManager.COORDINATE_TYPE_WGS84);

        mProbe = new WifiProbeManager();
        mRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    /***********************************/
    private void initData() {
        // 获取经纬度坐标
        // 1 获取位置管理者对象
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 2 通过lm获得经纬调度坐标
        // (参数： provider（定位方式 提供者 通过 LocationManager静态调用），
        // minTime（获取经纬度间隔的最小时间 时时刻刻获得传参数0），
        // minDistance（移动的最小间距 时时刻刻传0），LocationListener（监听）)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
        //LocationManager 每隔 5 秒钟会检测一下位置的变化情况，当移动距离超过 10 米的时候，
        // 就会调用 LocationListener 的 onLocationChanged() 方法，并把新的位置信息作为参数传入。
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // 获取经纬度主要方法
                lat = location.getLatitude();
                lng = location.getLongitude();
                // 发送地址
                sendMac();
                flag = false;
                Log.d(TAG, "latitude" + lat + "  " + "longitude" + lng);
                sb = new StringBuilder();
                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                addressList = new ArrayList<Address>();
                try {
                    // 返回集合对象泛型address
                    addressList = geocoder.getFromLocation(lat, lng, 1);
                    Log.d(TAG, "onLocationChanged: " + addressList);
                    if (addressList.size() > 0) {
                        Address address = addressList.get(0);
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }
                        sb.append(address.getFeatureName());//周边地址
                    }
                    Log.d(TAG, "onLocationChanged: " + "当前位置" + sb.toString());
                    // tvAddress.setText();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //状态发生改变监听
            }

            @Override
            public void onProviderEnabled(String s) {
                // GPS 开启的事件监听
            }

            @Override
            public void onProviderDisabled(String s) {
                // GPS 关闭的事件监听
            }
        });
    }

    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.SEND_SMS)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.d(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，而且选中『不再询问』
                            Log.d(TAG, permission.name + " is denied.");
                        }
                    }
                });
    }

    /**
     * 开启定位监听器
     */
    private void initTencentLocationRequest() {
        TencentLocationRequest request = TencentLocationRequest.create();

        request
                .setInterval(30000)
                .setRequestLevel(1)
                .setAllowCache(true);
        TencentLocationManager locationManager = TencentLocationManager.getInstance(this);
        int error = locationManager.requestLocationUpdates(request, this);

        if (error == 0) {
            Log.d(TAG, "注册位置监听器成功！");
        } else {
            Log.d(TAG, "注册位置监听器失败！");
        }
    }

    /**
     * 位置更新时的回调
     *
     * @param tencentLocation 新的位置
     * @param i               错误码
     * @param s               错误描述
     */
    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if (TencentLocation.ERROR_OK == i) {
            // 定位成功
            if (tencentLocation != null) {
                String lat = String.valueOf(tencentLocation.getLatitude());
                String lon = String.valueOf(tencentLocation.getLongitude());
                Log.d(TAG, lat + "---" + lon);
                T.showToast(lat + "---" + lon);
            }
        } else {
            // 定位失败
            T.showToast("定位失败");
        }
    }

    /***********************************/


    // 响应点击"开始"
    public void startLocation() {
        // 创建定位请求
        TencentLocationRequest request = TencentLocationRequest.create();
        // 修改定位请求参数, 定位周期 3000 ms
        request.setInterval(3000);
        // 开始定位
        mLocationManager.requestLocationUpdates(request, this);
        updateLocationStatus("开始定位: " + request + ", 坐标系=" + mLocationManager.getCoordinateType());
    }

    // 响应点击"停止"
    public void stopLocation() {
        mLocationManager.removeUpdates(this);
        updateLocationStatus("停止定位");
    }

    private void updateLocationStatus(String message) {
        Log.d(TAG, "updateLocationStatus: " + message + "\n---\n");
    }

    private void permissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            if (checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
        switch (requestCode) {
            case 0:
                if (PackageManager.PERMISSION_GRANTED != grantResults[0]) {
                    System.exit(0);
                }
                break;
        }
    }

    private void sendMac() {
        if (!flag) return;
        mProbe.startScan(new WifiProbeManager.MacListListener() {
            @Override
            public void macList(final List<String> macList) {
                //因为在线程中进行扫描的，所以要切换到主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "macList: " + macList);
                        JSONArray jsonArray = new JSONArray(macList);
                        Map parmas = new HashMap();
                        parmas.put("lat", lat + "");
                        parmas.put("lng", lng + "");
                        parmas.put("mac", jsonArray.toString());
                        mMacLogsBiz.insertMac(parmas, new CommonCallback<List<MacLogs>>() {
                            @Override
                            public void onError(Exception e) {
                            }

                            @Override
                            public void onSuccess(List<MacLogs> response, String info) {
                                Log.d(TAG, "onSuccess: " + response);
                                mAdapter.setData(response);
                            }
                        });

                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: " + item);
        switch (item.getItemId()) {
            case R.id.refresh:
                flag = true;
                sendMac();
                T.showToast("成功刷新！");
                break;
            default:
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStatusUpdate(String name, int status, String desc) {
        Log.d(TAG, "onLocationChanged: " + name + "," + status + "," + desc);
    }
}
