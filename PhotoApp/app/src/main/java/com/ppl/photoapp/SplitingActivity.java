package com.ppl.photoapp;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ppl.photoapp.Adapter.NumberAdapter;
import com.ppl.photoapp.Adapter.SplitingVerticalAdapter;
import com.ppl.photoapp.Config.Config;
import com.ppl.photoapp.Config.FormatNameFile;
import com.ppl.photoapp.GlobalVariable.Global;
import com.ppl.photoapp.Model.LabeledBitmapArray;
import com.ppl.photoapp.OpenCV.OpenCV;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class SplitingActivity extends AppCompatActivity {

    ArrayList<LabeledBitmapArray> arrLabeledBitmap;
    RecyclerView recyclerViewVertical;
    SplitingVerticalAdapter splitingVerticalAdapter;
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spliting);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setTitle("Result Spliting");

        progressDialog = new ProgressDialog(this) ;
        progressDialog.setMessage("Spliting");
        progressDialog.setCancelable(false) ;

        CheckInputBitmap() ;
        GetLabeledBitmap() ;
        ButtonSave() ;
//        SetSplitingView() ;
    }

    public void DeleteSingleItem(final int positionVertical,final int positionHorizontal){
        arrLabeledBitmap.get(positionVertical).deleteItem(positionHorizontal) ;
        if (arrLabeledBitmap.get(positionVertical).getBitmap().length == 0){
            arrLabeledBitmap.remove(positionVertical) ;
        }
        UpdateSplitingView();
    }

    public void DeleteRow(int position){
        arrLabeledBitmap.remove(position) ;
        UpdateSplitingView();
    }

    public void UpdateSplitingView(){
        splitingVerticalAdapter.notifyDataSetChanged();
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
                SaveImages() ;
            }
        });
    }

    void SaveImages(){
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File folderRoot = new File(FormatNameFile.RootFolder(root) );
        folderRoot.mkdirs() ;
        String date = System.currentTimeMillis() + "" ;
        for(int i = 0 ; i < arrLabeledBitmap.size() ; i ++ ){
            int label = arrLabeledBitmap.get(i).getLabel() ;
            File subFolder = new File(folderRoot + "/" + FormatNameFile.SubFolder(label) );
            subFolder.mkdirs() ;
            for(int j = 0 ; j < arrLabeledBitmap.get(i).getBitmap().length ; j ++){
                Bitmap bitmap = arrLabeledBitmap.get(i).getBitmap()[j] ;
                File file = new File(subFolder,FormatNameFile.NamingSavedFile(date,j)) ;
                SaveSingleImage(file,bitmap);
            }
        }

        finish();
    }

    void SaveSingleImage(File file,Bitmap imagePhoto)
    {
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            imagePhoto.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {

        }
    }

    void GetLabeledBitmap(){
        arrLabeledBitmap = new ArrayList<>() ;
        new openCV_Async().execute(Global.bitmap) ;
    }

    private class openCV_Async extends AsyncTask<Bitmap, Integer, ArrayList<LabeledBitmapArray>> {
        @Override
        protected void onPreExecute() {
            //Start Loading
            progressDialog.show();
        }

        @Override
        protected ArrayList<LabeledBitmapArray> doInBackground(Bitmap... bitmaps) {
            ArrayList<Bitmap> arrBitmap = new ArrayList<>() ;
            arrBitmap = OpenCV.getArrayBitmap(bitmaps[0]) ;
            ArrayList<LabeledBitmapArray> temp = OpenCV.mappingBitmap(arrBitmap) ;
            return temp;
        }

        @Override
        protected void onPostExecute(ArrayList<LabeledBitmapArray> labeledBitmapArrays) {
            arrLabeledBitmap = labeledBitmapArrays ;
            SetSplitingView() ;
            //End Loading
            progressDialog.dismiss();
        }
    }

    void CheckInputBitmap(){
        if (Global.bitmap == null){
            finish();
        }
    }
}
