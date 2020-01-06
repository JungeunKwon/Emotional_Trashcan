package com.example.jungeunkwon.myapplication.Diary;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DiaryDatabase {
    private static final String TAG = "DiaryDatabase";
    private static final String DATABASE_NAME = "DiaryDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private DatabaseHelper mDbHelper;

    private static SQLiteDatabase INSTANCE;

    public static SQLiteDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseHelper(context).getWritableDatabase();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
/*
    public AppDatabase(Context context) {
        this.mContext = context;
    }

    public AppDatabase open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();

        //mUserDao = new UserDao(mDb);

        return this;
    }

    public void close() {
        mDbHelper.close();
    }*/

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private Context context;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DiarySchema.SQL_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version "
                    + oldVersion + " to "
                    + newVersion + " which destroys all old data");

            //db.execSQL("DROP TABLE IF EXISTS " + IUserSchema.USER_TABLE);
            //onCreate(db);
            for (int i = oldVersion; i < newVersion; ++i) {
                String migrationName = String.format(TAG + "_from_%d_to_%d.sql", i, (i + 1));
                Log.d(TAG, "Looking for migration file: " + migrationName);
            }
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}

