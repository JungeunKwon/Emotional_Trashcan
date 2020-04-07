package com.Jungeun.wjdwjd95.emotional_trashcan.Todo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.Jungeun.wjdwjd95.emotional_trashcan.Constants;
import com.Jungeun.wjdwjd95.emotional_trashcan.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Todoactivity extends Activity implements TodoView {

    Todopersenter mPresenter;
    LinearLayout liChecked;
    LinearLayout liUnchecked;
    Toolbar Toptoolbar;
    Toolbar Bottomtoolbar;
    final int DIALOG_DATE = 1;
    TextView innerDate;
    Date now;
    String insertdate;
    String id = "TodoList";
    String date;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    private static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        mPresenter = new Todopersenter(Todoactivity.this, this);
        prefs = Todoactivity.this.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        Boolean IsFirstOpen = prefs.getBoolean(Constants.SHARED_PREF_KEY_TODO_ACTIVITY_FIRST, true);
        if (IsFirstOpen) {
            final Dialog dialog = new Dialog(Todoactivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            LayoutInflater inflater = Todoactivity.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.intro_todo_template, null);

            ImageButton exit = (ImageButton) dialogView.findViewById(R.id.intro_todo_btngotit);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(Constants.SHARED_PREF_KEY_TODO_ACTIVITY_FIRST, false);
                    editor.apply();
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.setContentView(dialogView);
            dialog.show();
        }
        liChecked = (LinearLayout)findViewById(R.id.todo_activity_checkedlist);
        liUnchecked=(LinearLayout)findViewById(R.id.todo_activity_uncheckedlist);
        Toptoolbar = (Toolbar) findViewById(R.id.todo_activity_top);
        Toptoolbar.setTitle(getString(R.string.multi_TodoList));
        Toptoolbar.setNavigationIcon(R.drawable.goback);
        boolean ison = prefs.getBoolean(Constants.SHARED_PREF_KEY_PIN_STATUS, false);
        if(ison)
        {
            Toptoolbar.getMenu().clear();
            Toptoolbar.inflateMenu(R.menu.top_menu_for_todo);
            //   pushAlarm.releaseAlarm();
        }
        else {
            Toptoolbar.getMenu().clear();
            Toptoolbar.inflateMenu(R.menu.top_menu_for_todo_2);
        }
        Toptoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });
        Toptoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.todo_menu_pin: {
                        BtnPin_Click();
                        break;
                    }
                    case R.id.todo_menu_unpin:
                    {
                        BtnPin_Click();
                        break;
                    }

                }
                return false;
            }
        });
        Bottomtoolbar = (Toolbar) findViewById(R.id.todo_activity_bottom);
        Bottomtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //  Log.WriteLine("bottom clicked " + item.getItemId() + item.getTitle());
                switch (item.getItemId()) {
                    case R.id.bottom_menu_write_only_write:
                        BtnWrite_Click();
                        break;
                }
                return true;
            }
        });
        Bottomtoolbar.inflateMenu(R.menu.bottom_menu_write_only);
        getList();
    }
    public void BtnPin_Click()
    {
        try {
            SharedPreferences.Editor editor = prefs.edit();
            if(!mPresenter.isList())
            {
                Toptoolbar.getMenu().clear();
                Toast.makeText(Todoactivity.this, getString(R.string.multi_TodoList_no_todo_error), Toast.LENGTH_SHORT).show();
                editor.putBoolean(Constants.SHARED_PREF_KEY_PIN_STATUS, false);
                editor.apply();
                Toptoolbar.inflateMenu(R.menu.top_menu_for_todo_2);
                return;
            }

            boolean ison = prefs.getBoolean(Constants.SHARED_PREF_KEY_PIN_STATUS, false);

            if(ison)
            {

                unpintotop();

                Toast.makeText(Todoactivity.this, getString(R.string.multi_TodoList_set_unpin), Toast.LENGTH_SHORT).show();
            }
            else {

                pintotop();

                Toast.makeText(Todoactivity.this, getString(R.string.multi_TodoList_set_pin), Toast.LENGTH_SHORT).show();
            }

            //

            //pushAlarm =  new PushAlarm(GoalPlannerActivity.this);

        }catch (Exception ex)
        {
            ex.getMessage();
        }
    }
    public void pintotop()
    {

        NotificationCompat.Builder builder;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Constants.NOTI_PIN_TOP_CIDE, new Intent(this, Todoactivity.class), PendingIntent.FLAG_ONE_SHOT);
        if(Build.VERSION.SDK_INT >= 26)
        {
            NotificationChannel mChannal = new NotificationChannel(id,id, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannal);
            builder = new NotificationCompat.Builder(this,mChannal.getId());
        }else
        {
            builder = new NotificationCompat.Builder(this);
        }

        List<TodoEntity> list = mPresenter.getTodoEntityList();

        // Drawable drawable = context.getResources().getDrawable(id);
        // = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.heart2png)
                .setTicker("TODO LIST")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("TODO LIST")
                .setContentText("Drag to see")
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setOngoing(true);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        SharedPreferences.Editor editor = prefs.edit();
        if(!mPresenter.isList())
        {
            Toptoolbar.getMenu().clear();
            Toptoolbar.inflateMenu(R.menu.top_menu_for_todo_2);

            editor.putBoolean(Constants.SHARED_PREF_KEY_PIN_STATUS, false);
            editor.apply();
            notificationManager.cancelAll();
            return;
        }
        else
        {
            Toptoolbar.getMenu().clear();
            Toptoolbar.inflateMenu(R.menu.top_menu_for_todo);
            editor.putBoolean(Constants.SHARED_PREF_KEY_PIN_STATUS, true);
            editor.apply();
        }
        if(list != null && list.size() > 0 )
        {
            for(int i = 0; i<list.size(); i++)
            {
                if(!list.get(i).getChecked())
                {
                    inboxStyle.addLine(list.get(i).getContent());
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            CharSequence channelName  = "노티페케이션 채널";
            String description = "오레오 이상을 위한 것임";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(Constants.NOTI_PIN_TOP_CIDE+"" ,channelName , importance);
            channel.setDescription(description);

            // 노티피케이션 채널을 시스템에 등록
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);

        }// Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

        builder.setStyle(inboxStyle);
        notificationManager.cancelAll();
        notificationManager.notify(Constants.NOTI_PIN_TOP_CIDE, builder.build());
    }
    public void unpintotop()
    {
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Constants.NOTI_PIN_TOP_CIDE, new Intent(this, Todoactivity.class), PendingIntent.FLAG_ONE_SHOT);
        if(Build.VERSION.SDK_INT >= 26)
        {
            NotificationChannel mChannal = new NotificationChannel(id,id, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannal);
            builder = new NotificationCompat.Builder(this,mChannal.getId());
        }else
        {
            builder = new NotificationCompat.Builder(this);
        }

        SharedPreferences.Editor editor = prefs.edit();
        Toptoolbar.getMenu().clear();
        Toptoolbar.inflateMenu(R.menu.top_menu_for_todo_2);
        editor.putBoolean(Constants.SHARED_PREF_KEY_PIN_STATUS, false);
        editor.apply();
        notificationManager.cancelAll();
    }
    public void BtnWrite_Click()
    {
        final AlertDialog.Builder inneralert = new AlertDialog.Builder(Todoactivity.this);
        LayoutInflater inflater = Todoactivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_write,null);
        inneralert.setView(dialogView);
        inneralert.setCancelable(false);
        TextView title = (TextView)dialogView.findViewById(R.id.alert_dialog_write_title);
        title.setText(getString(R.string.multi_throw_add_note));
        ImageView btncalendar = (ImageView)dialogView.findViewById(R.id.alert_dialog_write_btnDate);
        btncalendar.setVisibility(View.GONE);
        TextView lbldate = (TextView)dialogView.findViewById(R.id.alert_dialog_write_lbldate);
        lbldate.setText("TO DO : ");
        final AlertDialog alertDialog = inneralert.create();
        alertDialog.show();
        final EditText todo = (EditText)dialogView.findViewById(R.id.alert_dialog_write_edittxt);
        ImageButton Save = (ImageButton)dialogView.findViewById(R.id.alert_dialog_write_btnsave);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = todo.getText().toString();
               /* if(content.length() > 200)
                {
                    Toast.makeText(DiaryActivity.this, "It should be less than 200 charter", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                if(content.equals("")||content.isEmpty())
                {
                    Toast.makeText(Todoactivity.this, getString(R.string.multi_alert_inputmsg), Toast.LENGTH_SHORT).show();
                    return;
                }
                alertDialog.dismiss();

                if(mPresenter.insert(content))
                {
                    Toast.makeText(Todoactivity.this, getString(R.string.multi_alert_insert_good), Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(Todoactivity.this, getString(R.string.multi_alert_insert_error), Toast.LENGTH_SHORT).show();
                }
                getList();

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
                DatePickerDialog dpd = new DatePickerDialog(Todoactivity.this, DatePickerDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {
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
        mPresenter.update(innerDate.getTag().toString(),insertdate);

    }
    public void getList()
    {
        mPresenter.getList();
    }
    @Override
    public void addcheckedview(View view)
    {
        liChecked.addView(view);
    }
    public void adduncheckedview(View view)
    {
        liUnchecked.addView(view);
    }
    public void chagedate(TextView view) {


        innerDate = view;
        showDialog(DIALOG_DATE);

    }
    public void clear()
    {
        liChecked.removeAllViewsInLayout();
        liUnchecked.removeAllViewsInLayout();
    }
    @Override
    public void updatepin()
    {
        boolean ison = prefs.getBoolean(Constants.SHARED_PREF_KEY_PIN_STATUS, false);
        if(ison)
        {
            pintotop();
        }
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
