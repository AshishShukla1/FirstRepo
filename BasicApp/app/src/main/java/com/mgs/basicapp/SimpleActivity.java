package com.mgs.basicapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SimpleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        if(getIntent().getExtras()!=null){
            Bundle b=getIntent().getExtras();
            String abc1=b.getString("First");
            String abc2=b.getString("Sec")==null?"":abc1;
            Toast.makeText(SimpleActivity.this,abc1+"==="+abc2,Toast.LENGTH_SHORT).show();
        }
    }
}
