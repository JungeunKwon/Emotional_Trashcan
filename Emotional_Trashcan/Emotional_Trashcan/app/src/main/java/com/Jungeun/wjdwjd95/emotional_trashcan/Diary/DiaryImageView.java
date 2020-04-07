package com.Jungeun.wjdwjd95.emotional_trashcan.Diary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.Jungeun.wjdwjd95.emotional_trashcan.R;

import static com.Jungeun.wjdwjd95.emotional_trashcan.Imageset.exifOrientationToDegrees;
import static com.Jungeun.wjdwjd95.emotional_trashcan.Imageset.rotate;

public class DiaryImageView extends Activity {
    ImageView img;
    String imagePath;
    DiaryPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //출처: http://ghj1001020.tistory.com/9 [혁준 블로그]
        setContentView(R.layout.activity_diary_image_view);
        img = (ImageView)findViewById(R.id.diary_image_activity_image);
        Intent intent = getIntent();
        imagePath = intent.getStringExtra("imagePath");
       try {
           BitmapFactory.Options factory = new BitmapFactory.Options();
           factory.inSampleSize = 4;
           factory.inJustDecodeBounds = false;
           factory.inPurgeable = true;
           Bitmap bitmap = BitmapFactory.decodeFile(imagePath, factory);
           ExifInterface exif = new ExifInterface(imagePath);
           int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
           int exifDegree = exifOrientationToDegrees(exifOrientation);
           bitmap = rotate(bitmap, exifDegree);

           img.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 480,800, true));
       }catch (Exception ex)
       {
           Toast.makeText(DiaryImageView.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
       }

    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
}
