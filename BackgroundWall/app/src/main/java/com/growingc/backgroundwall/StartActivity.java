package com.growingc.backgroundwall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_1:
                startActivity(new Intent(StartActivity.this, StaticRecActivity.class));
                break;
            case R.id.button_2:
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                break;
            default:
                break;
        }
    }
}
