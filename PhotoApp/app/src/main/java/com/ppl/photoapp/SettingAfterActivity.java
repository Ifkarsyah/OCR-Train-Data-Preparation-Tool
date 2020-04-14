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

    Switch settingAdjustBorder;
    EditText etPaddingSize;

    Switch settingDeleteNoise;
    EditText etNoiseThreshold;

    Switch settingDilate;
    EditText etDilationFactor;

    Switch settingErode;
    EditText etErosionFactor;

    RadioGroup settingColorMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_after);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setTitle("After Settings");

        settingColorMode = findViewById(R.id.settingColorMode);

        settingAdjustBorder = findViewById(R.id.settingAdjustBorder);
        etPaddingSize = findViewById(R.id.settingPaddingSize);

        settingDeleteNoise = findViewById(R.id.settingDeleteNoise);
        etNoiseThreshold = findViewById(R.id.settingNoiseThreshold);

        settingDilate = findViewById(R.id.settingDilate);
        etDilationFactor = findViewById(R.id.settingDilateFactor);

        settingErode = findViewById(R.id.settingErode);
        etErosionFactor = findViewById(R.id.settingErodeFactor);

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
        settingDeleteNoise.setChecked(Global.settingDeleteNoise);
        etNoiseThreshold.setText(Integer.toString(Global.noiseThreshold));

        settingAdjustBorder.setChecked(Global.settingAdjustBorder);
        etPaddingSize.setText(Integer.toString(Global.paddingSize));

        settingDilate.setChecked(Global.settingDilation);
        etDilationFactor.setText(Integer.toString(Global.dilationFactor));

        settingErode.setChecked(Global.settingErosion);
        etErosionFactor.setText(Integer.toString(Global.erosionFactor));
    }

    public void finishActivity(View view) {
        Global.isSettingsChanged = true;


        Global.settingAdjustBorder = settingAdjustBorder.isChecked();
        Global.paddingSize = Integer.parseInt(etPaddingSize.getText().toString());

        Global.settingDeleteNoise = settingDeleteNoise.isChecked();
        Global.noiseThreshold = Integer.parseInt(etNoiseThreshold.getText().toString());

        Global.settingDilation = settingDilate.isChecked();
        Global.dilationFactor = Integer.parseInt(etDilationFactor.getText().toString());

        Global.settingErosion = settingErode.isChecked();
        Global.erosionFactor = Integer.parseInt(etErosionFactor.getText().toString());

        finish();
    }
}
