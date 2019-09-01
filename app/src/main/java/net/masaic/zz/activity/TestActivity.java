package net.masaic.zz.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.masaic.zz.R;
import net.masaic.zz.adapter.MyRecyclerViewAdapter;
import net.masaic.zz.bean.MacLogs;
import net.masaic.zz.biz.MacLogsBiz;
import net.masaic.zz.net.CommonCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity-app";
    private MacLogsBiz mMacLogsBiz = new MacLogsBiz();
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mRecyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new MyRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
/*                List<String> data = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    String s = "第";
                    data.add(s);
                }
                mAdapter.setDataSource(data);*/
            }
        });

    }

    public void post() {
        Map parmas = new HashMap();
        parmas.put("mac", "[c0:a6:00:73:c6:b2, 70:89:cc:c8:17:93, 30:b4:9e:d4:31:d7]");
        Log.d(TAG, "post: " + parmas);
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
}
