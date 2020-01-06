package com.example.jungeunkwon.myapplication.Widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.jungeunkwon.myapplication.App;
import com.example.jungeunkwon.myapplication.Diary.DiaryEntity;
import com.example.jungeunkwon.myapplication.Diary.DiaryPresenter;
import com.example.jungeunkwon.myapplication.R;
import com.example.jungeunkwon.myapplication.Trash.TrashEntity;
import com.example.jungeunkwon.myapplication.Trash.TrashPresenter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.widget.ListPopupWindow.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

public class WidgetDialogActivity extends Activity {
    EditText input;
    TextView txt;
    TextView innerDate;
    Date now;
    DiaryPresenter mDPresenter;
    TrashPresenter mTPresenter;
    String insertdate;
    DateFormat innerDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    final int DIALOG_DATE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_dialog_write);
        mDPresenter = new DiaryPresenter(WidgetDialogActivity.this, null);
        mTPresenter = new TrashPresenter(WidgetDialogActivity.this,null);

        final AlertDialog.Builder alert = new AlertDialog.Builder(WidgetDialogActivity.this);
        LayoutInflater inflater = WidgetDialogActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_camera,null);
        alert.setView(dialogView);
        alert.setCancelable(false);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(1000,800);

        TextView Title = (TextView)dialogView.findViewById(R.id.alert_dialog_camera_title);
        Title.setText(getString(R.string.multi_widget_title));
        TextView lbldate = (TextView)dialogView.findViewById(R.id.alerT_dialog_camera_lbldate);
        TextView txtdate = (TextView)dialogView.findViewById(R.id.alert_dialog_camera_txtDate);
        ImageView btndate = (ImageView)dialogView.findViewById(R.id.alert_dialog_camera_btnDate);
        txtdate.setVisibility(View.INVISIBLE);
        btndate.setVisibility(View.INVISIBLE);
        lbldate.setText(getString(R.string.multi_add_trash_caption));
        LinearLayout linearLayout = (LinearLayout)dialogView.findViewById(R.id.alert_dialog_camera_calendarlayout);
        linearLayout.setVisibility(View.GONE);
        alert.setCancelable(false);
        TextView lbldiary = (TextView)dialogView.findViewById(R.id.alert_dialog_camera_lblcamera);
        lbldiary.setText(getString(R.string.multi_diary));
        TextView lbltrash = (TextView)dialogView.findViewById(R.id.alert_dialog_camera_lblgallery);
        lbltrash.setText(getString(R.string.multi_trashcan));
        Button Diary = (Button)dialogView.findViewById(R.id.alert_dialog_camera_btncamera);
        Diary.setBackgroundResource(R.drawable.diary_2);
        Diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertforDiary();
                alertDialog.dismiss();

            }
        });
        Button Trash = (Button)dialogView.findViewById(R.id.alert_dialog_camera_btngallery);
        Trash.setBackgroundResource(R.drawable.trash_2);
        Trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                alertforTrash();
            }
        });

        Button exit = (Button)dialogView.findViewById(R.id.alert_dialog_camera_btnexit);
        exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void alertforDiary()
    {

        innerDate = (TextView)findViewById(R.id.alert_dialog_write_txtDate);
        now = new Date();
        insertdate = innerDateFormat.format(now);
        innerDate.setText(insertdate);
        ImageView btncalendar = (ImageView)findViewById(R.id.alert_dialog_write_btnDate);
        btncalendar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });
        final EditText diary = (EditText)findViewById(R.id.alert_dialog_write_edittxt);
        ImageButton Save = (ImageButton)findViewById(R.id.alert_dialog_write_btnsave);
        //Save.setWidth(500);
        Save.setOnClickListener(new OnClickListener() {
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
                    Toast.makeText(WidgetDialogActivity.this, getString(R.string.multi_alert_inputmsg), Toast.LENGTH_SHORT).show();
                    return;
                }

                DiaryEntity diaryEntity = new DiaryEntity();
                diaryEntity.setDate(innerDate.getText().toString());
                diaryEntity.setContent(content);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//FromContext(this);
                imm.hideSoftInputFromWindow(diary.getWindowToken(), 0);
                if(mDPresenter.insert(diaryEntity))
                {
                    Toast.makeText(WidgetDialogActivity.this, getString(R.string.multi_alert_insert_good), Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(WidgetDialogActivity.this, getString(R.string.multi_alert_insert_error), Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        Button exit = (Button)findViewById(R.id.alert_dialog_write_btnexit);
        exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void alertforTrash()
    {
        TextView title = (TextView)findViewById(R.id.alert_dialog_write_title);
        title.setText(getString(R.string.multi_add_trash));
        TextView lbldate = (TextView)findViewById(R.id.alert_dialog_write_lbldate);
        TextView txtdate = (TextView)findViewById(R.id.alert_dialog_write_txtDate);
        ImageView btndate = (ImageView)findViewById(R.id.alert_dialog_write_btnDate);
        txtdate.setVisibility(View.INVISIBLE);
        btndate.setVisibility(View.INVISIBLE);
        lbldate.setText(getString(R.string.multi_add_trash_caption));
        lbldate.setHeight(150);
        final EditText diary = (EditText)findViewById(R.id.alert_dialog_write_edittxt);
        ImageButton Save = (ImageButton)findViewById(R.id.alert_dialog_write_btnsave);
        //Save.setWidth(500);
        Save.setOnClickListener(new OnClickListener() {
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
                    Toast.makeText(WidgetDialogActivity.this, getString(R.string.multi_alert_inputmsg), Toast.LENGTH_SHORT).show();
                    return;
                }

                TrashEntity trashEntity = new TrashEntity();
                Date now = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                trashEntity.setDate(dateFormat.format(now));
                trashEntity.setContent(content);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//FromContext(this);
                imm.hideSoftInputFromWindow(diary.getWindowToken(), 0);
                if(mTPresenter.insertTrash(trashEntity))
                {
                    Toast.makeText(WidgetDialogActivity.this, getString(R.string.multi_alert_insert_good), Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(WidgetDialogActivity.this, getString(R.string.multi_alert_insert_error), Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
        Button exit = (Button)findViewById(R.id.alert_dialog_write_btnexit);
        exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                DatePickerDialog dpd = new DatePickerDialog(WidgetDialogActivity.this, DatePickerDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        setDate(innerDate, year, monthOfYear, dayOfMonth);

                    }
                }, year, month, day);

                return dpd;
        }
        return super.onCreateDialog(id);
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
