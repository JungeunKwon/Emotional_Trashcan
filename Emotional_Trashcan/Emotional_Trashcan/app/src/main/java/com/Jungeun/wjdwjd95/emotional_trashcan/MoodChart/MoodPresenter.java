package com.Jungeun.wjdwjd95.emotional_trashcan.MoodChart;

import android.content.Context;


import com.Jungeun.wjdwjd95.emotional_trashcan.Diary.DiaryDAO;

import java.util.ArrayList;
import java.util.List;

public class MoodPresenter {

    Context context;
    MoodDAO moodDAO = new MoodDAO();
    DiaryDAO diaryDAO = new DiaryDAO();
    public MoodPresenter(Context _context)
    {
        context = _context;
    }
    public int selectDate(String date)
    {
        int result = 0;
        try{
            result = moodDAO.SelectDate(date);
        }catch (Exception ex)
        {
            result = -1;
        }
        return result;
    }
    public String selectEmoji(String date)
    {
        String result = "";
        List<MoodEntity> data = new ArrayList<MoodEntity>();
        try{
            data = moodDAO.SelectEmoji(date);
            if(data.size()>0)
            {
                result = data.get(0).getEmoji();
            }
        }catch (Exception ex)
        {
            result = "";
        }
        return result;
    }
    public int SelectAllCount(String date)
    {
        List<MoodListEntity> data = new ArrayList<MoodListEntity>();
        int count  = 0;
        try{
            count = moodDAO.SelectAllCount(date);
            if(count>0)
            {
                return count;
            }
        }catch (Exception ex)
        {
            return count;
        }
        return count;
    }
    public List<MoodListEntity> SelectCount(String date)
    {

        List<MoodListEntity> data = new ArrayList<MoodListEntity>();
        try{
            data = moodDAO.SelectCount(date);
            if(data.size()>0)
            {
                return data;
            }
        }catch (Exception ex)
        {

            return null;
        }
        return null;
    }
    public int IsDiaryatDate(String date)
    {
        int result = 0;
        try{
            result = diaryDAO.SelectDiarysize(date);
        }catch (Exception ex)
        {
            result = -1;
        }
        return result;
    }
    public int upDateEmoji(String Date, String Emoji)
    {
        int result = 0;
        try{
            result = moodDAO.update(Date, Emoji);
        }catch (Exception ex)
        {
            result = -1;
        }
        return result;
    }
    public int InsertEmoji(String Date, String Emoji)
    {
        int result = 0;
        try{
            MoodEntity moodEntity = new MoodEntity();
            moodEntity.setDate(Date);
            moodEntity.setEmoji(Emoji);
            result = moodDAO.insert(moodEntity);
        }catch (Exception ex)
        {
            result = -1;
        }
        return result;
    }
}

