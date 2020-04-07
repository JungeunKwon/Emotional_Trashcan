package com.Jungeun.wjdwjd95.emotional_trashcan.Todo;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Jungeun.wjdwjd95.emotional_trashcan.Constants;
import com.Jungeun.wjdwjd95.emotional_trashcan.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Todopersenter {
    Context context;
    private TodoView mView;
    private TodoDao todoDao = new TodoDao();
    public Todopersenter(Context _context, TodoView view)
    {
        context = _context;
        mView = view;
    }
    public boolean insert(String Content)
    {
        boolean result = false;
        TodoEntity todoEntity = new TodoEntity();
        Date date = new Date();

        todoEntity.setId(Constants.iddateFormat.format(date));
        todoEntity.setChecked(false);
        todoEntity.setContent(Content);
        todoEntity.setEndDate("");
        if(todoDao.insert(todoEntity) < 0 )
        {
            result = false;
        }else
            result = true;
        return result;
    }
    public void update(String id, String date)
    {
        todoDao.update(id,true,date);
    }
    public boolean isList()
    {
        boolean result = false;
        List<TodoEntity> list = todoDao.selectuncheckedTodo();
        if(list != null && list.size() > 0)
        {
            result = true;
        }else
            result = false;
        return result;
    }
    public  List<TodoEntity> getTodoEntityList()
    {
        List<TodoEntity> list = new ArrayList<>();
        try{

            list = todoDao.selectTodo();
            if(list != null && list.size() > 0)
            {
                return list;
            }
            else
                return null;
        }catch (Exception ex)
        {
            return null;
        }
    }


    public void getList()
    {
        mView.clear();
        List<TodoEntity> list = todoDao.selectTodo();
        if(list != null && list.size() > 0)
        {
            for(int i = 0 ; i<list.size(); i++)
            {
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER_VERTICAL;
                linearLayout.setLayoutParams(lp);
                linearLayout.setGravity(Gravity.CENTER);
                lp = new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.weight = 1;
                lp.setMargins(0,0,10,0);
                ImageButton heartButton = new ImageButton(context);
                heartButton.setLayoutParams(lp);
                heartButton.setBackgroundColor(Color.TRANSPARENT);
                heartButton.setTag(list.get(i).getId());
                heartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<TodoEntity> tmplist = todoDao.selectid(v.getTag().toString());
                        if(tmplist != null && tmplist.size() > 0) {
                            if (tmplist.get(0).getChecked()) {
                                todoDao.update(tmplist.get(0).getId(), false, "");
                                getList();
                            } else {
                                Date date = new Date();
                                todoDao.update(tmplist.get(0).getId(), true, Constants.dateFormat.format(date));
                                getList();
                            }
                        }
                    }
                });
                ImageButton exitButton = new ImageButton(context);
                exitButton.setLayoutParams(lp);
                exitButton.setImageResource(R.drawable.close);
                exitButton.setBackgroundColor(Color.TRANSPARENT);
                exitButton.setTag(list.get(i).getId());
                exitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        todoDao.delete(v.getTag().toString());
                        getList();
                    }
                });
                TextView textcontent = new TextView(context);
                textcontent.setGravity(Gravity.CENTER_VERTICAL);
                textcontent.setText(list.get(i).getContent());
                lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.weight = 6;
                textcontent.setLayoutParams(lp);
                TextView textdate = new TextView(context);
                textdate.setText(list.get(i).getEndDate());
                lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.weight = 3;
                textdate.setLayoutParams(lp);
                textdate.setTag(list.get(i).getId());
                textdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mView.chagedate((TextView)view);
                    }
                });
                if(list.get(i).getChecked())
                {
                    heartButton.setImageResource(R.drawable.heart2png);
                    linearLayout.addView(heartButton);
                    linearLayout.addView(textcontent);
                    linearLayout.addView(textdate);
                    linearLayout.addView(exitButton);
                    mView.addcheckedview(linearLayout);

                }else
                {
                    heartButton.setImageResource(R.drawable.hearts);
                    linearLayout.addView(heartButton);
                    linearLayout.addView(textcontent);
                    linearLayout.addView(textdate);
                    linearLayout.addView(exitButton);
                    mView.adduncheckedview(linearLayout);

                }

            }

        }mView.updatepin();
    }
}
