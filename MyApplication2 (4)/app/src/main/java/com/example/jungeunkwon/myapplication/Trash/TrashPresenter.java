package com.example.jungeunkwon.myapplication.Trash;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jungeunkwon.myapplication.Constants;
import com.example.jungeunkwon.myapplication.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TrashPresenter {
    Context context;
    TrashView mView;
    TrashDAO trashDAO = new TrashDAO();


    public TrashPresenter(Context _context, TrashView view) {
        mView = view;
        context = _context;

    }
    public void insertTrashCommentFirst()
    {
        Resources resources = context.getResources();
        String[] comments = resources.getStringArray(R.array.multi_thorw_comment);
        for(int i =0; i <comments.length;i++)
        {
            TrashCommentEntity trashcommentEntity = new TrashCommentEntity();
            trashcommentEntity.setComment(comments[i]);
            trashDAO.insertTrashComment(trashcommentEntity);
        }
        //insert

    }
    public boolean insertTrashComment(TrashCommentEntity entity)
    {
        boolean result = false;
        try
        {
            List<TrashCommentEntity> listdate = trashDAO.selectAllComment();
            if(listdate != null && listdate.size() > 0)
            {
                listdate.add(entity);

                trashDAO.refreshTrashComment();
            }else
            {
                listdate = new ArrayList<>();
                listdate.add(entity);
            }
            for(int i = 0 ; i < listdate.size(); i ++)
            {
                trashDAO.insertTrashComment(listdate.get(i));
            }

            result = true;
        }catch (Exception ex)
        {
            result = false;
        }
        return result;
    }
    public boolean insertTrash(TrashEntity entity) {
        boolean result = false;
        try {
            Date now = new Date();
            String id = Constants.iddateFormat.format(now);
            entity.setID(id);
            int maxcount = trashDAO.selectMaxCount();
            int mincount = trashDAO.selectlowestnum();
            Random random = new Random();
            int randomNum;
            List<Integer>randomarray = new ArrayList<>();
            int i = 0;
            while(true)
            {    randomNum = random.nextInt(maxcount - mincount + 1) + mincount;
                if(trashDAO.IsUsingSeq(Integer.toString(randomNum)))
                {
                    i++;
                    if(i > trashDAO.getCommentCount())
                    {
                        break;
                    }
                    continue;
                }
                randomarray.add(randomNum);
                if(trashDAO.selectIsExist(Integer.toString(randomNum)))
                {
                    break;
                }
            }
            entity.setCommentSeq(randomNum);
            trashDAO.insertTrash(entity);
            result = true;
        } catch (Exception ex) {
            result = false;
            Toast.makeText(context, context.getString(R.string.multi_alert_insert_error), Toast.LENGTH_SHORT).show();
        }
        return result;
    }
    public void removepasseddat(String Today)
    {
        trashDAO.deletepasseddate(Today);
    }
    public List<TrashCommentEntity> getCommentList()
    {
        List<TrashCommentEntity> listdate = new ArrayList<>();
        try {
            listdate = trashDAO.selectAllComment();
        }catch (Exception ex)
        {

        }
        return listdate;
    }
    public void deletecomment(String Seq)
    {
        trashDAO.deleteComment(Seq);
    }
    public boolean IsUsing(String Seq)
    {
        boolean result = false;
        try
        {
            List<TrashEntity> list = trashDAO.selectIsUsingSeq(Seq);
             if(list != null && list.size() > 0)
             {
                 result = true;
             }else
             { result = false;}
        }catch (Exception ex)
        {
            result = false;
        }
        return  result;
    }
    public void getList(String Today)
    {
        mView.clear();
        final String today = Today;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        List<TrashEntity> list = new ArrayList<>();
        list = trashDAO.selectTodayTrash(today);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String usertext = list.get(i).getContent();
                int seq = list.get(i).getCommentSeq();
                String comment = "";
                List<TrashCommentEntity> commentdata = new ArrayList<>();
                commentdata = trashDAO.selsectThrowcommentbySeq(seq);
                if(commentdata != null && commentdata.size()>0)
                {
                    comment = commentdata.get(0).getComment();
                }
                LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.throwtemplate, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(30,10,30,10);
                layout.setLayoutParams(lp);
                TextView userT = (TextView)layout.findViewById(R.id.activity_throw_user_text);
                TextView commT = (TextView)layout.findViewById(R.id.activity_throw_comment_text);
                Button exitB = (Button)layout.findViewById(R.id.activity_throw_btnexit);
                exitB.setTag(list.get(i).getTitle());
                exitB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        trashDAO.delete(v.getTag().toString());
                        getList(today);
                    }
                });
                userT.setText(usertext);
                commT.setText(comment);
                mView.addView(layout);

            }

        }
    }
}