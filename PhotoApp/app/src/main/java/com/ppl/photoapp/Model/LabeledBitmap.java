package com.ppl.photoapp.Model;

import android.graphics.Bitmap;

public class LabeledBitmap
{
    private Bitmap bitmap ;
    private int label ;

    public LabeledBitmap(Bitmap bitmap, int label) {
        this.bitmap = bitmap;
        this.label = label;
    }
}
