package com.ppl.photoapp;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.ppl.photoapp.GlobalVariable.Global;

import java.io.File;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class LookImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_image);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();

        SetImage();
    }

    void SetImage(){
        String intentPathBitmap = getIntent().getExtras().getString(Global.INTENT_PATH_BITMAP,"") ;
        if (intentPathBitmap.equals("")){
            finish();
        }else {
            File imgFile = new File(intentPathBitmap);
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView imageView = findViewById(R.id.imageView) ;
            PhotoViewAttacher photoView = new PhotoViewAttacher(imageView) ;
            photoView.update();
            imageView.setImageBitmap(myBitmap) ;
        }
    }
}
