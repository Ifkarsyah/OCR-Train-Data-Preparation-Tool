package com.ppl.photoapp.OpenCV;

import android.graphics.Bitmap;

import com.ppl.photoapp.Model.LabeledBitmapArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class OpenCV
{
    private static final int COUNT_IMAGES_IN_ONE_ROW = 14;

    public static ArrayList<Bitmap> getArrayBitmap(Bitmap bitmapInput){
        // Input: bitmapInput == the whole image not split yet

        // Load image and resize into maximum of 1000px width
        Mat orig = new Mat();
        Bitmap bmp = bitmapInput.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp, orig);

        int width = Math.min(1000, orig.cols());
        int height = width * orig.rows() / orig.cols() ;
        Size sz = new Size(width, height);

        // Grayscale, adaptive threshold, and monochromatic invert from thresh
        Mat image = new Mat();
        Imgproc.resize(orig, image, sz , 0, 0, Imgproc.INTER_AREA);
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        Mat thresh = new Mat();
        Imgproc.adaptiveThreshold(gray, thresh, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 57, 5);
        Mat mono = new Mat();
        Core.bitwise_not(thresh, mono);

        // Filter out all numbers and noise to isolate only boxes
        LinkedList<MatOfPoint> cnts = new LinkedList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(thresh, cnts, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        hierarchy.release();
        for (MatOfPoint c : cnts){
            double area = Imgproc.contourArea(c);
            if (area < 100000){
                List<MatOfPoint> drawCnt = new ArrayList<>();
                drawCnt.add(c);
                Imgproc.drawContours(thresh, drawCnt, -1, new Scalar(0, 0, 0), -1);
            }
        }

        // Fix horizontal and vertical lines
        Mat vertical_kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, 5));
        Imgproc.morphologyEx(thresh, thresh, Imgproc.MORPH_CLOSE, vertical_kernel, new Point(-1, -1), 9);
        Mat horizontal_kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 1));
        Imgproc.morphologyEx(thresh, thresh, Imgproc.MORPH_CLOSE, horizontal_kernel, new Point(-1, -1), 4);

        // Sort by left to right
        Mat invert = new Mat();
        Core.bitwise_not(thresh, invert);
        LinkedList<MatOfPoint> tmp = new LinkedList<>();
        Imgproc.findContours(invert, tmp, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        hierarchy.release();
        cnts = tmp;

        // Remove non-square contours, approximated by scale
        tmp = new LinkedList<>();
        for (MatOfPoint cnt : cnts){
            Rect bb = Imgproc.boundingRect(cnt);
            float ratio = ((float) bb.width)/bb.height;
            if (ratio > 0.8 && ratio < 1.2)
                tmp.add(cnt);
        }
        cnts = tmp;
        Collections.sort(cnts, new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint o1, MatOfPoint o2) {
                Rect rect1 = Imgproc.boundingRect(o1);
                Rect rect2 = Imgproc.boundingRect(o2);
                int result = Double.compare(rect1.tl().x, rect2.tl().x);
                return result;
            }
        } );

        // Iterate through each box, output to Bitmap
        ArrayList<Bitmap> arrBitmap = new ArrayList<>();
        while (!cnts.isEmpty()){
            MatOfPoint cnt = cnts.removeFirst();
            Rect rect = Imgproc.boundingRect(cnt);
            Mat outMap = new Mat(mono, rect);
            Bitmap outBmp = Bitmap.createBitmap(outMap.cols(), outMap.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(outMap, outBmp);
            arrBitmap.add(outBmp);
        }

        // Output: arrBitmap == ArrayList<Bitmap> == [0, 0, 0, 0, ...., 1, 1, 1, 1, ...., 9, 9, 9, 9, 9]
        return arrBitmap;
    }

    public static ArrayList<LabeledBitmapArray> mappingBitmap(ArrayList<Bitmap> arrBitmap){
        ArrayList<LabeledBitmapArray> arrLabeledBitmap = new ArrayList<>() ;

        for(int i = 0 ; i < arrBitmap.size()/COUNT_IMAGES_IN_ONE_ROW ; i ++ ){
            Bitmap[] bitmaps = new Bitmap[COUNT_IMAGES_IN_ONE_ROW] ;
            for(int j = 0 ; j < bitmaps.length ; j ++ ){
                int offsetRow = i*COUNT_IMAGES_IN_ONE_ROW;
                bitmaps[j] = arrBitmap.get(offsetRow + j) ;
            }
            arrLabeledBitmap.add(new LabeledBitmapArray(bitmaps,i) );
        }
        return arrLabeledBitmap ;
    }

}
