package com.Jungeun.wjdwjd95.emotional_trashcan.Diary;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;


import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.Jungeun.wjdwjd95.emotional_trashcan.Constants;
import com.Jungeun.wjdwjd95.emotional_trashcan.OnSwipeTouchListener;

import com.Jungeun.wjdwjd95.emotional_trashcan.PermissionRequester;
import com.Jungeun.wjdwjd95.emotional_trashcan.R;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DiaryActivity extends Activity implements DiaryView {
    Toolbar Toptoolbar;
    Toolbar Bottomtoolbar;
    LinearLayout dirlayout = null;
    final int DIALOG_DATE = 1;
    Date now;
    String insertdate;
    String date;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    TextView lbldate;
    //ImageButton btncalendar;
    private DiaryPresenter mPresenter;
    LinearLayout.LayoutParams lp = null;
    String imagePath;
    TextView innerDate;
    ScrollView scrollView;
    Uri photoURI;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        mPresenter = new DiaryPresenter(DiaryActivity.this, this);
        lbldate = (TextView) findViewById(R.id.diary_activity_lbldate);
        dirlayout = (LinearLayout) findViewById(R.id.diary_activity_dirlayout);


        imagePath = "";
        lp = new LinearLayout.LayoutParams(200, 200);
        now = new Date();
        insertdate = Constants.dateFormat.format(now);
        date = dateFormat.format(now);
        lbldate.setText("<  " +date + "  >");
        Intent intent = getIntent();
        if (intent.getStringExtra("Date") != null) {
            date = intent.getStringExtra("Date");
            date = date.substring(0,7);
            lbldate.setText("<  " +date + "  >");
        }
       /* btncalendar = (ImageButton) findViewById(R.id.diary_activity_btncalendar);
        btncalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });*/

        prefs = DiaryActivity.this.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        Boolean IsFirstOpen = prefs.getBoolean(Constants.SHARED_PREF_KEY_DIARY_ACTIVITY_FIRST, true);
        if (IsFirstOpen) {
            final Dialog dialog = new Dialog(DiaryActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            LayoutInflater inflater = DiaryActivity.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.intro_diary_template, null);

            ImageButton exit = (ImageButton) dialogView.findViewById(R.id.intro_diary_btngotit);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(Constants.SHARED_PREF_KEY_DIARY_ACTIVITY_FIRST, false);
                    editor.apply();
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.setContentView(dialogView);
            dialog.show();


            /*
            final AlertDialog.Builder inneralert = new AlertDialog.Builder(TrashActivity.this);

            dialogView.setBackgroundColor(Color.TRANSPARENT);
            inneralert.setView(dialogView);
            inneralert.setCancelable(false);
            final AlertDialog alertDialog = inneralert.create();
            alertDialog.show();
            */
            //설명창 시작
        }
        Toptoolbar = (Toolbar) findViewById(R.id.diary_activity_top);
        Toptoolbar.setTitle(getString(R.string.multi_diary));
        Toptoolbar.setNavigationIcon(R.drawable.goback);
        Toptoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });
        Bottomtoolbar = (Toolbar) findViewById(R.id.diary_activity_bottom);
        Bottomtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //  Log.WriteLine("bottom clicked " + item.getItemId() + item.getTitle());
                switch (item.getItemId()) {
                    case R.id.bottom_menu_write_camera_write:
                        BtnWrite_Click();
                        break;
                    case R.id.bottom_menu_write_camera_camera:
                        imagePath = "";
                        checkVerify();

                        break;
                }
                return true;
            }
        });
        scrollView = (ScrollView) findViewById(R.id.diary_activity_scrollview);

        scrollView.setOnTouchListener(new OnSwipeTouchListener(DiaryActivity.this) {

            @Override
            public void onSwipeRight() {
                final ProgressDialog progressDialog = ProgressDialog.show(DiaryActivity.this, "Loading data...", "Loading...", true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            now = dateFormat.parse(date);
                            now.setMonth(now.getMonth() - 1);
                            date = dateFormat.format(now);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lbldate.setText("<  " + date + "  >");
                                    ClearListView();
                                    mPresenter.getList(date);

                                }
                            });

                        } catch (Exception ex) {
                            Toast.makeText(DiaryActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.hide();
                            }
                        });
                    }
                }).start();


            }

            public void onSwipeLeft() {   final ProgressDialog progressDialog = ProgressDialog.show(DiaryActivity.this, "Loading data...", "Loading...", true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            now = dateFormat.parse(date);
                            now.setMonth(now.getMonth() + 1);
                            date = dateFormat.format(now);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lbldate.setText("<  " + date + "  >");
                                    ClearListView();
                                    mPresenter.getList(date);

                                }
                            });

                        } catch (Exception ex) {
                            Toast.makeText(DiaryActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.hide();
                            }
                        });
                    }
                }).start();
            }
        });
        Window window = this.getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Bottomtoolbar.inflateMenu(R.menu.bottom_menu_write_camera);
        ClearListView();

        mPresenter.getList(date);
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        now = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        switch (id) {

            case DIALOG_DATE:
                DatePickerDialog dpd = new DatePickerDialog(DiaryActivity.this, DatePickerDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        setDate(innerDate, year, monthOfYear, dayOfMonth);

                    }
                }, year, month, day);

                return dpd;
        }
        return super.onCreateDialog(id);
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

        }
        else
        {
            startcamera();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1)
        {
            if (grantResults.length > 0)
            {
                for (int i=0; i<grantResults.length; ++i)
                {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                    {
                        // 하나라도 거부한다면.
                        new AlertDialog.Builder(this).setTitle("알림").setMessage("권한을 허용해주셔야 앱을 이용할 수 있습니다.")
                                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).setNegativeButton("권한 설정", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                getApplicationContext().startActivity(intent);
                            }
                        }).setCancelable(false).show();

                        return;
                    }
                }
                startcamera();
            }
        }
    }
    public void startcamera()
    {

        final AlertDialog.Builder inneralert = new AlertDialog.Builder(DiaryActivity.this);
        LayoutInflater inflater = DiaryActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_camera,null);
        inneralert.setView(dialogView);
        inneralert.setCancelable(false);
        final AlertDialog alertDialog = inneralert.create();
        alertDialog.show();
        innerDate = (TextView)dialogView.findViewById(R.id.alert_dialog_camera_txtDate);
        now = new Date();
        insertdate = Constants.dateFormat.format(now);
        innerDate.setText(insertdate);
        ImageView btncalendar = (ImageView)dialogView.findViewById(R.id.alert_dialog_camera_btnDate);
        btncalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });
        Button Camera = (Button)dialogView.findViewById(R.id.alert_dialog_camera_btncamera);
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isExistCameraApplication()) {
                        Intent cameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        File picture = savePictureFile();
                        if (picture != null) {
                            photoURI = FileProvider.getUriForFile(DiaryActivity.this,
                                    "com.Jungeun.wjdwjd95.emotional_trashcan.fileprovider",
                                    picture);
                            cameraApp.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(cameraApp, REQUEST_TAKE_PHOTO);
                            alertDialog.dismiss();

                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(DiaryActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button Gallery = (Button)dialogView.findViewById(R.id.alert_dialog_camera_btngallery);
        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlbum();
                alertDialog.dismiss();

            }
        });
        Button exit = (Button)dialogView.findViewById(R.id.alert_dialog_camera_btnexit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
    public void setDate(TextView lbl, int year, int month, int day) {
        String tmpmonth = "", tmpday = "";
        if ((month + 1) < 10) {
            tmpmonth = "0" + (month + 1);
        } else
            tmpmonth = Integer.toString(month + 1);
        if (day < 10) {
            tmpday = "0" + day;
        } else {
            tmpday = Integer.toString(day);
        }
        now.setYear(year);
        now.setMonth(month);
        now.setDate(day);
        insertdate = year + "-" + tmpmonth + "-" + tmpday;
        lbl.setText(insertdate);

    }

    public void BtnWrite_Click() {
        final AlertDialog.Builder inneralert = new AlertDialog.Builder(DiaryActivity.this);
        LayoutInflater inflater = DiaryActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_write,null);
        inneralert.setView(dialogView);
        inneralert.setCancelable(false);
        final AlertDialog alertDialog = inneralert.create();
        alertDialog.show();
        innerDate = (TextView)dialogView.findViewById(R.id.alert_dialog_write_txtDate);
        now = new Date();
        insertdate = Constants.dateFormat.format(now);
        innerDate.setText(insertdate);
        ImageView btncalendar = (ImageView)dialogView.findViewById(R.id.alert_dialog_write_btnDate);
        btncalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });
        final EditText diary = (EditText)dialogView.findViewById(R.id.alert_dialog_write_edittxt);
        ImageButton Save = (ImageButton)dialogView.findViewById(R.id.alert_dialog_write_btnsave);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = diary.getText().toString();
               /* if(content.length() > 200)
                {
                    Toast.makeText(DiaryActivity.this, "It should be less than 200 charter", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                if(content.equals("")||content.isEmpty())
                {
                    Toast.makeText(DiaryActivity.this, getString(R.string.multi_alert_inputmsg), Toast.LENGTH_SHORT).show();
                    return;
                }

                DiaryEntity diaryEntity = new DiaryEntity();
                diaryEntity.setDate(innerDate.getText().toString());
                diaryEntity.setContent(content);
                alertDialog.dismiss();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//FromContext(this);
                imm.hideSoftInputFromWindow(diary.getWindowToken(), 0);
                if(mPresenter.insert(diaryEntity))
                {
                    Toast.makeText(DiaryActivity.this, getString(R.string.multi_alert_insert_good), Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(DiaryActivity.this, getString(R.string.multi_alert_insert_error), Toast.LENGTH_SHORT).show();
                }



                ClearListView();
                mPresenter.getList(date);
            }
        });

        Button exit = (Button)dialogView.findViewById(R.id.alert_dialog_write_btnexit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }

    private void getAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
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
                        DiaryEntity diaryEntity = new DiaryEntity();
                        diaryEntity.setDate(insertdate);
                        diaryEntity.setContent(imagePath);
                        mPresenter.insert(diaryEntity);
                        Toast.makeText(DiaryActivity.this, getString(R.string.multi_alert_insert_good), Toast.LENGTH_SHORT).show();
                        ClearListView();
                        mPresenter.getList(date);
                        imagePath = "";

                        break;
                    } catch (Exception ex) {
                        Toast.makeText(DiaryActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            case REQUEST_TAKE_ALBUM:
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        try {

                            photoURI = data.getData();
                            imagePath = getRealPathFromURI(photoURI);
                            File file = new File(imagePath);
                            if (!file.exists()) {
                                Toast.makeText(DiaryActivity.this, getString(R.string.multi_alert_file_not_exist), Toast.LENGTH_SHORT).show();
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                                return;
                            }
                            if (file.length() > 50 * 1024 * 1024) {
                                Toast.makeText(DiaryActivity.this, getString(R.string.multi_alert_file_exceed), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //50메가 이상이면 리턴..? file.size > 50*1024*1024

                            DiaryEntity diaryEntity = new DiaryEntity();
                            diaryEntity.setDate(insertdate);
                            diaryEntity.setContent(imagePath);
                            mPresenter.insert(diaryEntity);
                            Toast.makeText(DiaryActivity.this, getString(R.string.multi_alert_insert_good), Toast.LENGTH_SHORT).show();
                            ClearListView();
                            mPresenter.getList(date);
                            imagePath = "";


                        } catch (Exception ex) {
                            Toast.makeText(DiaryActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }

        }

    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);

    }

    private boolean isExistCameraApplication() {
        PackageManager packageManager = getPackageManager();

        Intent cameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        List camerApps = packageManager.queryIntentActivities(
                cameraApp, PackageManager.MATCH_DEFAULT_ONLY);
        return camerApps.size() > 0;

    }

    private File savePictureFile() {

        PermissionRequester.Builder requester = new PermissionRequester.Builder(this);
        int result = requester
                .create()
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, 20000, new PermissionRequester.OnClickDenyButtonListener() {
                            @Override
                            public void onClick(Activity activity) {

                            }
                        });

        if (result == PermissionRequester.ALREADY_GRANTED || result == PermissionRequester.REQUEST_PERMISSION) {
            if (true) //api 버전 체크 23이상이면 위의 퍼미션 해줘야 하는데에ㅔㅔ
            {
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fileName = "IMG_" + timestamp;

                File pictureStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                if (!pictureStorage.exists()) {
                    pictureStorage.mkdirs();
                }

                try {
                    imagePath = "";
                    File file = File.createTempFile(fileName, ".jpg", pictureStorage);
                    imagePath = file.getAbsolutePath();

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File f = new File(imagePath);
                    Uri contentUri = Uri.fromFile(file);
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
        return null;

    }
    @Override
    public void AddView(LinearLayout linearLayout)
    {
        dirlayout.addView(linearLayout);
        final View child = dirlayout.getChildAt(dirlayout.getChildCount()-1);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, child.getBottom());
            }
        });/*
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        },1000);*/
    }

    @Override
    public void ClearListView()
    {
        dirlayout.removeAllViewsInLayout();
    }

    @Override
    public void onBackPressed()
    {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
    @Override
    public void onStop()
    {
        super.onStop();
    }
}
