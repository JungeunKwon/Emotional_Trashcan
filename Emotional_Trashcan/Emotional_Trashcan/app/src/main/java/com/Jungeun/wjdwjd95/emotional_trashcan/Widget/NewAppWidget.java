package com.Jungeun.wjdwjd95.emotional_trashcan.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


import com.Jungeun.wjdwjd95.emotional_trashcan.R;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    private static final String SHOW_DIALOG_ACTION =
            "com.Jungeun.wjdwjd95.Widget.NewAppWidget";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        Intent writeintent=new Intent(context, WidgetDialogActivity.class);
        PendingIntent writependingIntent=PendingIntent.getActivity(context, 0, writeintent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_btn, writependingIntent);

        Intent cameraintent=new Intent(context, WidgetCameraActivity.class);
        PendingIntent camerpendingIntent=PendingIntent.getActivity(context, 0, cameraintent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_btncamera, camerpendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        /*for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }*/
        prepareWidget(context);

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
    private void prepareWidget(Context context) {

        AppWidgetManager appWidgetManager =
                AppWidgetManager.getInstance(context);

        ComponentName thisWidget =
                new ComponentName(context, NewAppWidget.class);

        // Fetch all instances of our widget
        // from the AppWidgetManager manager.
        // The user may have added multiple
        // instances of the same widget to the
        // home screen
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.new_app_widget);

            // Create intent that launches the
            // modal popup activity
            Intent writeintent = new Intent(context, NewAppWidget.class);
            writeintent.setAction(SHOW_DIALOG_ACTION);

            PendingIntent writependingIntent = PendingIntent.getBroadcast(
                    context, 0, writeintent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent cameraintent = new Intent(context, NewAppWidget.class);
            cameraintent.setAction("CAMERA");

            PendingIntent camerapendingIntent = PendingIntent.getBroadcast(
                    context, 0, cameraintent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Attach the onclick listener to
            // the widget button
            remoteViews.setOnClickPendingIntent(R.id.appwidget_btn,
                    writependingIntent);
            remoteViews.setOnClickPendingIntent(R.id.appwidget_btncamera,
                    camerapendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);

        }
    }
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(SHOW_DIALOG_ACTION)) {
            Intent i = new Intent(context, WidgetDialogActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(i);
        } else if (intent.getAction().equals("CAMERA"))
        {
            Intent i = new Intent(context, WidgetCameraActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(i);
        }
        super.onReceive(context, intent);
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

