package com.example.jungeunkwon.myapplication.Todo;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Tododatabase {
    private static final String TAG = "TodoDatabase";
    private static final String DATABASE_NAME = "Todo.db";
    // Increment DB Version on any schema change
    private static final int DATABASE_VERSION = 1;
    //public static UserDao mUserDao;
    //private final Context mContext;
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
            db.execSQL(TododatabaseSchema.SQL_TABLE_CREATE);
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
                readAndExecuteSQLScript(db, context, migrationName);
            }
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        private void readAndExecuteSQLScript(SQLiteDatabase db, Context ctx, String fileName) {
            if (TextUtils.isEmpty(fileName)) {
                Log.d(TAG, "SQL script file name is empty");
                return;
            }

            fileName = "database/" + fileName;

            Log.d(TAG, "Script found. Executing...");
            AssetManager assetManager = ctx.getAssets();
            BufferedReader reader = null;

            try {
                InputStream is = assetManager.open(fileName);
                InputStreamReader isr = new InputStreamReader(is);
                reader = new BufferedReader(isr);
                executeSQLScript(db, reader);
            } catch (IOException ex) {

                Log.e(TAG, "IOException:", ex);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {

                        Log.e(TAG, "IOException:", ex);
                    }
                }
            }
        }

        private void executeSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
            String line;
            StringBuilder statement = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                statement.append(line);
                statement.append("\n");
                if (line.endsWith(";")) {
                    db.execSQL(statement.toString());
                    statement = new StringBuilder();
                }
            }
        }
    }
}
