package com.example.jungeunkwon.myapplication;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class Imageset {
    public static int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)

        {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)

        {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)

        {
            return 270;
        }
        return 0;
    }


    //출처: http://bitnori.tistory.com/entry/안드로이드-로컬에서-사진가져올때-회전현상-해결 [Bitnori's Blog]
    public static Bitmap rotate(Bitmap bitmap, int degrees)
    {
        if(degrees != 0 && bitmap != null)
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try
            {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if(bitmap != converted)
                {
                    bitmap.recycle();
                    bitmap = converted;
                }
            }
            catch(OutOfMemoryError ex)
            {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }
}
