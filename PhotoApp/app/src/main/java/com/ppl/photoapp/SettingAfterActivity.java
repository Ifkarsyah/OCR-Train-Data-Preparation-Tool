package com.ppl.photoapp;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.ppl.photoapp.GlobalVariable.Global;

public class SettingAfterActivity extends AppCompatActivity {

    Switch settingDeleteNoise;
    Switch settingAdjustBorder;
    RadioGroup settingColorMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_after);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setTitle("After Settings");

        settingDeleteNoise = findViewById(R.id.settingDeleteNoise);
        settingAdjustBorder = findViewById(R.id.settingAdjustBorder);
        settingColorMode = findViewById(R.id.settingColorMode);

        getCurrentValue();
        settingColorMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.rb_color:
                        Global.settingColorMode = 0; break;
                    case R.id.rb_grayscale:
                        Global.settingColorMode = 1; break;
                    case R.id.rb_bw:
                        Global.settingColorMode = 2; break;
                }
            }
        });
    }

    private void getCurrentValue() {
        switch (Global.settingColorMode) {
            case 0:
                settingColorMode.check(R.id.rb_color); break;
            case 1:
                settingColorMode.check(R.id.rb_grayscale); break;
            case 2:
                settingColorMode.check(R.id.rb_bw); break;
        }
        settingAdjustBorder.setChecked(Global.settingAdjustBorder);
        settingDeleteNoise.setChecked(Global.settingDeleteNoise);
    }

    public void finishActivity(View view) {
        Global.isSettingsChanged = true;
        Global.settingDeleteNoise = settingDeleteNoise.isChecked();
        Global.settingAdjustBorder = settingAdjustBorder.isChecked();
        finish();
    }
}
