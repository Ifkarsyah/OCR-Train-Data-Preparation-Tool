package com.ppl.photoapp;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.ppl.photoapp.GlobalVariable.Global;

import uk.co.senab.photoview.PhotoViewAttacher;

public class LookSplitImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_split_image);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();

        SetImage();
    }

    void SetImage(){
        if (Global.splittedBitmap == null){
            finish();
        }else {
            ImageView imageView = findViewById(R.id.imageView) ;
            PhotoViewAttacher photoView = new PhotoViewAttacher(imageView) ;
            photoView.update();
            imageView.setImageBitmap(Global.splittedBitmap) ;
        }
    }
}
