package net.masaic.zz.activity;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import net.masaic.zz.R;
import net.masaic.zz.adapter.MyRecyclerViewAdapter;
import net.masaic.zz.bean.MacLogs;
import net.masaic.zz.biz.MacLogsBiz;
import net.masaic.zz.net.CommonCallback;
import net.masaic.zz.utils.LocationUtils;
import net.masaic.zz.utils.T;
import net.masaic.zz.wifi_probe.WifiProbeManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity-app";
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mAdapter;
    private MacLogsBiz mMacLogsBiz = new MacLogsBiz();

    //wifi探针
    private WifiProbeManager mProbe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProbe = new WifiProbeManager();

        mRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        sendMac();
    }


    private void sendMac() {
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
}
