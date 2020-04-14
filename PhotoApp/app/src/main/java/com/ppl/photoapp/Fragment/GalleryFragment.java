package com.ppl.photoapp.Fragment;


import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ppl.photoapp.Adapter.GalleryAdapter;
import com.ppl.photoapp.Adapter.NumberAdapter;
import com.ppl.photoapp.Config.Config;
import com.ppl.photoapp.Config.FormatNameFile;
import com.ppl.photoapp.R;

import java.io.File;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    public GalleryFragment() {

    }

    //Filter Number
    RecyclerView recyclerViewNumber ;
    NumberAdapter numberAdapter ;

    //Gallery
    RecyclerView recyclerViewGallery ;
    GalleryAdapter galleryAdapter ;
    ArrayList<String> arrPath ;

    LinearLayout lnBeforePermission ;
    LinearLayout lnAfterPermission ;
    Button btnPermissionRequest ;
    static final int PERMISSION_CODE = 1 ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        AssignWidget(view);
        CheckPermissions() ;
        ButtonPermissionRequest() ;
        SetFilterNumberView() ;
        SetGallery() ;


        return view ;
    }

    void ButtonPermissionRequest(){
        btnPermissionRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestPermission();
            }
        });
    }

    void AssignWidget(View view){
        lnBeforePermission = view.findViewById(R.id.lnBeforePermission) ;
        lnAfterPermission = view.findViewById(R.id.lnAfterPermission) ;
        recyclerViewGallery = view.findViewById(R.id.recyclerViewGallery) ;
        recyclerViewNumber = view.findViewById(R.id.recyclerViewNumber) ;
        btnPermissionRequest = view.findViewById(R.id.btnPermissionRequest) ;
    }

    void CheckPermissions(){
        if ((getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) || (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED))
        {
            lnBeforePermission.setVisibility(View.VISIBLE) ;
            lnAfterPermission.setVisibility(View.GONE);
        }
        else {
            lnBeforePermission.setVisibility(View.GONE);
            lnAfterPermission.setVisibility(View.VISIBLE);
        }
    }

    void RequestPermission(){
        if ((getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) || (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED))
        {
            String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE} ;
            requestPermissions(permission,PERMISSION_CODE) ;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    lnBeforePermission.setVisibility(View.GONE);
                    lnAfterPermission.setVisibility(View.VISIBLE);
                    SetGallery() ;
                }else {
                    Toast.makeText(getActivity(),"Permission Denied",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void SetCheckedNumber(){
        SharedPreferences pref = getContext().getSharedPreferences(Config.PREF_GALLERY, 0) ;
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(Config.KEY_GALLERY_NUMBER,numberAdapter.checkedNumber) ;
        editor.commit() ;
    }

    void SetFilterNumberView(){
        recyclerViewNumber.setHasFixedSize(true);
        recyclerViewNumber.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        SharedPreferences pref = getContext().getSharedPreferences(Config.PREF_GALLERY, 0) ;
        int checkedNumber = pref.getInt(Config.KEY_GALLERY_NUMBER, -1);
        numberAdapter = new NumberAdapter(getContext(),Config.FITLER_NUMBER,this,checkedNumber) ;
        numberAdapter.notifyDataSetChanged();
        recyclerViewNumber.setAdapter(numberAdapter);
    }

    void SetGallery(){
        recyclerViewGallery.setHasFixedSize(true);
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(getActivity(),3));
        if (numberAdapter.checkedNumber != -1) {
            new getImagesPathByNumber_Async(numberAdapter.checkedNumber).execute(getFolderPath()) ;
        }else {
            new getImagesPath_Async().execute(getFolderPath()) ;
        }
    }

    public void NumberChanged(){
        if (numberAdapter.checkedNumber != -1) {
            new getImagesPathByNumber_Async(numberAdapter.checkedNumber).execute(getFolderPath()) ;
        }else {
            new getImagesPath_Async().execute(getFolderPath()) ;
        }
    }

    public void UpdateRecylerViewNumber(){
        numberAdapter.notifyDataSetChanged();
    }

    private class getImagesPath_Async extends AsyncTask<File, Integer, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(File... files) {
            return getImagesPath(files[0]) ;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            arrPath = strings ;
            SetArrPathToRecyclerView() ;
        }
    }

    private class getImagesPathByNumber_Async extends AsyncTask<File, Integer, ArrayList<String>> {
        int number ;

        public getImagesPathByNumber_Async(int number) {
            super();
            this.number = number ;
        }

        @Override
        protected ArrayList<String> doInBackground(File... files) {
            ArrayList<String> temp = getImagesPath(files[0]) ;
            return getImagesPathByNumber(number,temp) ;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            arrPath = strings ;
            SetArrPathToRecyclerView() ;
        }
    }

    public ArrayList<String> getImagesPathByNumber(int number,ArrayList<String> arrAllPath){
        ArrayList<String> outputArrayList = new ArrayList<>() ;
        for (int i = 0 ; i < arrAllPath.size() ; i ++){
            String str = arrAllPath.get(i) ;
            if (str.contains(FormatNameFile.SubFolder(number))){
                outputArrayList.add(str) ;
            }
        }
        return outputArrayList ;
    }

    public ArrayList<String> getImagesPath(File dir) {
        ArrayList<String> fileList = new ArrayList<>() ;
        File listFile[] = dir.listFiles();
        ArrayList<File> fileArrayList = mapDirFiles(listFile) ;

        if (fileArrayList != null && fileArrayList.size() > 0) {
            for (File file : fileArrayList) {
                try {
                    String path = file.toString() ;
                    String extentsionFile = path.substring(path.lastIndexOf("."));
                    if (FormatNameFile.isAvaiableExtension(extentsionFile))
                    {
                        fileList.add(path);
                    }
                }catch (Exception e){

                }

            }
        }
        return fileList;
    }

    ArrayList<File> mapDirFiles(File listFile[]){
        ArrayList<File> fileArrayList = new ArrayList<>() ;
        if (listFile != null){
            for(int i = 0 ; i < listFile.length ; i ++){
                try {
                    File listSubFile[] = listFile[i].listFiles() ;
                    for(int  j = 0 ; j < listSubFile.length ; j ++ ){
                        fileArrayList.add(listSubFile[j]) ;
                    }
                }catch (Exception e){

                }

            }
        }
        return fileArrayList ;
    }

    File getFolderPath(){
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File folder = new File(FormatNameFile.RootFolder(root));
        folder.mkdirs();
        return folder ;
    }

    void SetArrPathToRecyclerView(){
        galleryAdapter = new GalleryAdapter(getContext(),arrPath,this) ;
        galleryAdapter.notifyDataSetChanged();
        recyclerViewGallery.setAdapter(galleryAdapter);
    }
}
