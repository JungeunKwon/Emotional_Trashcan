package com.example.jungeunkwon.myapplication.Widget;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.jungeunkwon.myapplication.Diary.DiaryEntity;
import com.example.jungeunkwon.myapplication.Diary.DiaryPresenter;
import com.example.jungeunkwon.myapplication.R;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WidgetCameraActivity extends Activity {
    EditText input;
    TextView txt;
    DiaryPresenter mPresenter;
    String imagePath = "";
    final int REQUEST_TAKE_PHOTO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_widget_camera);
        mPresenter = new DiaryPresenter(this,null);
        imagePath = "";

        checkVerify();

    }
    @TargetApi(Build.VERSION_CODES.M)
    public void checkVerify()
    {
        if (
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||
                        checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
                )
        {
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                // ...
            }
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    1);


            finish();
        }
        else
        {
            startcamera();
        }
    }
    public void startcamera()
    {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        try {
            if (isExistCameraApplication()) {
                Intent cameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File picture = savePictureFile();
                if (picture != null) {
                    cameraApp.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
                    startActivityForResult(cameraApp, REQUEST_TAKE_PHOTO);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(WidgetCameraActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        BitmapFactory.Options factory = new BitmapFactory.Options();
                        factory.inSampleSize = 4;
                        factory.inJustDecodeBounds = false;
                        factory.inPurgeable = true;
                        Date now;
                        String date;
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        now = new Date();
                        date = dateFormat.format(now);
                        DiaryEntity diaryEntity = new DiaryEntity();
                        diaryEntity.setDate(date);
                        diaryEntity.setContent(imagePath);
                        mPresenter.insert(diaryEntity);
                        imagePath = "";
                        Toast.makeText(WidgetCameraActivity.this,getString(R.string.multi_alert_insert_good), Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        Toast.makeText(WidgetCameraActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;


        }
        finish();
    }
    private boolean isExistCameraApplication() {
        PackageManager packageManager = getPackageManager();

        Intent cameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        List camerApps = packageManager.queryIntentActivities(
                cameraApp, PackageManager.MATCH_DEFAULT_ONLY);
        return camerApps.size() > 0;

    }

    private File savePictureFile() {
       /* PermissionRequester.Builder requester = new PermissionRequester.Builder(this);
        int result = requester
                .create()
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, 20000, new PermissionRequester.OnClickDenyButtonListener() {
                            @Override
                            public void onClick(Activity activity) {

                            }
                        });

        if (result == PermissionRequester.ALREADY_GRANTED || result == PermissionRequester.REQUEST_PERMISSION) {
        */
        if (true) //api 버전 체크 23이상이면 위의 퍼미션 해줘야 하는데에ㅔㅔ
        {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "IMG_" + timestamp;

            File pictureStorage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MYAPP/");

            if (!pictureStorage.exists()) {
                pictureStorage.mkdirs();
            }

            try {
                imagePath="";
                File file = File.createTempFile(fileName, ".jpg", pictureStorage);
                imagePath = file.getAbsolutePath();

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(imagePath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                return file;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
        }
        return null;
    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
    @Override
    public void onStop()
    {
        super.onStop();
    }
}
