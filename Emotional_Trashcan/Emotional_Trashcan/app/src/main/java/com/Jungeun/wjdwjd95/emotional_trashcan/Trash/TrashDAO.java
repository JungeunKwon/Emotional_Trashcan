package com.Jungeun.wjdwjd95.emotional_trashcan.Trash;

import android.content.ContentValues;
import android.database.Cursor;

import com.Jungeun.wjdwjd95.emotional_trashcan.App;
import com.Jungeun.wjdwjd95.emotional_trashcan.BaseDAO;

import java.util.ArrayList;
import java.util.List;


public class TrashDAO extends BaseDAO {
    public TrashDAO() {super(App.getTrash_DB());}

    public int insertTrash(TrashEntity test)
    {
        ContentValues values = null;
        try{
            values = microOrm.toContentValues(test);
        }catch (Exception ex)
        {

        }
        return (int) super.insert("Trash", values);
    }
    public int deletepasseddate(String date)
    {
        int deleteRows =0;
        try{
            deleteRows =  super.delete("Trash", "Date < '"+date+"'", null);
        }catch (Exception ex)
        {

        }

        return deleteRows;
    }
    public int insertTrashComment(TrashCommentEntity data) {
        ContentValues values = null;
        try{
            values = microOrm.toContentValues(data);
            values.remove("Seq");
        }catch (Exception ex)
        {

        }
        return (int) super.insert("TrashComment", values);
    }
    public List<TrashEntity> selectTodayTrash(String date)
    {
        List<TrashEntity> listData =new ArrayList<>();
        try {
            String strQry = "SELECT * FROM Trash WHERE Date =='" +date+"'";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, TrashEntity.class);
            }
        }catch (Exception ex)
        {

        }

        return listData;
    }
    public List<TrashEntity> selectIsUsingSeq(String Seq)
    {
        List<TrashEntity> listData =new ArrayList<>();
        try {
            String strQry = "SELECT * FROM Trash WHERE CommentSeq =='" +Seq+"'";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, TrashEntity.class);
            }
        }catch (Exception ex)
        {

        }

        return listData;
    }
    public boolean IsUsingSeq(String Seq)
    {
        boolean result = false;
        List<TrashEntity> listData =new ArrayList<>();

        try {
            String strQry = "SELECT * FROM Trash WHERE CommentSeq =='" +Seq+"'";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, TrashEntity.class);
            }
        }catch (Exception ex)
        {

        }
        if(listData != null && listData.size() > 0)
        {
            result = true;
        }
        else
        {result = false;}
        return result;
    }
    public boolean selectIsExist(String Seq)
    {
        List<TrashCommentEntity> listData =new ArrayList<>();
        boolean result = false;
        try {
            String strQry = "SELECT * FROM TrashComment WHERE Seq =='" +Seq+"'";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, TrashCommentEntity.class);
            }
        }catch (Exception ex)
        {

        }

        if(listData != null && listData.size() > 0)
        {
            result = true;
        }else {
            result = false;
        }
        return result;
    }
    public void refreshTrashComment()
    {
        int deleteRows =0;
        try{
            deleteRows =   super.delete("TrashComment", "1", null);
            mDb.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name='TrashComment'");
        }catch (Exception ex)
        {

        }


    }
    public List<TrashCommentEntity> selectAllComment()
    {
        List<TrashCommentEntity> listData =new ArrayList<>();
        try {
            String strQry = "SELECT * FROM TrashComment";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, TrashCommentEntity.class);
            }
        }catch (Exception ex)
        {

        }

        return listData;
    }
    public int selectlowestnum()
    {
        List<TrashCommentEntity> listData =new ArrayList<>();
        try {
            String strQry = "SELECT * FROM TrashComment ORDER BY Seq limit 1";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, TrashCommentEntity.class);
            }
        }catch (Exception ex)
        {

        }
        if(listData != null && listData.size() >0)
        {
            return listData.get(0).getSeq();
        }
        else
            return 0;
    }
    public int deleteComment(String Seq){
        int deleteRows =0;
        try{
            deleteRows =  super.delete("TrashComment", "Seq = '" + Seq + "'", null);
        }catch (Exception ex)
        {

        }

        return deleteRows;
    }
    public int delete(String id){
        int deleteRows =0;
        try{
            deleteRows =  super.delete("Trash", "Title = '" + id + "'", null);
        }catch (Exception ex)
        {

        }

        return deleteRows;
    }
    public int selectMaxCount()
    {
        List<TrashCommentEntity> listData =new ArrayList<>();
        try {
            String strQry = "SELECT * FROM TrashComment ORDER BY Seq DESC limit 1";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, TrashCommentEntity.class);
            }
        }catch (Exception ex)
        {

        }
        if(listData != null && listData.size() >0)
        {
            return listData.get(0).getSeq();
        }
        else
            return 0;
    }
    public int getCommentCount()
    {
        int count = 0;
        try {
            String strQry = "SELECT * FROM TrashComment";
            Cursor cursor = super.rawQuery(strQry, null);
            if (cursor != null) {
                count = cursor.getCount();
            }

        }catch (Exception ex)
        {

        }
        return count;
    }
    public List<TrashCommentEntity> selsectThrowcommentbySeq(int Seq)
    {
        List<TrashCommentEntity> listData =new ArrayList<>();
        try {
            String strQry = "SELECT * FROM TrashComment WHERE Seq='" + Seq + "'";
            Cursor cursor = super.rawQuery(strQry, null);
            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, TrashCommentEntity.class);
            }
        }catch (Exception ex)
        {

        }

        return listData;
    }

}
