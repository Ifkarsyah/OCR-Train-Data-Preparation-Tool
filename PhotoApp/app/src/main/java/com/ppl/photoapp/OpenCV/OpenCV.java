package com.ppl.photoapp.OpenCV;

import android.graphics.Bitmap;
import android.util.Log;

import com.ppl.photoapp.GlobalVariable.Global;
import com.ppl.photoapp.Model.LabeledBitmapArray;

import java.io.Console;
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
import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.imgproc.Imgproc.CC_STAT_LEFT;
import static org.opencv.imgproc.Imgproc.CC_STAT_TOP;
import static org.opencv.imgproc.Imgproc.CC_STAT_WIDTH;
import static org.opencv.imgproc.Imgproc.CC_STAT_HEIGHT;
import static org.opencv.imgproc.Imgproc.CC_STAT_AREA;


public class OpenCV
{
    private static final int COUNT_IMAGES_IN_ONE_ROW = Global.settingCountRow;

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

        // Remove non-square contours, approximated by scale
        tmp = new LinkedList<>();
        for (MatOfPoint cnt : cnts){
            Rect bb = Imgproc.boundingRect(cnt);
            float ratio = ((float) bb.width)/bb.height;
            if (ratio > 0.8 && ratio < 1.2)
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
            Mat outMap = new Mat(image, rect);
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

    public static Bitmap setColorModeBitmap(Bitmap wholeBitmap, int colorMode) {
        Mat orig = new Mat();
        Bitmap bmp = wholeBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp, orig);

        Bitmap returnBitmap = wholeBitmap;
        Mat gray = new Mat();
        Imgproc.cvtColor(orig, gray, Imgproc.COLOR_BGR2GRAY);
        switch (colorMode) {
            case 0: break; // biarin
            case 1: // grayscale
                returnBitmap = Bitmap.createBitmap(gray.cols(), gray.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(gray, returnBitmap);
                break;
            case 2: // blackwhite
                Mat thresh = new Mat();
                Imgproc.adaptiveThreshold(gray, thresh, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 57, 5);
                Mat mono = new Mat();
                Core.bitwise_not(thresh, mono);
                returnBitmap = Bitmap.createBitmap(mono.cols(), mono.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(mono, returnBitmap);
                break;
        }
        return returnBitmap;
    }

    public static Bitmap deleteNoise(Bitmap splittedBitmap) {
        // TODO alam

        int imw = splittedBitmap.getWidth();
        int imh = splittedBitmap.getHeight();
        int area_threshold = imw*imh*Global.noiseThreshold/100;
        Mat process_image = new Mat();
        Bitmap bmp = splittedBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp, process_image);

        //convert to grayscale
        Mat gray_image = new Mat();
        Imgproc.cvtColor(process_image,gray_image,Imgproc.COLOR_BGR2GRAY);


        Mat inverted_image = new Mat(imh,imw,CV_8UC1);

        Core.bitwise_not(gray_image,inverted_image);


        // Get stats
        Mat stats_mat = new Mat();
        int n_label = Imgproc.connectedComponentsWithStats(inverted_image, new Mat(), stats_mat, new Mat());

        for (int image = 1; image < n_label; image++) {

            int comp_x, comp_y, comp_width, comp_height;
            // Check area, below certain threshold
            if (stats_mat.get(image, CC_STAT_AREA)[0] < area_threshold) {
                comp_x = (int) stats_mat.get(image, CC_STAT_LEFT)[0];
                comp_y = (int) stats_mat.get(image, CC_STAT_TOP)[0];
                comp_width = (int) stats_mat.get(image, CC_STAT_WIDTH)[0];
                comp_height = (int) stats_mat.get(image, CC_STAT_HEIGHT)[0];
                for (int y = (int) comp_y; y < comp_y + comp_height; y++) {
                    for (int x = (int) comp_x; x < comp_x + comp_width; x++) {
                        inverted_image.put(y, x,0);
                    }
                }
            }
        }

        // Invert back
        Core.bitwise_not(inverted_image,gray_image);
        Utils.matToBitmap(gray_image, bmp);

        return bmp;


    //return splittedBitmap;
    }

    public static Bitmap adjustPaddingBorder(Bitmap splittedBitmap) {
        // TODO suhailie
        return splittedBitmap;
    }
}
