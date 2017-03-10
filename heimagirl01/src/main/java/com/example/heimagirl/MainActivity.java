package com.example.heimagirl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.list_view)
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        new Thread(new Runnable() {
            @Override
            public void run() {

               Request request= new Request.Builder().get().url(url).build();
            }
        }).start();
    }
}
