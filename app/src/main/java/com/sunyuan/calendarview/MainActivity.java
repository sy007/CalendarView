package com.sunyuan.calendarview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.ele.uetool.UETool;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UETool.showUETMenu();
        findViewById(R.id.btn_single_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SingleSelectActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_range_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RangeSelectActivity.class);
                startActivity(intent);
            }
        });
    }
}
