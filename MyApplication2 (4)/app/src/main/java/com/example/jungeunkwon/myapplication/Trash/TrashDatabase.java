package com.example.jungeunkwon.myapplication.Trash;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TrashDatabase {


    private static final String TAG = "TrashDatabase";
    private static final String DATABASE_NAME = "TrashDatabase.db";
    private static final int DATABASE_VERSION = 2;
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

            db.execSQL(TrashSchema.SQL_TABLE_CREATE);
            db.execSQL(TrashCommentdatabaseSchema.SQL_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version "
                    + oldVersion + " to "
                    + newVersion + " which destroys all old data");

            //db.execSQL("DROP TABLE IF EXISTS " + IUserSchema.USER_TABLE);
            //onCreate(db);
            try {
                switch (oldVersion) {
                    case 1:
                        db.beginTransaction();
                        db.execSQL("ALTER TABLE Trash ADD COLUMN CommentSeq Integer DEFAULT 0");
                        db.setTransactionSuccessful();
                    case 2:
                }
            }catch (Exception ex)
            {
                db.endTransaction();
            }
           /* for (int i = oldVersion; i < newVersion; ++i) {
                try {

                }catch (Exception ex)
                {

                }finally {
                    db.endTransaction();
                };

            }*/
            String migrationName = String.format(TAG + "_from_%d_to_%d.sql", oldVersion, (oldVersion + 1));
            Log.d(TAG, "Looking for migration file: " + migrationName);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
