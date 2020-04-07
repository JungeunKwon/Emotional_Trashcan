package com.Jungeun.wjdwjd95.emotional_trashcan.Diary;

import android.content.ContentValues;
import android.database.Cursor;


import com.Jungeun.wjdwjd95.emotional_trashcan.App;
import com.Jungeun.wjdwjd95.emotional_trashcan.BaseDAO;

import java.util.ArrayList;
import java.util.List;

public class DiaryDAO extends BaseDAO {
    public DiaryDAO() {super(App.getDiaryDB());}

    public int insert(DiaryEntity test)
    {
        ContentValues values = null;
        try{
            values = microOrm.toContentValues(test);
        }catch (Exception ex)
        {

        }
        return (int) super.insert("Diary", values);
    }
    public int delete(String id)
    {
        int deleteRows =0;
        try{
            deleteRows =  super.delete("Diary", "ID = '" + id + "'", null);
        }catch (Exception ex)
        {

        }
        return deleteRows;
    }
    public int SelectDiarysize(String date)
    {
        List<DiaryEntity> listData = new ArrayList<>();
        try{
            String strQry = "SELECT * FROM Diary WHERE Date = '" + date + "'";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, DiaryEntity.class);
            }
        }catch (Exception ex)
        {

        }
        return listData.size();
    }
    public List<String> SelectDateMothly(String date) {

        List<String> listData = new ArrayList<>();
        try {
            String strQry = "SELECT DISTINCT Date FROM Diary WHERE Date LIKE '" + date + "%' ORDER BY Date";
            Cursor cursor = super.rawQuery(strQry, null);

            cursor.moveToFirst();
            if (cursor != null) {
                while (!cursor.isAfterLast())
                {
                    listData.add(cursor.getString(cursor.getColumnIndex("Date")));
                    cursor.moveToNext();
                }
            }
        } catch (Exception ex) {

        }
        return listData;
    }

    public List<DiaryEntity> SelectDate(String date)
    {
        List<DiaryEntity> listData = new ArrayList<>();
        try{
            String strQry = "SELECT * FROM Diary WHERE Date == '" + date + "'";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, DiaryEntity.class);
            }
        }catch (Exception ex)
        {

        }
        return listData;
    }

}
