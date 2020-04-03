package com.ppl.photoapp.OpenCV;

import android.graphics.Bitmap;

import com.ppl.photoapp.Model.LabeledBitmapArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.utils.Converters;

import static org.opencv.core.CvType.CV_32S;

public class OpenCV
{
    private static final int COUNT_IMAGES_IN_ONE_ROW = 14;

    public static ArrayList<Bitmap> getArrayBitmap(Bitmap bitmapInput){
        // Input: bitmapInput == the whole image not split yet

        // Load image
        Mat orig = new Mat();
        Bitmap bmp = bitmapInput.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp, orig);

        Mat gray = new Mat();
        Imgproc.cvtColor(orig, gray, Imgproc.COLOR_BGR2GRAY);
        Mat thresh = new Mat();
        Imgproc.adaptiveThreshold(gray, thresh, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 57, 5);

        // Find the largest rectangle (paper)
        LinkedList<MatOfPoint> cnts = new LinkedList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(thresh, cnts, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        Collections.sort(cnts, new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint o1, MatOfPoint o2) {
                double area1 = Imgproc.contourArea(o1);
                double area2 = Imgproc.contourArea(o2);
                int result = Double.compare(area2, area1);
                return result;
            }
        } );
        MatOfPoint2f screenCnt = new MatOfPoint2f();
        for (MatOfPoint c : cnts){
            MatOfPoint2f c_2f = new MatOfPoint2f(c.toArray());
            double peri = Imgproc.arcLength(c_2f, true);
            MatOfPoint2f approx = new MatOfPoint2f();
            Imgproc.approxPolyDP(c_2f, approx, 0.02 * peri, true);
            if (approx.total() == 4){
                screenCnt = approx;
                break;
            }
        }

        // Sort the bounding box indices to prepare for warpPerspective
        List<Point> pts = screenCnt.toList();
        Point tl = pts.get(0);
        Point br = pts.get(0);
        Point bl = pts.get(0);
        Point tr = pts.get(0);
        for (Point pt : pts){
            if (pt.x + pt.y > br.x + br.y) br = pt;
            if (pt.x + pt.y < tl.x + tl.y) tl = pt;
            if (pt.y - pt.x > bl.y - bl.x) bl = pt;
            if (pt.y - pt.x < tr.y - tr.x) tr = pt;
        };

        double heightA = Math.sqrt(Math.pow(tr.x - br.x, 2) + Math.pow(tr.y - br.y, 2));
        double heightB = Math.sqrt(Math.pow(tl.x - bl.x, 2) + Math.pow(tl.y - bl.y, 2));
        int maxHeight = Math.max((int) heightA , (int) heightB);
        int maxWidth = (int) (maxHeight / 1.414);

        // Perspective correction
        MatOfPoint2f src = new MatOfPoint2f(tl, tr, br, bl);
        MatOfPoint2f dst = new MatOfPoint2f(
            new Point(0, 0),
            new Point(maxWidth - 1, 0),
            new Point(maxWidth - 1, maxHeight - 1),
            new Point(0, maxHeight - 1)
        );

        Mat M = Imgproc.getPerspectiveTransform(src, dst);
        Mat warped = new Mat();
        Imgproc.warpPerspective(orig, warped, M, new Size(maxWidth, maxHeight));

        int width = Math.min(1000, warped.cols());
        int height = width * warped.rows() / warped.cols() ;
        Size sz = new Size(width, height);

        // Grayscale, adaptive threshold, and monochromatic invert from thresh
        Mat image = new Mat();
        Imgproc.resize(warped, image, sz , 0, 0, Imgproc.INTER_AREA);

        gray = new Mat();
        Imgproc.cvtColor(warped, gray, Imgproc.COLOR_BGR2GRAY);
        thresh = new Mat();
        Imgproc.adaptiveThreshold(gray, thresh, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 57, 5);
        Mat mono = new Mat();
        Core.bitwise_not(thresh, mono);

        // Filter out all numbers and noise to isolate only boxes
        cnts = new LinkedList<>();
        hierarchy = new Mat();
        Imgproc.findContours(thresh, cnts, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        hierarchy.release();
        for (MatOfPoint c : cnts){
            double area = Imgproc.contourArea(c);
            if (area < 1000){
                List<MatOfPoint> drawCnt = new ArrayList<>();
                drawCnt.add(c);
                Imgproc.drawContours(thresh, drawCnt, -1, new Scalar(0, 0, 0), -1);
            }
        }

        // Fix horizontal and vertical lines
        Mat vertical_kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, 3));
        Imgproc.morphologyEx(thresh, thresh, Imgproc.MORPH_CLOSE, vertical_kernel, new Point(-1, -1), 9);
        Mat horizontal_kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 1));
        Imgproc.morphologyEx(thresh, thresh, Imgproc.MORPH_CLOSE, horizontal_kernel, new Point(-1, -1), 4);

        Mat invert = new Mat();
        Core.bitwise_not(thresh, invert);
        LinkedList<MatOfPoint> tmp = new LinkedList<>();
        Imgproc.findContours(invert, tmp, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        hierarchy.release();
        cnts = tmp;

        // Remove non-square contours, approximated by scale and approximated n_point
        tmp = new LinkedList<>();
        for (MatOfPoint cnt : cnts){
            Rect bb = Imgproc.boundingRect(cnt);
            float ratio = ((float) bb.width)/bb.height;

            MatOfPoint2f c_2f = new MatOfPoint2f(cnt.toArray());
            double peri = Imgproc.arcLength(c_2f, true);
            MatOfPoint2f approx = new MatOfPoint2f();
            Imgproc.approxPolyDP(c_2f, approx, 0.02 * peri, true);
            if (approx.total() <= 6 && ratio > 0.8 && ratio < 1.2)
                tmp.add(cnt);
        }
        // Sort by left to right
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
        int sz = arrBitmap.size();
        for(int i = 0 ; i < (sz + COUNT_IMAGES_IN_ONE_ROW - 1)/COUNT_IMAGES_IN_ONE_ROW ; i ++ ){
            Bitmap[] bitmaps = new Bitmap[COUNT_IMAGES_IN_ONE_ROW] ;
            for(int j = 0 ; j < bitmaps.length ; j ++ ){
                int offsetRow = i*COUNT_IMAGES_IN_ONE_ROW;
                if (offsetRow + j < sz)
                    bitmaps[j] = arrBitmap.get(offsetRow + j) ;
            }
            arrLabeledBitmap.add(new LabeledBitmapArray(bitmaps,i) );
        }
        return arrLabeledBitmap ;
    }

    public static Bitmap setColorModeBitmap(Bitmap wholeBitmap, int colorMode) {
        // TODO asif
        switch (colorMode) {
            case 0: break; // biarin
            case 1: break; // grayscale
            case 2: break; // blackwhite
        }
        return wholeBitmap;
    }

    public static Bitmap deleteNoise(Bitmap splittedBitmap) {
        // TODO alam
        return splittedBitmap;
    }

    public static Bitmap adjustPaddingBorder(Bitmap splittedBitmap) {
        // TODO suhailie
        return splittedBitmap;
    }
}
