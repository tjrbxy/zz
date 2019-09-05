package net.masaic.zz.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import net.masaic.zz.R;
import net.masaic.zz.adapter.BaseRecyclerViewAdapter;
import net.masaic.zz.adapter.MyViewHolder;
import net.masaic.zz.bean.MacLogs;
import net.masaic.zz.biz.MacLogsBiz;
import net.masaic.zz.net.CommonCallback;
import net.masaic.zz.utils.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestActivity extends AppCompatActivity implements AdapterView.OnClickListener {
    private static final String TAG = "TestActivity-app";
    private MacLogsBiz mMacLogsBiz = new MacLogsBiz();
    private RecyclerView mRecyclerView;
    private BaseRecyclerViewAdapter mBaseRecyclerViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mRecyclerView = findViewById(R.id.recycler_view);
        // 下面两个必须要有没有会不执行
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        List<MacLogs> mData = new ArrayList<>();

        mBaseRecyclerViewAdapter = new BaseRecyclerViewAdapter<MacLogs>(mData, R.layout.item) {

            @Override
            protected void onBindViewHolder(MyViewHolder holder, MacLogs model, int position) {
                holder.image(R.id.icon, R.drawable.icon_1);
                holder.text(R.id.name, model.getName());

            }
        };
        mRecyclerView.setAdapter(mBaseRecyclerViewAdapter);
    }

    public void post() {
        Map parmas = new HashMap();
        parmas.put("mac", "[c0:a6:00:73:c6:b2, 70:89:cc:c8:17:93, 30:b4:9e:d4:31:d7]");
        Log.d(TAG, "post: " + parmas);
        mMacLogsBiz.index(parmas, new CommonCallback<List<MacLogs>>() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(List<MacLogs> response, String info) {
                Log.d(TAG, "onSuccess: " + response);
                mBaseRecyclerViewAdapter.refresh(response);
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                post();
                T.showToast("click button");
                break;
            default:
                break;
        }
    }
}
