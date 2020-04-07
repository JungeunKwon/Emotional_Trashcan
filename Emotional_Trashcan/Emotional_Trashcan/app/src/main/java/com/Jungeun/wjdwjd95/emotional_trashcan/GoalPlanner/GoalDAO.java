package com.Jungeun.wjdwjd95.emotional_trashcan.GoalPlanner;

import android.content.ContentValues;
import android.database.Cursor;

import com.Jungeun.wjdwjd95.emotional_trashcan.App;
import com.Jungeun.wjdwjd95.emotional_trashcan.BaseDAO;
import com.Jungeun.wjdwjd95.emotional_trashcan.Constants;

import java.util.ArrayList;
import java.util.List;

public class GoalDAO extends BaseDAO {
    public GoalDAO() {super(App.getGoalDB());}

    public int insert(GoalEntity test)
    {
        ContentValues values = null;
        try{
            values = microOrm.toContentValues(test);
        }catch (Exception ex)
        {

        }
        return (int) super.insert("Goal", values);
    }
    public int insertdetail(GoalDetailEntity test)
    {
        ContentValues values = null;
        try{
            values = microOrm.toContentValues(test);
        }catch (Exception ex)
        {

        }
        return (int) super.insert("GoalDetail", values);
    }
    public int delete(String id)
    {
        int deleteRows =0;
        try{
            deleteRows =  super.delete("Goal", "ID = '" + id + "'", null);
        }catch (Exception ex)
        {

        }
        return deleteRows;
    }
    public int deletedetail(String id)
    {
        int deleteRows =0;
        try{
            deleteRows =  super.delete("GoalDetail", "ID = '" + id + "'", null);
        }catch (Exception ex)
        {

        }
        return deleteRows;
    }
    public int deletedetailbydate(String date,String id)
    {
        int deleteRows =0;
        try{
            deleteRows =  super.delete("GoalDetail", "Date = '" + date + "' AND ID = '" + id + "'", null);
        }catch (Exception ex)
        {

        }
        return deleteRows;
    }
    public List<GoalEntity> IsGoalbydate(String date)
    {
        List<GoalEntity> listData = new ArrayList<>();
        try{
            String strQry = "SELECT * FROM Goal WHERE EndDate >='"+ date + "' AND Status = '" +Constants.GOAL_STATUS_ING + "'";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, GoalEntity.class);
            }
        }catch (Exception ex)
        {

        }
        return listData;
    }
    public List<GoalDetailEntity> IsDetail(String date, String id)
    {
        List<GoalDetailEntity> listData = new ArrayList<>();
        try{
            String strQry = "SELECT * FROM GoalDetail WHERE Date = '" + date + "' AND ID ='"+ id + "'";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, GoalDetailEntity.class);
            }
        }catch (Exception ex)
        {

        }
        return listData;
    }
    public int SelectDate(String date)
    {
        List<GoalDetailEntity> listData = new ArrayList<>();
        try{
            String strQry = "SELECT * FROM GoalDetail WHERE Date == '" + date + "' ORDER BY ID DESC";;
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, GoalDetailEntity.class);
            }
        }catch (Exception ex)
        {

        }
        return listData.size();
    }
    public int SelectDate(String Id, String date)
    {
        List<GoalDetailEntity> listData = new ArrayList<>();
        try{
            String strQry = "SELECT * FROM GoalDetail WHERE ID =='"+Id+"' AND Date == '" + date + "' ORDER BY ID DESC";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, GoalDetailEntity.class);
            }
        }catch (Exception ex)
        {

        }
        return listData.size();
    }
    public String SelectDateIcon(String date)
    {
        String result = "";
        List<GoalDetailEntity> listData = new ArrayList<>();
        List<GoalEntity> list = new ArrayList<>();
        try{
            String strQry = "SELECT * FROM GoalDetail WHERE Date == '" + date + "' ORDER BY ID DESC";
            Cursor cursor = super.rawQuery(strQry, null);

            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, GoalDetailEntity.class);
            }
            if(listData != null)
            {
                strQry = "SELECT * FROM Goal WHERE ID = '"+listData.get(0).getID() + "'";
                cursor = super.rawQuery(strQry, null);
                if (cursor != null) {
                    list = microOrm.listFromCursor(cursor, GoalEntity.class);
                }
                if(list != null)
                {
                    result = list.get(0).getIcon();
                }
            }
            else
                result = "";

        }catch (Exception ex)
        {
            result = "";
        }
        return  result;
    }
    public List<GoalEntity> gettotalGoal()
    {
        List<GoalEntity> listData = new ArrayList<>();
        try{
            String strQry = "SELECT * FROM Goal";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, GoalEntity.class);
            }
        }catch (Exception ex)
        {

        }
        return listData;
    }
    public List<GoalEntity> getGoalbyID(String ID)
    {
        List<GoalEntity> listData = new ArrayList<>();
        try{
            String strQry = "SELECT * FROM Goal WHERE ID = '" + ID + "'";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, GoalEntity.class);
            }
        }catch (Exception ex)
        {

        }
        return listData;
    }
    public List<GoalEntity> IsGoal()
    {
        List<GoalEntity> listData = new ArrayList<>();
        try{
            String strQry = "SELECT * FROM Goal WHERE Status = '" + Constants.GOAL_STATUS_ING + "'";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, GoalEntity.class);
            }
        }catch (Exception ex)
        {

        }
        return listData;
    }

    public int update(String id, String Status, String ConfirmDate)
    {
        try {
            ContentValues newValues = new ContentValues();
            newValues.put("Status", Status);
            newValues.put("ConfirmDate", ConfirmDate);
            String Filter = "ID = '" + id+ "'" ;
            return super.update("Goal", newValues, Filter,null);

        }catch (Exception ex)
        {

            return -1;
        }

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
    public List<GoalCountListEntity> SelectCount(String ID)
    {
        List<GoalCountListEntity> listData = new ArrayList<>();
        try{
            DropTmpTable();
            String strQry1 ="", strQry2 = "", strQry3 = "", strQry4 = "";
            strQry1 = "CREATE TEMPORARY TABLE IF NOT EXISTS TEMP_TABLE(ID VARCHAR(20) NOT NULL , Count int)";
            strQry2 = "INSERT INTO TEMP_TABLE SELECT ID, COUNT (*) FROM GoalDetail WHERE ID='" + ID + "' GROUP BY ID";
            strQry3 = "select * from TEMP_TABLE";
            strQry4 = "DROP TABLE IF EXISTS TEMP_TABLE";
            mDb.execSQL(strQry1);
            mDb.execSQL(strQry2);
            Cursor cursor = super.rawQuery(strQry3, null);

            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, GoalCountListEntity.class);
                mDb.execSQL(strQry4);
            }
        }catch (Exception ex)
        {

        }
        return listData;
    }

}

