package net.masaic.zz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.masaic.zz.R;
import net.masaic.zz.bean.MacLogs;

import java.util.ArrayList;
import java.util.List;

/**
 * 1.继承 RecyclerView.Adapter
 * 2. 绑定 ViewHolder
 * 3. 实现Adapter 的相关方法
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter
        .MyViewHolder> {

    private static final String TAG = "MyRecyclerViewAdapter-app";
    private List<String> dataSource;
    private List<MacLogs> mData;
    private Context mContext;

    public MyRecyclerViewAdapter() {
    }

    public MyRecyclerViewAdapter(Context context) {
        this.mContext = context;
        this.dataSource = new ArrayList<>();
        this.mData = new ArrayList<>();
    }

    public void setData(List<MacLogs> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, viewGroup,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        myViewHolder.mIcon.setImageResource(getIcon(mData.get(position).getIcon()));
        myViewHolder.mName.setText(mData.get(position).getName());
        myViewHolder.mSafety.setText(mData.get(position).getSafety());
    }

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

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mSafety;
        ImageView mIcon;
        TextView mName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mIcon = itemView.findViewById(R.id.icon);
            mSafety = itemView.findViewById(R.id.safety);
        }
    }
}
