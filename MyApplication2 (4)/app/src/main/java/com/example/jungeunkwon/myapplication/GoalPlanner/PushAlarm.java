package com.example.jungeunkwon.myapplication.GoalPlanner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import com.example.jungeunkwon.myapplication.Constants;
import com.example.jungeunkwon.myapplication.MainActivity;
import com.example.jungeunkwon.myapplication.R;

import java.util.Calendar;

public class PushAlarm {
    private Context context;
    final String ALRAM_SET = "ALARAM";

    public PushAlarm(Context _context)
    {
        this.context = _context;
    }

    public void Alarm(int housOfDay, int minutes)
    {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BroadCast.class);
        intent.setAction(ALRAM_SET);
        PendingIntent sender = PendingIntent.getBroadcast(context,Constants.NOTI_ALARM_CODE,intent,0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, housOfDay);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND,0);
        if(Build.VERSION.SDK_INT >= 26)
        {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender);
        }else
        {
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender);
        }

        //am.setRepeating(
       // am.set(AlarmManager.RTC, calendar.getTimeInMillis(),sender);
    }
    public void releaseAlarm()
    {
        try {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, BroadCast.class);
            intent.setAction(ALRAM_SET);
            PendingIntent sender = PendingIntent.getBroadcast(context, Constants.NOTI_ALARM_CODE, intent, 0);
            am.cancel(sender);
            SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Constants.SHARED_PREF_KEY_NOTI_STATUS, false);
            editor.apply();
        }catch (Exception ex)
        {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
