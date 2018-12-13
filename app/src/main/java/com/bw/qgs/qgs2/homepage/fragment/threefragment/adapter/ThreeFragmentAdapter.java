package com.bw.qgs.qgs2.homepage.fragment.threefragment.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bw.qgs.qgs2.R;
import com.bw.qgs.qgs2.homepage.fragment.threefragment.user.ThreeFragmentUser;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * date:2018/12/13    19:17
 * author:秦广帅(Lenovo)
 * fileName:ThreeFragmentAdapter
 */
public class ThreeFragmentAdapter extends RecyclerView.Adapter<ThreeFragmentAdapter.MyViewHolder> {

    private Context mContext;
    private List<ThreeFragmentUser.ResultBean> mResult1;

    public ThreeFragmentAdapter(Context context, List<ThreeFragmentUser.ResultBean> result1) {
        mContext = context;
        mResult1 = result1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.threeshop, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ThreeFragmentUser.ResultBean bean = mResult1.get(i);
        Uri uri = Uri.parse(bean.getPic());
        myViewHolder.threesimple.setImageURI(uri);
        myViewHolder.threett1.setText(bean.getCommodityName());
        myViewHolder.threett2.setText(bean.getPrice()+"");
    }

    @Override
    public int getItemCount() {
        return mResult1.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView threesimple;
        TextView threett1,threett2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            threesimple = itemView.findViewById(R.id.threesimple);
            threett1 = itemView.findViewById(R.id.threett1);
            threett2 = itemView.findViewById(R.id.threett2);
        }
    }
}