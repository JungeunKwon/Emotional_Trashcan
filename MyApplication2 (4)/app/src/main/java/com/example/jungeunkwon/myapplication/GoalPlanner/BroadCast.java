package com.example.jungeunkwon.myapplication.GoalPlanner;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.jungeunkwon.myapplication.Constants;
import com.example.jungeunkwon.myapplication.MainActivity;
import com.example.jungeunkwon.myapplication.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class BroadCast extends BroadcastReceiver {
    String INTETN_ACTION = Intent.ACTION_BOOT_COMPLETED;
    String id = "GoalPlannerAlarm";
    final String TAG = "BOOT_START_SERVICE";
    GoalDAO goalDAO = new GoalDAO();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        try {
            //알람시간이 되었을때 onreceive 호출
            List<GoalEntity> list = goalDAO.IsGoal();
            if(list != null && list.size() > 0)
            {
               Date date = new Date();
               DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                List<GoalDetailEntity> detail = goalDAO.IsDetail(dateFormat.format(date), list.get(0).getID());
                if(detail != null && detail.size() >0 )
                {
                    return;
                }
            NotificationCompat.Builder builder;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, GoalPlannerActivity.class), PendingIntent.FLAG_ONE_SHOT);
            if(Build.VERSION.SDK_INT >= 26)
            {
                NotificationChannel mChannal = new NotificationChannel(id,id, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(mChannal);
                builder = new NotificationCompat.Builder(context,mChannal.getId());
            }else
            {
                builder = new NotificationCompat.Builder(context);
            }
            SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
            String img = prefs.getString(Constants.SHARED_PREF_KEY_CURRENT_GOAL_IMG, "goal_1");
            String content = prefs.getString(Constants.SHARED_PREF_KEY_CURRENT_GOAL_NAME, "");
            int id = context.getResources().getIdentifier(img, "drawable", context.getPackageName());
           // Drawable drawable = context.getResources().getDrawable(id);
           // = new Notification.Builder(context);
            builder.setSmallIcon(id)
                    .setTicker("GOAL PLANNER")
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("GOAL PLANNER")
                    .setContentText(content)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            notificationManager.notify(Constants.NOTI_ALARM_CODE, builder.build());
            }
            else
            {
                PushAlarm pushAlarm = new PushAlarm(context);
                pushAlarm.releaseAlarm();
                return;
            }
        }catch (Exception ex)
        {
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
