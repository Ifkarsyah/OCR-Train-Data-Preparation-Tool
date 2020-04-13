package com.ppl.photoapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ppl.photoapp.Adapter.SplittingVerticalAdapter;
import com.ppl.photoapp.Config.FormatNameFile;
import com.ppl.photoapp.GlobalVariable.Global;
import com.ppl.photoapp.Model.LabeledBitmapArray;
import com.ppl.photoapp.OpenCV.OpenCV;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class SplittingActivity extends AppCompatActivity {

    ArrayList<LabeledBitmapArray> arrLabeledBitmap;
    RecyclerView recyclerViewVertical;
    SplittingVerticalAdapter splittingVerticalAdapter;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splitting);

//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setTitle("Result Splitting");

        progressDialog = new ProgressDialog(this) ;
        progressDialog.setMessage("Splitting");
        progressDialog.setCancelable(false) ;

        CheckInputBitmap() ;
        GetLabeledBitmap() ;
        GotoSettingAfterActivity();
        ButtonSave() ;
        ButtonViewAll() ;
    }

    void ButtonViewAll(){
        Button btnViewAll = findViewById(R.id.btnViewAll) ;
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ViewAllActivity.class) ;
                Global.labeledBitmap = arrLabeledBitmap ;
                startActivity(intent);
            }
        });
    }

    public void DeleteSingleItem(final int positionVertical,final int positionHorizontal){
        arrLabeledBitmap.get(positionVertical).deleteItem(positionHorizontal) ;
        if (arrLabeledBitmap.get(positionVertical).getBitmap().length == 0){
            arrLabeledBitmap.remove(positionVertical) ;
        }
        UpdateSplittingView();
    }

    public void DeleteRow(int position){
        arrLabeledBitmap.remove(position) ;
        UpdateSplittingView();
    }

    public void UpdateSplittingView(){
        splittingVerticalAdapter.notifyDataSetChanged();
    }

    void SetSplittingView(){
        recyclerViewVertical = findViewById(R.id.recyclerViewSplittingVertical) ;
        recyclerViewVertical.setHasFixedSize(true);
        recyclerViewVertical.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        splittingVerticalAdapter = new SplittingVerticalAdapter(this,arrLabeledBitmap,this) ;
        splittingVerticalAdapter.notifyDataSetChanged();
        recyclerViewVertical.setAdapter(splittingVerticalAdapter);
    }

    void ButtonSave(){
        Button btnSave = findViewById(R.id.btnSave) ;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                File folderRoot = new File(FormatNameFile.RootFolder(root) );
                folderRoot.mkdirs() ;

                new saveImages_Async(SplittingActivity.this,System.currentTimeMillis() + "",folderRoot.toString() )
                        .execute(arrLabeledBitmap) ;
            }
        });
    }

    private class saveImages_Async extends AsyncTask<ArrayList<LabeledBitmapArray>, Integer, String> {
        Context context ;
        ProgressDialog progressDialogSave;
        String date ;
        String folderRoot ;

        public saveImages_Async(Context context,String date,String folderRoot) {
            this.context = context ;
            this.progressDialogSave = new ProgressDialog(context) ;
            this.progressDialogSave.setMessage("Save");
            this.progressDialogSave.setCancelable(false);
            this.date = date ;
            this.folderRoot = folderRoot ;
        }

        @Override
        protected void onPreExecute() {
             progressDialogSave.show();
        }

        @Override
        protected String doInBackground(ArrayList<LabeledBitmapArray>... arrayLists) {
            ArrayList<LabeledBitmapArray> arrLabeledBitmap_local = arrayLists[0] ;
            for(int i = 0 ; i < arrLabeledBitmap_local.size() ; i ++ ){
                int label = arrLabeledBitmap_local.get(i).getLabel() ;
                File subFolder = new File(folderRoot + "/" + FormatNameFile.SubFolder(label) );
                subFolder.mkdirs() ;
                for(int j = 0 ; j < arrLabeledBitmap_local.get(i).getBitmap().length ; j ++){
                    Bitmap bitmap = arrLabeledBitmap_local.get(i).getBitmap()[j] ;
                    File file = new File(subFolder,FormatNameFile.NamingSavedFile(date,j)) ;
                    SaveSingleImage(file,bitmap);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(String s) {
            progressDialogSave.dismiss();
            finish();
        }
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

            // Prepossesses
            int setColorMode = Global.settingColorMode;
            bitmaps[0] = OpenCV.setColorModeBitmap(bitmaps[0], setColorMode);


            arrBitmap = OpenCV.getArrayBitmap(bitmaps[0]);

            for (int i = 0; i < arrBitmap.size(); i++){
                if (Global.settingDeleteNoise){
                    arrBitmap.set(i, OpenCV.deleteNoise(arrBitmap.get(i)));
                }
                if (Global.settingAdjustBorder){
                    arrBitmap.set(i, OpenCV.adjustPaddingBorder(arrBitmap.get(i)));
                }
                if (Global.settingErosion){
                    arrBitmap.set(i,OpenCV.erode(arrBitmap.get(i)));
                }
                if (Global.settingDilation){
                    arrBitmap.set(i,OpenCV.dilate(arrBitmap.get(i)));
                }
            }



            ArrayList<LabeledBitmapArray> temp = OpenCV.mappingBitmap(arrBitmap) ;
            return temp;
        }

        @Override
        protected void onPostExecute(ArrayList<LabeledBitmapArray> labeledBitmapArrays) {
            arrLabeledBitmap = labeledBitmapArrays ;
            SetSplittingView() ;
            //End Loading
            progressDialog.dismiss();
        }
    }

    void CheckInputBitmap(){
        if (Global.bitmap == null){
            finish();
        }
    }

    void GotoSettingAfterActivity(){
        ImageView ivSetting = findViewById(R.id.ivSettingAfter) ;
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingAfterActivity.class);
                startActivity(intent);
            }
        });
    }
}
