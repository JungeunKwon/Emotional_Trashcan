package com.example.jungeunkwon.myapplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class Constants {
    public static final String SHARED_PREF_NAME = "Emotional_Trash";
    public static final String SHARED_PREF_KEY_THROW_ACTIVITY_FIRST = "TRASHACTIVITY";
    public static final String SHARED_PREF_KEY_MAIN_ACTIVITY_FIRST = "MAINACTIVITY";
    public static final String SHARED_PREF_KEY_TODO_ACTIVITY_FIRST = "TODOACTIVITY";
    public static final String SHARED_PREF_KEY_MOOD_ACTIVITY_FIRST = "MOODACTIVITY";
    public static final String SHARED_PREF_KEY_DIARY_ACTIVITY_FIRST = "DIARYACTIVITY";
    public static final String SHARED_PREF_KEY_GOALPLANNER_ACTIVITY_FIRST = "GOALPLANNERACTIVITY";
    public static final String SHARED_PREF_KEY_CURRENT_GOAL_IMG = "GOALIMG";
    public static final String SHARED_PREF_KEY_CURRENT_ID = "GOALID";
    public static final String SHARED_PREF_KEY_CURRENT_GOAL_NAME = "GOALNAME";
    public static final String SHARED_PREF_KEY_NOTI_STATUS = "NOTISTATUS";
    public static final String SHARED_PREF_KEY_PIN_STATUS = "PINSTATUS";


    public static final String GOAL_STATUS_FINISH = "Finish";
    public static final String GOAL_STATUS_FAIL = "Fail";
    public static final String GOAL_STATUS_ING = "Ing";

    public static int NOTI_ALARM_CODE = 0;
    public static int NOTI_PIN_TOP_CIDE = 1;

    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat iddateFormat = new SimpleDateFormat("yyyyMMddHH:mm:ss");
}
