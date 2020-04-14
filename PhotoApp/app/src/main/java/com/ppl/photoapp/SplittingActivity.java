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
import android.util.Log;
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
    public static String TAG = "SplittingActivity.java";

    ArrayList<LabeledBitmapArray> arrLabeledBitmapInitial;
    ArrayList<LabeledBitmapArray> arrLabeledBitmap;
    RecyclerView recyclerViewVertical;
    SplittingVerticalAdapter splittingVerticalAdapter;
    ProgressDialog progressDialog;

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

    @Override
    protected void onResume(){
        super.onResume();
        if (Global.isSettingsChanged){
            Global.isSettingsChanged = false;
            new updateSettings_Async().execute();
        }
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
//            finish();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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
        arrLabeledBitmap = new ArrayList<>();
        new openCV_Async().execute(Global.bitmap);
    }

    private class openCV_Async extends AsyncTask<Bitmap, Integer, ArrayList<LabeledBitmapArray>> {
        @Override
        protected void onPreExecute() {
            //Start Loading
            progressDialog.show();
        }

        @Override
        protected ArrayList<LabeledBitmapArray> doInBackground(Bitmap... bitmaps) {
            ArrayList<Bitmap> arrBitmap = OpenCV.getArrayBitmap(bitmaps[0]);
            return OpenCV.mappingBitmap(arrBitmap);
        }

        @Override
        protected void onPostExecute(ArrayList<LabeledBitmapArray> labeledBitmapArrays) {
            arrLabeledBitmap = new ArrayList<>();
            arrLabeledBitmapInitial = labeledBitmapArrays;
            new updateSettings_Async().execute();
            SetSplittingView() ;
            //End Loading
            progressDialog.dismiss();
        }
    }

    private class updateSettings_Async extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params){
            Log.d(TAG, "updateSettings_Async");
            arrLabeledBitmap.clear();
            for (LabeledBitmapArray tmp : arrLabeledBitmapInitial){
                Bitmap[] bitmaps = tmp.getBitmap();
                Bitmap[] tmpBitmaps = new Bitmap[bitmaps.length];
                for (int i = 0; i < bitmaps.length; i++) {
                    Bitmap currentBitmap = bitmaps[i];
                    if (currentBitmap == null) continue;
                    currentBitmap = OpenCV.setColorModeBitmap(currentBitmap, Global.settingColorMode);
                    currentBitmap = Global.settingDeleteNoise ? OpenCV.deleteNoise(currentBitmap, Global.noiseThreshold) : currentBitmap;
                    currentBitmap = Global.settingAdjustBorder ? OpenCV.adjustPaddingBorder(currentBitmap, Global.paddingSize) : currentBitmap;
                    currentBitmap = Global.settingDilation ? OpenCV.dilate(currentBitmap, Global.dilationFactor) : currentBitmap;
                    currentBitmap = Global.settingErosion ? OpenCV.erode(currentBitmap, Global.erosionFactor) : currentBitmap;
                    tmpBitmaps[i] = currentBitmap;
                }
                arrLabeledBitmap.add(new LabeledBitmapArray(tmpBitmaps, tmp.getLabel()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param){
            splittingVerticalAdapter.notifyDataSetChanged();
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