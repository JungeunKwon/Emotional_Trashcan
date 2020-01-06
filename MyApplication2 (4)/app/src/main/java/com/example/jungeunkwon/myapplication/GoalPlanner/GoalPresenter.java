package com.example.jungeunkwon.myapplication.GoalPlanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.jungeunkwon.myapplication.Constants;
import com.example.jungeunkwon.myapplication.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoalPresenter {
    Context context;
    GoalView mView;
    GoalDAO goalDAO = new GoalDAO();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public GoalPresenter(Context _context, GoalView view)
    {
        mView = view;
        context = _context;
    }
    GoalEntity IsGoal_getentity()
    {
        List<GoalEntity> listdate = goalDAO.IsGoal();
        if(listdate != null && listdate.size() > 0)
        {
            return listdate.get(0);
        }else
            return null;
    }
    public void update(String status)
    {
        List<GoalEntity> listdate = goalDAO.IsGoal();
        if(listdate != null && listdate.size() > 0)
        {
            goalDAO.update(listdate.get(0).getID(), status);
        }else
        {

        }

    }
    public void RemoveGoal(String ID)
    {
        try{
            goalDAO.deletedetail(ID);
            goalDAO.delete(ID);
        }catch (Exception ex)
        {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    public GoalEntity getGoal_ifexistatdate(String date)
    {
        List<GoalEntity> listdate = goalDAO.IsGoalbydate(date);
        if(listdate != null && listdate.size() > 0)
        {
            return listdate.get(0);
        }else
            return null;
    }
    public List<GoalEntity> gettotalgoal()
    {     List<GoalEntity> listdate = new ArrayList<>();
        try{
            listdate = goalDAO.gettotalGoal();
        }catch (Exception ex)
        {
          listdate = null;
        }
        return listdate;
    }

    public int selectDate(String date)
    {
        int result = 0;
        try{
            result = goalDAO.SelectDate(date);
        }catch (Exception ex)
        {
            result = -1;
        }
        return result;
    }
    public int selectDate(String Id, String date)
    {
        int result = 0;
        try{
            result = goalDAO.SelectDate(Id,date);
        }catch (Exception ex)
        {
            result = -1;
        }
        return result;
    }
    public String getGoalrate(String ID)
    {
        String result = "";
        List<GoalCountListEntity> detaillist = new ArrayList<>();
        List<GoalEntity> list= new ArrayList<>();
        int diff=  0;
        int count = 0;
        double ratio = 0.0;
        try {
            detaillist = goalDAO.SelectCount(ID);
            if(detaillist == null || detaillist.size() <1)
            {
                count = 0;
            }else
            {
                count = detaillist.get(0).getCOUNT();
            }
            list = goalDAO.getGoalbyID(ID);
            if(list != null && list.size() >0) {
                Date startdate = dateFormat.parse(list.get(0).getStartDate());
                Date enddate = dateFormat.parse(list.get(0).getEndDate());
                diff = ((int)enddate.getTime()/(24*60*60*1000)) - ((int)startdate.getTime()/(24*60*60*1000)) + 2;
                ratio = ((double)count/(double)diff) * 100;
                result =  String.format("%.1f", ratio) + "%";
            }
        }catch (Exception ex)
        {
            result = "";
        }
        return result;
    }

    public String getIcon(String date)
    {
        String result = "";
        try {
            result = goalDAO.SelectDateIcon(date);
        }catch (Exception ex)
        {
            result = "";
        }return result;
    }
    public boolean insert(GoalEntity entity) {
        boolean result = false;
        try {


            Date now = new Date();
            String id = Constants.iddateFormat.format(now);
            entity.setID(id);
            SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Constants.SHARED_PREF_KEY_CURRENT_ID, id);
            editor.apply();
            goalDAO.insert(entity);
            result = true;
        }catch (Exception ex)
        {
            result = false;
            Toast.makeText(context, context.getString(R.string.multi_alert_insert_error), Toast.LENGTH_SHORT).show();
        }return result;
    }
    public void deleteIcon(String date, String id)
    {
        goalDAO.deletedetailbydate(date, id);
    }
    public boolean insertdetail(GoalDetailEntity entity) {
        boolean result = false;
        try {

            goalDAO.insertdetail(entity);
            result = true;
        }catch (Exception ex)
        {
            result = false;
            Toast.makeText(context, context.getString(R.string.multi_alert_insert_error), Toast.LENGTH_SHORT).show();
        }return result;
    }
    public boolean IsGoal()
    {
        List<GoalEntity> listdate = goalDAO.IsGoal();
        if(listdate != null && listdate.size() > 0)
        {
            return true;
        }else
            return false;
    }
}

