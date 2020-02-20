package com.ppl.photoapp.Model;

import android.graphics.Bitmap;

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
}
