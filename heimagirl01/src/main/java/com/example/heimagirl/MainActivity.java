package com.example.heimagirl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String  TAG = "MainActivity";
    @BindView(R.id.list_view)
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        sendSyncRequest();
        getSendAsyncRequest();


    }

    private void getSendAsyncRequest() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://gank.io/api/data/福利/10/1";
        Request request = new Request.Builder().get().url(url).build();


    }

    private void sendSyncRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                //创建网络请求：
               // Log.d(TAG, "run: qingqiuwangluo");
                String url="http://gank.io/api/data/福利/10/1";
                Request request = new Request.Builder().get().url(url).build();
                try {
                    //同步请求，等待网络响应，后面代码不执行
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    Log.d(TAG, "sendSyncRequest: "+result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
        }).start();
    }
}
