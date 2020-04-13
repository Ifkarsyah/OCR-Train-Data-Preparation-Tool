package com.ppl.photoapp;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ppl.photoapp.Adapter.GalleryAdapter;
import com.ppl.photoapp.Adapter.ViewAllAdapter;
import com.ppl.photoapp.GlobalVariable.Global;
import com.ppl.photoapp.Model.LabeledBitmapArray;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewAllActivity extends AppCompatActivity {

    Button btnBack ;

    RecyclerView recyclerView ;
    ViewAllAdapter viewAllAdapter ;
    ArrayList<Bitmap> arrayList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();

        if (Global.labeledBitmap == null){
            finish();
        }

        btnBack = findViewById(R.id.btnBack) ;
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Init();
    }

    void Init(){
        arrayList = new ArrayList<>() ;

        for(int i = 0 ; i < Global.labeledBitmap.size() ; i ++){
            LabeledBitmapArray labeledBitmapArray = Global.labeledBitmap.get(i) ;
            Bitmap[] bitmaps = labeledBitmapArray.getBitmap() ;
            arrayList.addAll(Arrays.asList(bitmaps));
        }

        recyclerView = findViewById(R.id.recyclerView) ;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,7));

        viewAllAdapter = new ViewAllAdapter(this,arrayList) ;
        viewAllAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(viewAllAdapter);
    }
}
