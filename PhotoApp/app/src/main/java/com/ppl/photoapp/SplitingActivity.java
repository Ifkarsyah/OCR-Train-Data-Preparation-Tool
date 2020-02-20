package com.ppl.photoapp;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ppl.photoapp.Adapter.NumberAdapter;
import com.ppl.photoapp.Adapter.SplitingVerticalAdapter;
import com.ppl.photoapp.Config.Config;
import com.ppl.photoapp.GlobalVariable.Global;
import com.ppl.photoapp.Model.LabeledBitmapArray;
import com.ppl.photoapp.OpenCV.OpenCV;

import java.util.ArrayList;

public class SplitingActivity extends AppCompatActivity {

    ArrayList<LabeledBitmapArray> arrLabeledBitmap;
    public RecyclerView recyclerViewVertical;
    SplitingVerticalAdapter splitingVerticalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spliting);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setTitle("Result Spliting");

        CheckInputBitmap() ;
        GetLabeledBitmap() ;
        ButtonSave() ;
        SetSplitingView() ;
    }

    void SetSplitingView(){
        recyclerViewVertical = findViewById(R.id.recyclerViewSplitingVertical) ;
        recyclerViewVertical.setHasFixedSize(true);
        recyclerViewVertical.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        splitingVerticalAdapter = new SplitingVerticalAdapter(this,arrLabeledBitmap,this) ;
        splitingVerticalAdapter.notifyDataSetChanged();
        recyclerViewVertical.setAdapter(splitingVerticalAdapter);
    }

    void ButtonSave(){
        Button btnSave = findViewById(R.id.btnSave) ;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
