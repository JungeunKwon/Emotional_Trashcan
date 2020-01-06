package com.example.jungeunkwon.myapplication;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.jungeunkwon.myapplication.Diary.DiaryDatabase;
import com.example.jungeunkwon.myapplication.GoalPlanner.GoalDatabase;
import com.example.jungeunkwon.myapplication.MoodChart.MoodDatabase;
import com.example.jungeunkwon.myapplication.Todo.Tododatabase;
import com.example.jungeunkwon.myapplication.Trash.TrashDatabase;

public class App extends Application {
    private static Application instance;
    private static SQLiteDatabase DiaryDB;
    private static SQLiteDatabase TrashDB;
    private static SQLiteDatabase MoodDB;
    private static SQLiteDatabase TodoDB;
    private static SQLiteDatabase GoalDB;
    public static Context getContext() {
        return instance.getApplicationContext();
    }
    public static SQLiteDatabase getDiaryDB() {
        return DiaryDB;
    }
    public static SQLiteDatabase getTrashDB() {
        return TrashDB;
    }
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
        TrashDB = TrashDatabase.getDatabase(this);
        MoodDB = MoodDatabase.getDatabase(this);
        TodoDB = Tododatabase.getDatabase(this);
        GoalDB = GoalDatabase.getDatabase(this);
    }

}
