package com.ppl.photoapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ppl.photoapp.Bitmap.BitmapOperation;
import com.ppl.photoapp.GlobalVariable.Global;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ResultActivity extends AppCompatActivity {

    Uri imageUri ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setTitle("Result");
        OpenCameraOrGallery() ;
        ButtonTakeAnotherPicture() ;
        ButtonApply() ;
    }

    void ButtonApply(){
        ImageView ivApply = findViewById(R.id.ivApply) ;
        ivApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SplitingActivity.class) ;
                startActivity(intent);
            }
        });
    }

    void ButtonTakeAnotherPicture(){
        Button btnTakeAnotherPicture = findViewById(R.id.btnTakePicture) ;
        btnTakeAnotherPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenCameraOrGallery() ;
            }
        });
    }

    void OpenCameraOrGallery(){
        CropImage.activity().start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  && resultCode == RESULT_OK )
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data) ;
            imageUri = result.getUri() ;

            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver() , imageUri);
                bitmap = BitmapOperation.toGrayscale(bitmap) ;
                ImageView imageView = findViewById(R.id.imageView) ;
                imageView.setImageBitmap(bitmap);
                Global.bitmap = bitmap ;
            }
            catch (Exception e)
            {

            }

        }
        else
        {
            if (imageUri == null){
                finish();
            }
        }
    }


}
