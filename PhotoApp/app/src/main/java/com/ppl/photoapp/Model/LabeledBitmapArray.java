package com.ppl.photoapp.Model;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class LabeledBitmapArray
{
    private Bitmap[] bitmap ;
    private int label ;

    public LabeledBitmapArray(Bitmap[] bitmap, int label) {
        this.bitmap = bitmap;
        this.label = label;
    }

    public LabeledBitmapArray() {
    }

    public Bitmap[] getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap[] bitmap) {
        this.bitmap = bitmap;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public void deleteItem(int position){
        ArrayList<Bitmap> arrBitmap = new ArrayList<>() ;
        for(int i = 0 ; i < bitmap.length ; i ++){
            arrBitmap.add(bitmap[i]) ;
        }

        arrBitmap.remove(position) ;

        bitmap = new Bitmap[arrBitmap.size()] ;
        for(int i = 0 ; i < arrBitmap.size() ; i ++){
            bitmap[i] = arrBitmap.get(i) ;
        }
    }
}
