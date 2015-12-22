package com.example.xrecyclerview;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

public class StaggeredGridActivity extends AppCompatActivity {
    private XRecyclerView mRecyclerView;
    private MyStaggeredAdapter mAdapter;
    private ArrayList<String> listData;
    private int refreshTime = 0;
    private int times = 0;

    private int GRIDSPAN_COUNT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        mRecyclerView = (XRecyclerView)this.findViewById(R.id.recyclerview);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(GRIDSPAN_COUNT,
                StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        View header =   LayoutInflater.from(this).inflate(R.layout.recyclerview_header, (ViewGroup)findViewById(android.R.id.content),false);
        mRecyclerView.addHeaderView(header);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime ++;
                times = 0;
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        listData.clear();
                        for(int i = 0; i < 25 ;i++){
                            listData.add("item " + i + " after " + refreshTime + " times of refresh");
                        }
                        mAdapter.initRandomH();
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                if(times < 2){
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            mRecyclerView.loadMoreComplete();
                            for(int i = 0; i < 25 ;i++){
                                listData.add("item " + i);
                            }
                            mAdapter.addRandomH();
                            mRecyclerView.refreshComplete();
                            if(times==1) mRecyclerView.loadMoreComplete();
                        }
                    }, 1000);
                } else {
//                    new Handler().postDelayed(new Runnable() {
//                        public void run() {
//                            mRecyclerView.loadMoreComplete();
//                        }
//                    }, 1000);
                }
                times ++;
            }
        });

        listData = new  ArrayList<>();
        for(int i = 0; i < 25 ;i++){
            listData.add("item" + i);
        }
        mAdapter = new MyStaggeredAdapter(mRecyclerView, listData);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnItemClickListener(new XRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, RecyclerView.ViewHolder viewHolder, View view, int position) {
                int spanIndex = ((StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams()).getSpanIndex();
                Toast.makeText(getApplicationContext(), "root_item " + position + ",spanIndex=" + spanIndex, Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
