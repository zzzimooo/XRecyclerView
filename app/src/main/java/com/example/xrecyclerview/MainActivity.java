package com.example.xrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn);
        new OkHttpClient().newCall(new Request.Builder()
                .url("http://www.baidu.com")
                .get()
                .build())
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }
                    @Override
                    public void onResponse(Response response) throws IOException {
                        btn.setText("1");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "111", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
    public void gotoLinearActivity(View v) {
        Intent intent = new Intent();
        intent.setClass(this,LinearActivity.class);
        startActivity(intent);
    }
    public void gotoGridActivity(View v) {
        Intent intent = new Intent();
        intent.setClass(this,GridActivity.class);
        startActivity(intent);
    }
    public void gotoStaggeredGridActivity(View v) {
        Intent intent = new Intent();
        intent.setClass(this,StaggeredGridActivity.class);
        startActivity(intent);
    }

}
