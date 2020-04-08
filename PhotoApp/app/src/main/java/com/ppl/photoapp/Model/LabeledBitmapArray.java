package com.ppl.photoapp.Model;

import android.graphics.Bitmap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

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
        ArrayList<Bitmap> arrBitmap = new ArrayList<>(Arrays.asList(bitmap)) ;
        arrBitmap.remove(position);
        bitmap = arrBitmap.toArray(new Bitmap[arrBitmap.size()]);
    }
}
