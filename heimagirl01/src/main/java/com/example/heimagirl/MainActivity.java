package com.example.heimagirl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String  TAG = "MainActivity";
    @BindView(R.id.list_view)
    ListView mListView;
    private Gson mGson=new Gson();
    private boolean isLoading=false;
    private List<ResultBean.ResultsBean> mList = new ArrayList<ResultBean.ResultsBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mListView.setAdapter(mBaseAdapter);
        //sendSyncRequest();
        sendAsyncRequest();
        mListView.setOnScrollListener(mOnScrollListener);


    }
    private AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener(){
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                if(mListView.getLastVisiblePosition()==mList.size()-1 && isLoading==false){
                    loadMoreData();
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };
    //加载更多的数据：
    private void loadMoreData() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://gank.io/api/data/福利/10/"+mList.size()/10+1;
        isLoading=true;
        Request request = new Request.Builder().get().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            //子线程调用
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                ResultBean resultBean = mGson.fromJson(result, ResultBean.class);
                Log.d(TAG, "onResponse: "+resultBean.getResults().get(0).getUrl());
                mList.addAll(resultBean.getResults());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBaseAdapter.notifyDataSetChanged();
                    }
                });
                isLoading=false;
            }
        });

    }


    private void sendAsyncRequest() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://gank.io/api/data/福利/10/1";
        Request request = new Request.Builder().get().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                
            }
                //子线程调用
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                ResultBean resultBean = mGson.fromJson(result, ResultBean.class);
                Log.d(TAG, "onResponse: "+resultBean.getResults().get(0).getUrl());
                mList.addAll(resultBean.getResults());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBaseAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        Log.d(TAG, "getSendAsyncRequest: ");
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
    private BaseAdapter mBaseAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                convertView = View.inflate(MainActivity.this,R.layout.view_list_item,null);
                viewHolder= new ViewHolder(convertView);
                convertView.setTag(viewHolder);

            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            //results的信息集合的position位置的那个信息对象：
            ResultBean.ResultsBean resultsBean = mList.get(position);
            String publishedAt = resultsBean.getPublishedAt();
            //更新发布时间
            viewHolder.mTextView.setText(publishedAt);
            //刷新图片
            String url = resultsBean.getUrl();
            Glide.with(MainActivity.this).load(url).centerCrop().
                    bitmapTransform(new CropCircleTransformation(MainActivity.this)).
                    into(viewHolder.mImageView);

            return convertView;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


    };
    public  class ViewHolder{
        ImageView mImageView;
        TextView mTextView;
        public ViewHolder(View root){
            mImageView  = (ImageView) root.findViewById(R.id.image);
            mTextView = (TextView) root.findViewById(R.id.publish_time);
        }

    }



}
