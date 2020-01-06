package com.example.jungeunkwon.myapplication.Todo;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.jungeunkwon.myapplication.App;
import com.example.jungeunkwon.myapplication.BaseDAO;

import java.util.ArrayList;
import java.util.List;

public class TodoDao extends BaseDAO {
    public TodoDao(){super(App.getTodoDB());}

    public int insert(TodoEntity data) {
        ContentValues values = null;
        try{
            values = microOrm.toContentValues(data);
        }catch (Exception ex)
        {

        }
        return (int) super.insert("Todo", values);
    }

    public List<TodoEntity> selectTodo()
    {
        List<TodoEntity> listData =new ArrayList<>();
        try {
            String strQry = "SELECT * FROM Todo ORDER BY EndDate";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, TodoEntity.class);
            }
        }catch (Exception ex)
        {

        }

        return listData;
    }
    public List<TodoEntity> selectuncheckedTodo()
    {
        List<TodoEntity> listData =new ArrayList<>();
        try {
            String strQry = "SELECT * FROM Todo WHERE IsChecked = 0 ORDER BY EndDate";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, TodoEntity.class);
            }
        }catch (Exception ex)
        {

        }

        return listData;
    }

    public List<TodoEntity> selectid(String id)
    {
        List<TodoEntity> listData =new ArrayList<>();
        try {
            String strQry = "SELECT * FROM Todo WHERE Id = '" + id + "'";
            Cursor cursor = super.rawQuery(strQry, null);


            if (cursor != null) {
                listData = microOrm.listFromCursor(cursor, TodoEntity.class);
            }
        }catch (Exception ex)
        {

        }

        return listData;
    }
    public int update(String id, Boolean IsChecked, String EndDate)
    {
        try {
            ContentValues newValues = new ContentValues();
            newValues.put("IsChecked", IsChecked);
            newValues.put("EndDate",EndDate);

            String Filter = "ID = '" + id+ "'" ;
            return super.update("Todo", newValues, Filter,null);

        }catch (Exception ex)
        {

            return -1;
        }

    }
    public int delete(String id){
        int deleteRows =0;
        try{
            deleteRows =  super.delete("Todo", "Id = '" + id + "'", null);
        }catch (Exception ex)
        {

        }

        return deleteRows;
    }
}
