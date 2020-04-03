package com.ppl.photoapp;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.ppl.photoapp.GlobalVariable.Global;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setTitle("Settings");


        final Switch settingDeleteNoise = findViewById(R.id.settingDeleteNoise);
        Switch settingAdjustBorder = findViewById(R.id.settingAdjustBorder);
        settingDeleteNoise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean isChecked){
                Global.settingDeleteNoise = isChecked;
            }
        });
        settingAdjustBorder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean isChecked){
                Global.settingAdjustBorder = isChecked;
            }
        });
    }

    public void finishActivity(View view) {
        finish();
    }
}
