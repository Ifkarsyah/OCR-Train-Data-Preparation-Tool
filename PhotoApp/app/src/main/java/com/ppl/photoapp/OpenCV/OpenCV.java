package com.ppl.photoapp.OpenCV;

import android.graphics.Bitmap;

import com.ppl.photoapp.Model.LabeledBitmapArray;

import java.util.ArrayList;

public class OpenCV
{
    public static ArrayList<Bitmap> getArrayBitmap(Bitmap bitmapInput){
        // Input: bitmapInput == the whole image not splitted yet

        ArrayList<Bitmap> arrBitmap = new ArrayList<>() ;
        // TODO: change with working openCV implementation
        for(int i = 0 ; i < 10*10 ; i ++ ){
            // i = 0  .. 9  ==> get first row, will be labeled 0
            // i = 10 .. 19 ==> get second row, will be labeled 1
            // i = 20 .. 29 ==> get third row, will be labeled 2
            //
            // i = 90 .. 99 ==> get last row, will be labeled 9
            arrBitmap.add(bitmapInput) ; // TODO: comment this
        }

        // Output: arrBitmap == ArrayList<Bitmap> == [0, 0, 0, 0, ...., 1, 1, 1, 1, ...., 9, 9, 9, 9, 9]
        return arrBitmap ;
    }

    public static ArrayList<LabeledBitmapArray> mappingBitmap(ArrayList<Bitmap> arrBitmap){
        ArrayList<LabeledBitmapArray> arrLabeledBitmap = new ArrayList<>() ;

        //Dummy
        for(int i = 0 ; i < arrBitmap.size()/10 ; i ++ ){
            Bitmap[] bitmaps = new Bitmap[10] ;
            for(int j = 0 ; j < bitmaps.length ; j ++ ){
                bitmaps[j] = arrBitmap.get(i*10 + j) ;
            }
            arrLabeledBitmap.add(new LabeledBitmapArray(bitmaps,i) );
        }
        return arrLabeledBitmap ;
    }

}
