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
}
