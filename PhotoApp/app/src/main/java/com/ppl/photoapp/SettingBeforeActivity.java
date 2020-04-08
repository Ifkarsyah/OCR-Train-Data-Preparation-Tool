package com.ppl.photoapp;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.ppl.photoapp.GlobalVariable.Global;

public class SettingBeforeActivity extends AppCompatActivity {

    Switch settingDeleteNoise;
    Switch settingAdjustBorder;
    EditText settingCountRow;
    EditText settingCountCol;
    RadioGroup settingColorMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_before);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setTitle("Settings");



        settingCountRow = findViewById(R.id.text_input_count_row);
        settingCountCol = findViewById(R.id.text_input_count_col);

        getCurrentValue();
    }

    private void getCurrentValue() {
        settingCountRow.setText(Integer.toString(Global.settingCountRow));
        settingCountCol.setText(Integer.toString(Global.settingCountCol));
    }

    public void finishActivity(View view) {
        Global.settingCountRow = Integer.parseInt(settingCountRow.getText().toString());
        Global.settingCountCol = Integer.parseInt(settingCountCol.getText().toString());
        finish();
    }
}
