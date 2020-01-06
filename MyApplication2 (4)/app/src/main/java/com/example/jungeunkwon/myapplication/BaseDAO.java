package com.example.jungeunkwon.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.chalup.microorm.MicroOrm;

public abstract class BaseDAO {
    public SQLiteDatabase mDb;
    public MicroOrm microOrm = new MicroOrm();

    public BaseDAO(SQLiteDatabase db) {
        this.mDb = db;
    }

    protected int delete(String tableName, String selection, String[] selectionArgs) {
        return mDb.delete(tableName, selection, selectionArgs);
    }

    protected long insert(String tableName, ContentValues values) {
        return mDb.insert(tableName, null, values);
    }

    //protected abstract <T> T cursorToEntity(Cursor cursor);

    protected Cursor query(String tableName, String[] columns, String selection, String[] selectionArgs, String sortOrder) {

        final Cursor cursor = mDb.query(tableName, columns, selection, selectionArgs, null, null, sortOrder);

        return cursor;
    }

    protected Cursor query(String tableName, String[] columns, String selection, String[] selectionArgs, String sortOrder, String limit) {

        return mDb.query(tableName, columns, selection, selectionArgs, null, null, sortOrder, limit);
    }

    protected Cursor query(String tableName, String[] columns,
                           String selection, String[] selectionArgs, String groupBy,
                           String having, String orderBy, String limit) {

        return mDb.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    protected int update(String tableName, ContentValues values, String selection, String[] selectionArgs) {
        return mDb.update(tableName, values, selection, selectionArgs);
    }

    protected Cursor rawQuery(String sql, String[] selectionArgs) {
        return mDb.rawQuery(sql, selectionArgs);
    }
}
