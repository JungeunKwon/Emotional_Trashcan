package com.Jungeun.wjdwjd95.emotional_trashcan;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.Jungeun.wjdwjd95.emotional_trashcan.Diary.DiaryDatabase;
import com.Jungeun.wjdwjd95.emotional_trashcan.GoalPlanner.GoalDatabase;
import com.Jungeun.wjdwjd95.emotional_trashcan.MoodChart.MoodDatabase;
import com.Jungeun.wjdwjd95.emotional_trashcan.Todo.Tododatabase;
import com.Jungeun.wjdwjd95.emotional_trashcan.Trash.Trash2Database;

public class App extends Application {
    private static Application instance;
    private static SQLiteDatabase DiaryDB;
   // private static SQLiteDatabase TrashDB;
    private static SQLiteDatabase MoodDB;
    private static SQLiteDatabase TodoDB;
    private static SQLiteDatabase Trash2DB;
    private static SQLiteDatabase GoalDB;

    public static Context getContext() {
        return instance.getApplicationContext();
    }
    public static SQLiteDatabase getDiaryDB() {
        return DiaryDB;
    }
    /*public static SQLiteDatabase getTrashDB() {
        return TrashDB;
    }*/
    public static SQLiteDatabase getTrash_DB() {return  Trash2DB;}
    public static SQLiteDatabase getTodoDB() {
        return TodoDB;
    }

    public static SQLiteDatabase getMoodDB() {
        return MoodDB;
    }
    public static SQLiteDatabase getGoalDB() {return GoalDB; }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        DiaryDB = DiaryDatabase.getDatabase(this);
        Trash2DB = Trash2Database.getDatabase(this);
      //  TrashDB = TrashDatabase.getDatabase(this);
        MoodDB = MoodDatabase.getDatabase(this);
        TodoDB = Tododatabase.getDatabase(this);
        GoalDB = GoalDatabase.getDatabase(this);

    }

}
