package com.ppl.photoapp.OpenCV;

import android.graphics.Bitmap;

import com.ppl.photoapp.Model.LabeledBitmap;

import java.util.ArrayList;

public class OpenCV
{
    public static ArrayList<Bitmap> getArrayBitmap(Bitmap bitmapInput){
        ArrayList<Bitmap> arrBitmap = new ArrayList<>() ;

        //Dummy
        for(int i = 0 ; i < 4*10 ; i ++ ){
             arrBitmap.add(bitmapInput) ;
        }

        return arrBitmap ;
    }

    public static ArrayList<LabeledBitmap> mappingBitmap(ArrayList<Bitmap> arrBitmap){
        ArrayList<LabeledBitmap> arrLabeledBitmap = new ArrayList<>() ;

        //Dummy
        int acc = 0 ;
        int label = 0 ;
        for(int i = 0 ; i < arrBitmap.size() ; i ++ ){
            arrLabeledBitmap.add(new LabeledBitmap(arrBitmap.get(i),label) );
            acc ++ ;
            if (acc % 4 == 0){
                label ++ ;
            }
        }
        return arrLabeledBitmap ;
    }

}
