package com.example.xrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianghejie on 15/11/26.
 */
public class MyStaggeredAdapter extends RecyclerView.Adapter<MyStaggeredAdapter.ViewHolder> {
    private RecyclerView recyclerView;
    public ArrayList<String> datas = null;
    public List<Integer> mHeights;

    public MyStaggeredAdapter(RecyclerView recyclerView, ArrayList<String> datas) {
        this.recyclerView = recyclerView;
        this.datas = datas;
        initRandomH();
    }

    public void addRandomH() {
        if (mHeights == null) return;
        if (mHeights.size() == 0) return;
        for (int i = mHeights.size(); i < datas.size(); i++) {
            mHeights.add((int) (Math.random() * 500) + 200);
        }
    }

    public void initRandomH() {
        if (mHeights == null)
            mHeights = new ArrayList<>();
        if (mHeights.size() > 0)
            mHeights.clear();
        for (int i = 0; i < datas.size(); i++) {
            mHeights.add((int) (Math.random() * 500) + 200);
        }
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ViewGroup.LayoutParams mLayoutParams = viewHolder.itemView.getLayoutParams();
        mLayoutParams.height = mHeights.get(position);
        viewHolder.itemView.setLayoutParams(mLayoutParams);
        viewHolder.mTextView.setText(datas.get(position));
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.text);
        }
    }
}
