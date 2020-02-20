package com.ppl.photoapp;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ppl.photoapp.GlobalVariable.Global;
import com.ppl.photoapp.Model.LabeledBitmap;
import com.ppl.photoapp.OpenCV.OpenCV;

import java.util.ArrayList;

public class SplitingActivity extends AppCompatActivity {

    ArrayList<LabeledBitmap> arrLabeledBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spliting);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setTitle("Result Spliting");

        CheckInputBitmap() ;
        GetLabeledBitmap() ;

    }

    void GetLabeledBitmap(){
        arrLabeledBitmap = new ArrayList<>() ;
        ArrayList<Bitmap> arrBitmap = new ArrayList<>() ;
        arrBitmap = OpenCV.getArrayBitmap(Global.bitmap) ;
        arrLabeledBitmap = OpenCV.mappingBitmap(arrBitmap) ;


    }

    void CheckInputBitmap(){
        if (Global.bitmap == null){
            finish();
        }
    }
}
