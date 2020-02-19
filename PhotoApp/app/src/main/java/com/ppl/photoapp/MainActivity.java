package com.ppl.photoapp;

import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ppl.photoapp.Fragment.CameraFragment;
import com.ppl.photoapp.Fragment.GalleryFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();

        BottomNavigationView navigationView = findViewById(R.id.navigation) ;
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        //Default
        CameraFragment fragment = new CameraFragment() ;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction() ;
        ft.replace(R.id.content,fragment,"") ;
        ft.commit() ;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.nav_camera :
                            CameraFragment cameraFragment = new CameraFragment() ;
                            FragmentTransaction cameraFragmentTransaction = getSupportFragmentManager().beginTransaction() ;
                            cameraFragmentTransaction.replace(R.id.content,cameraFragment,"") ;
                            cameraFragmentTransaction.commit() ;
                            return true ;
                        case R.id.nav_gallery :
                            GalleryFragment galleryFragment = new GalleryFragment() ;
                            FragmentTransaction galleryFragmentTransaction = getSupportFragmentManager().beginTransaction() ;
                            galleryFragmentTransaction.replace(R.id.content,galleryFragment,"") ;
                            galleryFragmentTransaction.commit() ;
                            return true ;
                    }

                    return false;
                }
            } ;
}
