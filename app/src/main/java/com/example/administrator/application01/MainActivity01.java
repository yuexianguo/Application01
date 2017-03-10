package com.example.administrator.application01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity01 extends AppCompatActivity {

    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main01);
        TextView viewById = (TextView) findViewById(R.id.tv);
        test();
    }

    public void test() {
        hh.print();
    }
}
