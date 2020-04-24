package com.ppl.photoapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ppl.photoapp.GlobalVariable.Global;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class SettingBeforeActivity extends AppCompatActivity {

    EditText settingCountRow,settingPixelWidth,settingPixelHeigth;
//    EditText settingCountCol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_before);

        // this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setTitle("Settings");

        settingCountRow = findViewById(R.id.text_input_count_row);
//        settingCountCol = findViewById(R.id.text_input_count_col);
        settingPixelWidth = findViewById(R.id.text_input_pixel_width);
        settingPixelHeigth = findViewById(R.id.text_input_pixel_heigth);

        getCurrentValue();

        DownloadSamplePaper(R.id.ivDownloadPaper55, "Paper55.pdf");
        DownloadSamplePaper(R.id.ivDownloadPaper1010, "Paper1010.pdf");
        DownloadSamplePaper(R.id.ivDownloadPaper1414, "Paper1414.pdf");
    }

    private void getCurrentValue() {
        settingCountRow.setText(Integer.toString(Global.settingCountRow));
        settingPixelHeigth.setText(Integer.toString(Global.pixelHeight));
        settingPixelWidth.setText(Integer.toString(Global.pixelWidth));
    }

    public void finishActivity(View view) {
        Global.settingCountRow = Integer.parseInt(settingCountRow.getText().toString());
        Global.pixelHeight = Integer.parseInt(settingPixelHeigth.getText().toString());
        Global.pixelWidth = Integer.parseInt(settingPixelWidth.getText().toString());
        finish();
    }

    void DownloadSamplePaper(int resourceID, final String fileName) {
        Button btnDownloadPaper = findViewById(resourceID);
        btnDownloadPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFromAssets(fileName);
            }
        });
    }

    public void readFromAssets(String fileName) {
        AssetManager assetManager = getAssets();
        InputStream in = null;
        OutputStream out = null;
        Context mContext = SettingBeforeActivity.this;
        File file = new File(mContext.getFilesDir(), fileName);
        try {
            in = assetManager.open(fileName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                out = mContext.openFileOutput(file.getName(), Context.MODE_PRIVATE);
            } else {
                out = mContext.openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
            }
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
        Uri pdfFileURI;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pdfFileURI = FileProvider.getUriForFile(mContext,
                    BuildConfig.APPLICATION_ID + ".provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            pdfFileURI = Uri.parse("file://" + mContext.getFilesDir() + "/" + fileName);
        }
        intent.setDataAndType(pdfFileURI, "application/pdf");
        mContext.startActivity(intent);
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

}
