package com.Jungeun.wjdwjd95.emotional_trashcan.GoalPlanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Jungeun.wjdwjd95.emotional_trashcan.Constants;
import com.Jungeun.wjdwjd95.emotional_trashcan.R;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class GoalGridAdapter extends BaseAdapter {
    private List<GoalSetClass> list;
    private LayoutInflater inflater;
    Context context;
    int count = 0;
    View tmpView;
    public Calendar mCal;
    GoalPresenter mPresenter;
    String Startdate;
    String Enddate;
    public GoalGridAdapter(Context _context, List<GoalSetClass> value) {
        context = _context;

        list = value;
        mPresenter = new GoalPresenter(_context, null);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        GoalEntity goalEntity = mPresenter.IsGoal_getentity();
        if(goalEntity != null)
        {
            Startdate = goalEntity.getStartDate();
            Enddate = goalEntity.getEndDate();
        }else
        {
            Startdate = "";
            Enddate = "";
        }




    }

    public void ChangeImage(int position, String Id)
    {
        if(!mPresenter.IsGoal())
        {
            Toast.makeText(context,context.getString(R.string.multi_GoalPlanner_enter_goal_error), Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        String getdate = list.get(position).getYear() +"-" + list.get(position).getMonth() +"-"+ list.get(position).getDay();
        String Icon = "";
        if(mPresenter.selectDate(Id, getdate) > 0)
        {
            mPresenter.deleteIcon(getdate, Id);
            Icon = "nothing";
        }else if(mPresenter.getGoal_ifexistatdate(getdate) == null)
        {
            Toast.makeText(context,context.getString(R.string.multi_GoalPlanner_enter_out_of_date), Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {


            GoalDetailEntity entity = new GoalDetailEntity();
            entity.setDate(getdate);
            entity.setID(Id);
            if(mPresenter.insertdetail(entity))
            {

            }else
            {

            }

            Icon = prefs.getString(Constants.SHARED_PREF_KEY_CURRENT_GOAL_IMG, "nothing");
        }
        int id = context.getResources().getIdentifier(Icon, "drawable", context.getPackageName());
        Drawable drawable = context.getResources().getDrawable(id);
        list.get(position).getImageView().setImageDrawable(drawable);

    }
    @Override
    public int getCount()
    {
        return list.size();
    }
    @Override
    public GoalSetClass getItem(int position)
    {
        return list.get(position);
    }
    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View converView, ViewGroup parent) {
        //ViewHolder holder = null;
        LinearLayout test;
        LinearLayout.LayoutParams lp = null;
        final String Date = getItem(position).getYear() + "-" + getItem(position).getMonth() + "-" + getItem(position).getDay();
        if(count > 0 && position == 0)
        {
            return tmpView;
        }
        if(converView != null)
        {
            test = (LinearLayout)converView.getTag();
            test.removeAllViews();
            test.removeAllViewsInLayout();

        }else {
            if (getItem(position).getDay().equals("")) {
                lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200
                );
                lp.gravity = Gravity.CENTER;
                converView = inflater.inflate(R.layout.calender_item, parent, false);
                test = (LinearLayout) converView.findViewById(R.id.calender_item_layout);
                test.setOrientation(LinearLayout.VERTICAL);
                test.removeAllViews();
                test.removeAllViewsInLayout();
                test.setLayoutParams(lp);
                test.setBackgroundColor(Color.TRANSPARENT);

                converView.setTag(test);
                return converView;
            }

            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
            converView = inflater.inflate(R.layout.calender_item, parent, false);
            test = (LinearLayout) converView.findViewById(R.id.calender_item_layout);
            test.setOrientation(LinearLayout.VERTICAL);
            test.removeAllViews();
            test.removeAllViewsInLayout();
            lp.gravity = Gravity.CENTER;
            test.setLayoutParams(lp);
            test.setBackgroundColor(Color.WHITE);
        }

        //textView.setGravity(Gravity.CENTER);

        if(getItem(position).getImageView().getParent() != null)
        {
            ((ViewGroup)getItem(position).getImageView().getParent()).removeView(getItem(position).getImageView());
        }

        ImageView imageView = getItem(position).getImageView();
        imageView.setTag(getItem(position).getImageView().getTag());
        lp = new LinearLayout.LayoutParams(100,0);
        lp.weight = 8;
        lp.gravity = Gravity.CENTER;
        lp.setMargins(20,10,20,20);
        imageView.setLayoutParams(lp);

        LinearLayout TextLayout = new LinearLayout(context);
        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        lp.weight = 4;
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        TextLayout.setLayoutParams(lp);
        TextLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(context);
        lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.weight = 2;
        lp.gravity = Gravity.START;
        textView.setLayoutParams(lp);
        textView.setTextSize(11);
        textView.setText("" + getItem(position).getDay());


        TextLayout.addView(textView);
        mCal = Calendar.getInstance();
        Integer year = mCal.get(Calendar.YEAR);
        Integer today = mCal.get(Calendar.DAY_OF_MONTH);
        Integer month = mCal.get(Calendar.MONTH);
        String sToday = "";
        if(today < 10)
        {
            sToday = "0" + String.valueOf(today);
        }else
        {
            sToday = String.valueOf(today);
        }

        String sMonth = "";
        if(month+1 <10 )
        {
            sMonth = "0" + (month+1);
        }else
        {
            sMonth = String.valueOf(month+1);
        }

        if (sToday.equals(getItem(position).getDay()) && sMonth.equals(getItem(position).getMonth())) {
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setTypeface(null, Typeface.BOLD);
            test.setBackgroundResource(R.color.mybasecolor);
            TextLayout.setBackgroundResource(R.color.mybasecolor);
        } else if (position % 7 == 0) {
            textView.setTextColor(Color.parseColor("#ff0000"));
        } else if (position % 7 == 6)
        {
            textView.setTextColor(Color.parseColor("#ff0000"));
        }
        if(Date.equals(Enddate))
        {
            textView.setText("" + getItem(position).getDay() + " D-Day");
            textView.setTextColor(Color.RED);
            textView.setTypeface(null, Typeface.BOLD);

        }else if(Date.equals(Startdate))
        {
            textView.setText("" + getItem(position).getDay() + " Start!");
            textView.setTextColor(Color.BLACK);
            textView.setTypeface(null, Typeface.BOLD);

        }
        test.addView(TextLayout);
        test.addView(imageView);
        if(position == 0)
        {
            count++;
            tmpView = converView;
        }
        converView.setTag(test);
        return converView;

    }

}
