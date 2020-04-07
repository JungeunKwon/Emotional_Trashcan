package com.Jungeun.wjdwjd95.emotional_trashcan.MoodChart;

import android.content.Context;
import android.content.Intent;
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


import com.Jungeun.wjdwjd95.emotional_trashcan.Diary.DiaryActivity;
import com.Jungeun.wjdwjd95.emotional_trashcan.R;

import java.util.Calendar;
import java.util.List;

public class GridAdapter extends BaseAdapter {
    private  List<MoodSetClass> list;
    private  LayoutInflater inflater;
    Context context;
    int count = 0;
    View tmpView;
    public Calendar mCal;
    MoodPresenter mPresenter;

    public GridAdapter(Context _context, List<MoodSetClass> value)
    {
        context = _context;

        list = value;
        mPresenter = new MoodPresenter(_context);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }
    public void ChangeImage(int position, Drawable drawable, String result)
    {
        String getdate = list.get(position).getYear() +"-" + list.get(position).getMonth() +"-"+ list.get(position).getDay();
        if(mPresenter.selectDate(getdate) > 0)
        {
            if(mPresenter.upDateEmoji(getdate,result)<0)
            {
                Toast.makeText(context,context.getString(R.string.multi_alert_insert_error), Toast.LENGTH_SHORT).show();
            }
            //update
        }else
        {
            if(mPresenter.InsertEmoji(getdate,result)<0)
            {
                Toast.makeText(context,context.getString(R.string.multi_alert_insert_error), Toast.LENGTH_SHORT).show();
            }
        }

        list.get(position).getImageView().setImageDrawable(drawable);
    }
    @Override
    public int getCount()
    {
        return list.size();
    }
    @Override
    public MoodSetClass getItem(int position)
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
        lp = new LinearLayout.LayoutParams(100,100);
        lp.gravity = Gravity.CENTER;
        lp.setMargins(20,10,20,20);
        imageView.setLayoutParams(lp);

        LinearLayout TextLayout = new LinearLayout(context);
        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        lp.weight = 1;
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        TextLayout.setLayoutParams(lp);
        TextLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(context);
        lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight = 2;
        lp.gravity = Gravity.START;
        textView.setLayoutParams(lp);
        textView.setTextSize(15);
        textView.setText("" + getItem(position).getDay());

        ImageView TextImageView = new ImageView(context);
        lp = new LinearLayout.LayoutParams(40, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(10,10,10,10);
        lp.gravity = Gravity.END;
        TextImageView.setLayoutParams(lp);
        if(mPresenter.IsDiaryatDate(Date) >0)
        {
            TextImageView.setImageResource(R.drawable.edit);
        }
        TextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DiaryActivity.class);
                intent.putExtra("Date", Date);
                context.startActivity(intent);
            }
        });
        TextLayout.addView(textView);
        TextLayout.addView(TextImageView);
        mCal = Calendar.getInstance();
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
            textView.setTextColor(Color.parseColor("#0000ff"));
        }

        test.addView(TextLayout);
        test.addView(imageView);
        if(position == 0)
        {
            count++;
            tmpView = converView;
        }
        //  holder = new ViewHolder();

        // holder.txtItemGridView = (TextView) converView.findViewById(R.id.calender_item_txt);
        //  holder.imageItemGridView = (ImageView) converView.findViewById(R.id.calender_item_image);
        converView.setTag(test);
        return converView;
      /*  } else {
            LinearLayout.LayoutParams lp = null;
            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
            test = (LinearLayout)converView.getTag();
            test.setBackgroundColor(Color.WHITE);
            test.setLayoutParams(lp);
            TextView textView = new TextView(context);
            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,50);
            textView.setLayoutParams(lp);
            textView.setTextSize(15);
            textView.setGravity(Gravity.CENTER);
            if(getItem(position).getImageView().getParent() != null)
            {
                ((ViewGroup)getItem(position).getImageView().getParent()).removeView(getItem(position).getImageView());
            }
            ImageView imageView = getItem(position).getImageView();

            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,120);
            lp.gravity = Gravity.CENTER;
            lp.setMargins(0,10,0,0);
            imageView.setLayoutParams(lp);
            mCal = Calendar.getInstance();
            textView.setText("" + getItem(position).getDay());

            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            Integer month = mCal.get(Calendar.MONTH);
            String sToday = String.valueOf(today);
            String sMonth = "";
            if(month+1 <10 )
            {
                sMonth = "0" + (month+1);
            }else
            {
                sMonth = String.valueOf(month+1);
            }
            if (sToday.equals(getItem(position).getDay()) && sMonth.equals(getItem(position).getMonth())) {
                textView.setTextColor(Color.parseColor("#ffd9d1"));
            } else if (position % 7 == 0) {
                textView.setTextColor(Color.parseColor("#ff0000"));
            } else if (position % 7 == 6)
            {
                textView.setTextColor(Color.parseColor("#0000ff"));
            }
            test.removeAllViewsInLayout();
            test.addView(textView);
            test.addView(imageView);

            //  holder = new ViewHolder();

            // holder.txtItemGridView = (TextView) converView.findViewById(R.id.calender_item_txt);
            //  holder.imageItemGridView = (ImageView) converView.findViewById(R.id.calender_item_image);
            converView.setTag(test);
            return converView;
        }*/
        /*
        holder.txtItemGridView.setText("" + getItem(position).getDay());
        holder.imageItemGridView = getItem(position).getImageView();


        Integer today = mCal.get(Calendar.DAY_OF_MONTH);
        Integer month = mCal.get(Calendar.MONTH);
        String sToday = String.valueOf(today);
        String sMonth = "";
        if(month+1 <10 )
        {
            sMonth = "0" + (month+1);
        }else
        {
            sMonth = String.valueOf(month+1);
        }
        if (sToday.equals(getItem(position).getDay()) && sMonth.equals(getItem(position).getMonth())) {
            holder.txtItemGridView.setTextColor(Color.parseColor("#ffd9d1"));
        } else if (position % 7 == 0) {
            holder.txtItemGridView.setTextColor(Color.parseColor("#ff0000"));
        } else if (position % 7 == 6)
        {
            holder.txtItemGridView.setTextColor(Color.parseColor("#0000ff"));
        }*/

    }

}
