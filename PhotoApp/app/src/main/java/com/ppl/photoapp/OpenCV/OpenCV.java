package com.ppl.photoapp.OpenCV;

import android.graphics.Bitmap;

import com.ppl.photoapp.Model.LabeledBitmapArray;

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

    public static ArrayList<LabeledBitmapArray> mappingBitmap(ArrayList<Bitmap> arrBitmap){
        ArrayList<LabeledBitmapArray> arrLabeledBitmap = new ArrayList<>() ;

        //Dummy
        for(int i = 0 ; i < arrBitmap.size()/4 ; i ++ ){
            Bitmap[] bitmaps = new Bitmap[4] ;
            for(int j = 0 ; j < bitmaps.length ; j ++ ){
                bitmaps[j] = arrBitmap.get(i*4 + j) ;
            }
            arrLabeledBitmap.add(new LabeledBitmapArray(bitmaps,i) );
        }
        return arrLabeledBitmap ;
    }

}
