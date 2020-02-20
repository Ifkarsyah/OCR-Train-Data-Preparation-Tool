package com.ppl.photoapp.Bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class BitmapOperation
{
    public static Bitmap convertGrayscale(Bitmap original){
        Bitmap finalImage = Bitmap.createBitmap(original.getWidth(), original.getHeight(),original.getConfig()) ;

        int A , R , G , B ;
        int colorPixel ;
        int width = original.getWidth() ;
        int height = original.getHeight() ;

        for(int x = 0 ; x < width ; x ++){
            for(int y = 0 ; y < height ; y ++){
                colorPixel = original.getPixel(x,y) ;
                A = Color.alpha(colorPixel) ;
                R = Color.red(colorPixel) ;
                G = Color.green(colorPixel) ;
                B = Color.blue(colorPixel) ;

                R = (R + G + B)/3 ;
                G = R ;
                B = R ;

                finalImage.setPixel(x,y,Color.argb(A,R,G,B));
            }
        }

        return finalImage ;
    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal){
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmp);
        Paint paint = new Paint();

        ColorMatrix cm = new ColorMatrix(new ColorMatrix(new float[]{
                (float)1/3,(float)1/3,(float)1/3,0,0,
                (float)1/3,(float)1/3,(float)1/3,0,0,
                (float)1/3,(float)1/3,(float)1/3,0,0,
                0,0,0,1,0}
        ) ) ;

        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmp;
    }

    public static Bitmap toBlackWhite(Bitmap bmpOriginal){
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmp);
        Paint paint = new Paint();


        int percentage = 85 ; //default = 85
        ColorMatrix cm = new ColorMatrix(new ColorMatrix(new float[]{
                percentage,percentage,percentage, 0, -128*255,
                percentage,percentage,percentage, 0, -128*255,
                percentage,percentage,percentage, 0, -128*255,
                0, 0, 0, 1, 0 }
                ) ) ;

        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmp;
    }
}
