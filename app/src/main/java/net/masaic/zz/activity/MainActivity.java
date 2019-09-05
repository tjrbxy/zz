package net.masaic.zz.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxTool;
import com.vondear.rxtool.interfaces.OnSimpleListener;

import net.masaic.zz.R;
import net.masaic.zz.adapter.BaseRecyclerViewAdapter;
import net.masaic.zz.adapter.MyViewHolder;
import net.masaic.zz.bean.MacLogs;
import net.masaic.zz.biz.MacLogsBiz;
import net.masaic.zz.net.CommonCallback;
import net.masaic.zz.utils.T;
import net.masaic.zz.wifi_probe.WifiProbeManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity-app";
    private RecyclerView mRecyclerView;
    private BaseRecyclerViewAdapter mBaseAdapter;
    private MacLogsBiz mMacLogsBiz = new MacLogsBiz();
    private List<MacLogs> mMacListLogs = new ArrayList<>();

    private double lat = 0;
    private double lng = 0;
    private boolean flag = true;
    //wifi探针
    private int WiFi_time = 30 * 1000;//30秒扫描一次
    private WifiProbeManager mProbe;
    private List mMacList = new ArrayList<>();
    private Timer mTimer;
    // 权限
    final RxPermissions rxPermissions = new RxPermissions(this);
    private WebSettings settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // WebView
        initWebView();
        // WIFI
        mProbe = new WifiProbeManager();
        // 检测Mac
        initScan();
        // 权限
        rxPermissions.requestEach(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 已经同意该权限
                            Log.d(TAG, "已同意授权");
                            initData();
                        } else {
                            // 拒绝了该权限
                            T.Toast("本程序2秒后退出，请允许获取地理位置");
                            // System.exit(0);
                            RxTool.delayToDo(2000, new OnSimpleListener() {
                                @Override
                                public void doSomething() {
                                    System.exit(0);
                                }
                            });

                        }
                    }
                });
        initRecyclerView();
    }

    private void initRecyclerView() {
        // 列表
        mRecyclerView = findViewById(R.id.recycler_view);
        //  RecyclerView 下面两行如果没有 数据不显示
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // 适配器
        mBaseAdapter = new BaseRecyclerViewAdapter<MacLogs>(mMacListLogs, R.layout.item, this) {
            @Override
            protected void onBindViewHolder(MyViewHolder holder, MacLogs model, int position) {
                holder.text(R.id.grade, model.getSafety());
                holder.text(R.id.name, model.getName());
                holder.image(R.id.icon, getIcon(model.getIcon()));
            }
        };
        mRecyclerView.setAdapter(mBaseAdapter);
    }
    //<editor-fold desc="WebView">

    /**
     * WebView
     */
    private void initWebView() {
        WebView webView = findViewById(R.id.web_view);//绑定ID
        webView.setWebViewClient(new WebViewClient());//添加WebViewClient实例
        settings = webView.getSettings();
        /**关闭webview中缓存**/
        settings.setJavaScriptEnabled(true); // 支持js
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setTextZoom(100);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.loadUrl("https://zz.masaic.net/web.html");//添加浏览器地址
    }
    //</editor-fold>

    /**
     * 检测mac
     */
    private void initScan() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                startScan();
            }
        }, 0, WiFi_time);
    }

    private void startScan() {
        Log.d(TAG, "startScan: ");
        mProbe.startScan(new WifiProbeManager.MacListListener() {
            @Override
            public void macList(final List<String> macList) {
                //因为在线程中进行扫描的，所以要切换到主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMacList.clear();
                        mMacList.addAll(macList);
                        Log.d(TAG, "获取到的Mac列表：" + mMacList);
                    }
                });
            }
        });
    }

    private void initSendMac() {
        // mAdapter.setData(mMacListLogs);
        if (mMacList.size() > 0) {
            JSONArray jsonArray = new JSONArray(mMacList);
            Map parmas = new HashMap();
            parmas.put("lat", lat + "");
            parmas.put("lng", lng + "");
            parmas.put("mac", jsonArray.toString());
            Log.d(TAG, "initSendMac: " + jsonArray.toString());
            mMacLogsBiz.insertMac(parmas, new CommonCallback<List<MacLogs>>() {
                @Override
                public void onError(Exception e) {
                    T.Toast(e.getMessage());
                    return;
                }

                @Override
                public void onSuccess(List<MacLogs> response, String info) {
                    Log.d(TAG, "onSuccess: " + response);
                    // mAdapter.setData(response);
                    // 刷新数据
                    mBaseAdapter.refresh(response);
                }
            });
        }

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                // T.showToast("获取经纬度");
                // 获取经纬度主要方法
                lat = location.getLatitude();
                lng = location.getLongitude();
                // 发送地址
                // T.showToast("latitude:" + lat + "  " + "longitude:" + lng);
                // if (flag) initSendMac();
                Log.d(TAG, "latitude:" + lat + "  " + "longitude:" + lng);
/*                Log.d(TAG, "latitude" + lat + "  " + "longitude" + lng);
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
                }*/
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //状态发生改变监听
                // T.showToast("状态发生改变监听");
            }

            @Override
            public void onProviderEnabled(String s) {
                // GPS 开启的事件监听
                // T.showToast("GPS 开启的事件监听");
            }

            @Override
            public void onProviderDisabled(String s) {
                // GPS 关闭的事件监听
                // T.showToast("GPS 关闭的事件监听");
            }
        });
    }

    // <editor-fold desc="Menu">
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                // 发送
                initSendMac();
                T.showToast("成功刷新！");
                break;
            default:
        }
        return true;
    }
    //</editor-fold>

    //<editor-fold desc="MainActivity">
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //</editor-fold>

    //<editor-fold desc="Event">
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check:
                if (mMacList.size() == 0) {
                    T.showToast("30秒后重试！");
                    return;
                }
                initSendMac();
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemClick: parent" + parent + ", view" + view + ", position" + position +
                ", id" + id);
        T.Toast("免费版无此功能，请购买商业版！", 3000);
    }
    //</editor-fold>


    private int getIcon(int position) {
        switch (position) {
            case 1:
                return R.drawable.icon_1;
            case 2:
                return R.drawable.icon_2;
            case 3:
                return R.drawable.icon_3;
            case 4:
                return R.drawable.icon_4;
            case 5:
                return R.drawable.icon_5;
            case 6:
                return R.drawable.icon_6;
            case 7:
                return R.drawable.icon_7;
            case 8:
                return R.drawable.icon_8;
            case 9:
                return R.drawable.icon_9;
            default:
                return R.drawable.icon_1;
        }
    }
}
