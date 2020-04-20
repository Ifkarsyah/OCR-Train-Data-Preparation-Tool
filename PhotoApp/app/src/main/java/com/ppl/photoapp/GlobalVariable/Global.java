package com.ppl.photoapp.GlobalVariable;


import android.graphics.Bitmap;
import android.text.Editable;

import com.ppl.photoapp.Model.LabeledBitmapArray;

import java.util.ArrayList;

public class Global {
    public static Bitmap bitmap ;
    public static Bitmap splittedBitmap;
    public static final String INTENT_PATH_BITMAP = "INTENT_PATH_BITMAP";

    public static boolean isSettingsChanged = false;
    public static boolean settingDeleteNoise = false;
    public static boolean settingAdjustBorder = true;
    public static int settingCountRow = 14;
    public static int settingCountCol = 10;
    public static int settingColorMode = 0;

    // Percentage of connected component area considered as noisy
    // Example, 5 means that any connectedComponent cover less than 5 % of bitmap area
    // is considered as a noise and will be obliterated
    public static int noiseThreshold = 5;

    // Erosion factor
    // Make image thinner
    public static boolean settingErosion = false;
    public static int erosionFactor = 4;

    // Dilate factor
    // Make image bolder
    public static  boolean settingDilation = false;
    public static int dilationFactor = 4;



    public static int paddingSize = 10;

    public static ArrayList<LabeledBitmapArray> labeledBitmap ;


}
