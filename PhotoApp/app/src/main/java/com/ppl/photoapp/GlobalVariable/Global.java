package com.ppl.photoapp.GlobalVariable;


import android.graphics.Bitmap;
import android.text.Editable;

import com.ppl.photoapp.Model.LabeledBitmapArray;

import java.util.ArrayList;

public class Global {
    public static Bitmap bitmap ;
    public static Bitmap splittedBitmap;
    public static final String INTENT_PATH_BITMAP = "INTENT_PATH_BITMAP";

    public static boolean settingDeleteNoise = false;
    public static boolean settingAdjustBorder = true;
    public static int settingCountRow = 14;
    public static int settingCountCol = 10;
    public static int settingColorMode = 2;

    public static int paddingSize = 10;

    public static ArrayList<LabeledBitmapArray> labeledBitmap ;

}
