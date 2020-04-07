package com.Jungeun.wjdwjd95.emotional_trashcan.MoodChart;

import android.content.ContentValues;
import android.database.Cursor;


import com.Jungeun.wjdwjd95.emotional_trashcan.App;
import com.Jungeun.wjdwjd95.emotional_trashcan.BaseDAO;

import java.util.ArrayList;
import java.util.List;

public class MoodDAO  extends BaseDAO {
    public MoodDAO() {super(App.getMoodDB());}

    public int insert(MoodEntity test)
    {
        ContentValues values = null;
        try{
            values = microOrm.toContentValues(test);
        }catch (Exception ex)
        {

        }
        return (int) super.insert("Mood", values);
    }
    public int delete(String date)
    {
        int deleteRows =0;
        try{
            deleteRows =  super.delete("Mood", "Date = '" + date + "'", null);
        }catch (Exception ex)
        {

        }
        return deleteRows;
    }
    public int update(String date, String emoji) {
        try {
            ContentValues newValues = new ContentValues();
            newValues.put("Emoji", emoji);
            String Filter = "Date = '" + date + "'";
            return super.update("Mood", newValues, Filter, null);

        } catch (Exception ex) {
            return -1;
        }

    }
    public int SelectDate(String date)
    {
        List<MoodEntity> listData = new ArrayList<>();
        try{
            String strQry = "SELECT * FROM Mood WHERE Date == '" + date + "'";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, MoodEntity.class);
            }
        }catch (Exception ex)
        {

        }
        return listData.size();
    }

    public List<MoodEntity> SelectEmoji(String date)
    {
        List<MoodEntity> listData = new ArrayList<>();
        try{
            String strQry = "SELECT * FROM Mood WHERE Date == '" + date + "'";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, MoodEntity.class);
            }
        }catch (Exception ex)
        {

        }
        return listData;
    }
    public int SelectAllCount(String Date)
    {

        List<MoodListEntity> listData = new ArrayList<>();
        int count  = 0;
        try{
            String strQry =  "SELECT COUNT (*) FROM Mood WHERE Date like '"+Date+"%' AND Emoji != 'nothing'";
            Cursor cursor = super.rawQuery(strQry, null);
            cursor.moveToFirst();
            if (cursor != null) {
                count = cursor.getInt(0);
                // listData = microOrm.listFromCursor(cursor, MoodListEntity.class);
            }
        }catch (Exception ex)
        {

        }
        return count;
    }
    public void DropTmpTable()
    {

        try{

            String strQry =  "DROP TABLE IF EXISTS TEMP_TABLE";
            Cursor cursor = super.rawQuery(strQry, null);

        }catch (Exception ex)
        {

        }

    }
    public List<MoodListEntity> SelectCount(String Date)
    {
        List<MoodListEntity> listData = new ArrayList<>();
        try{
            DropTmpTable();
            String strQry1 ="", strQry2 = "", strQry3 = "", strQry4 = "";
            strQry1 = "DELETE FROM Mood WHERE Emoji = 'nothing'";
            strQry2 = "CREATE TEMPORARY TABLE IF NOT EXISTS TEMP_TABLE(Emoji VARCHAR(20) NOT NULL , Count int)";
            strQry3 = "INSERT INTO TEMP_TABLE SELECT Emoji, COUNT (*) FROM Mood WHERE Date like '"+Date+"%' GROUP BY Emoji";
            strQry4 = "DROP TABLE IF EXISTS TEMP_TABLE";
            mDb.execSQL(strQry1);
            mDb.execSQL(strQry2);
            mDb.execSQL(strQry3);
            String strQry = "SELECT * FROM TEMP_TABLE ORDER BY Count DESC";
            Cursor cursor = super.rawQuery(strQry, null);

            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, MoodListEntity.class);
                mDb.execSQL(strQry4);
            }
        }catch (Exception ex)
        {

        }
        return listData;
    }

}
