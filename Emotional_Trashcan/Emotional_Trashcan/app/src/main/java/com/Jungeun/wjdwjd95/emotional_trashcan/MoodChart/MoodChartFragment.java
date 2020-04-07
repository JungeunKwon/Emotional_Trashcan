package com.Jungeun.wjdwjd95.emotional_trashcan.MoodChart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Jungeun.wjdwjd95.emotional_trashcan.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoodChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoodChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters


    private OnFragmentInteractionListener mListener;

    public MoodChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment MoodChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoodChartFragment newInstance() {
        MoodChartFragment fragment = new MoodChartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
    public interface SetDate {
        public void setdate(String _date);
    }
    private SetDate SendDate;
    GridView gridView;
    TextView txtmain;
    GridAdapter gridAdapter;
    ImageButton btnleft;
    ImageButton btnright;
    Calendar mCal;
    Date date;
    int dayNum = 0;
    ArrayList<MoodSetClass> dayList;
    long now;
    final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.US);
    final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.US);
    final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd");
    MoodPresenter mPresenter;
    final int REQUEST_EMOJI = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_mood_chart, container, false);
        // Inflate the layout for this fragment
        mPresenter = new MoodPresenter(getActivity());
        txtmain = (TextView)view.findViewById(R.id.activity_mood_maintxt);
        gridView = (GridView)view.findViewById(R.id.activity_mood_chart_calender);
        btnleft = (ImageButton)view.findViewById(R.id.activity_mood_btnleft);
        btnright = (ImageButton)view.findViewById(R.id.activity_mood_btnright);
        SendDate = (SetDate)getActivity();
        now = System.currentTimeMillis();
        date = new Date(now);
        getCalendar(date);
        btnleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    date.setDate(1);
                    date.setMonth(date.getMonth() -1);
                    getCalendar(date);
                }catch (Exception ex)
                {
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    date.setDate(1);
                    date.setMonth(date.getMonth() +1);
                    getCalendar(date);
                }catch (Exception ex)
                {
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    public void getCalendar(Date pDate)
    {

        txtmain.setText(curYearFormat.format(pDate) + "-" + curMonthFormat.format(pDate));
        SendDate.setdate(curYearFormat.format(pDate) + "-" + curMonthFormat.format(pDate));

        dayList = new ArrayList<MoodSetClass>();
        mCal = Calendar.getInstance();

        mCal.set(Integer.parseInt(curYearFormat.format(pDate)),Integer.parseInt(curMonthFormat.format(pDate))-1,1);
        dayNum = mCal.get(Calendar.DAY_OF_WEEK) -1;
        if(dayNum != 0) {
            for (int i = 0; i < dayNum; i++) {
                MoodSetClass MoodSetClass = new MoodSetClass();
                MoodSetClass.setDay("");
                MoodSetClass.setMonth(curMonthFormat.format(pDate));
                MoodSetClass.setYear(curYearFormat.format(pDate));
                ImageView imageView = new ImageView(getActivity());

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100,100);
                lp.weight = 2;
                lp.gravity = Gravity.CENTER;
                lp.setMargins(20,10,20,20);
                imageView.setLayoutParams(lp);
                MoodSetClass.setImageView(imageView);
                dayList.add(MoodSetClass);
            }
        }
        setCalendarDate(mCal.get(Calendar.MONTH) + 1);
        gridAdapter = new GridAdapter(getActivity(), dayList);
        gridView.setAdapter(gridAdapter);
        dayNum = 0;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_EMOJI:
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("result");
                    int id = getResources().getIdentifier(result, "drawable", getActivity().getPackageName());
                    Drawable drawable = getResources().getDrawable(id);
                    String Tag = data.getStringExtra("Tag");
                    gridAdapter.ChangeImage(Integer.parseInt(Tag),drawable,result);
                    //gridAdapter.notifyDataSetChanged();


                    //gridView.setAdapter(gridAdapter);
                    //imageView.setImageDrawable(drawable);
                    // calendarView.set
                }
                break;

        }

    }
    /*

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(MoodChartActivity.this, MoodSelectAcitivity.class );
                startActivityForResult(intent, REQUEST_EMOJI);
                Toast.makeText(MoodChartActivity.this,Integer.toString(year)+Integer.toString(month)+Integer.toString(dayOfMonth), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_EMOJI:
                if (resultCode == RESULT_OK) {

                   String result = data.getStringExtra("result");
                    int id = getResources().getIdentifier(result, "drawable", getPackageName());
                    Drawable drawable = getResources().getDrawable(id);
                   // calendarView.set
                    calendarView.setSelectedDateVerticalBar(drawable);
                }
                break;

        }

    }

*/
    private void setCalendarDate(final int Month)
    {
        mCal.set(Calendar.MONTH, Month-1);
        for(int i = 0; i<mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++)
        {
            MoodSetClass MoodSetClass = new MoodSetClass();
            MoodSetClass.setMonth(curMonthFormat.format(date));
            MoodSetClass.setYear(curYearFormat.format(date));
            String tmpday = "";
            if((i+1) < 10)
            {
                tmpday = "0" + (i+1);
            }else
            {
                tmpday = Integer.toString((i+1));
            }
            String getdate = curYearFormat.format(date)+"-"+ curMonthFormat.format(date) +"-"+ tmpday;
            MoodSetClass.setDay("" + tmpday);
            final ImageView imageView = new ImageView(getActivity());
            LinearLayout.LayoutParams lp = null;
            lp = new LinearLayout.LayoutParams(100,100);
            lp.weight = 2;
            lp.gravity = Gravity.CENTER;
            lp.setMargins(20,10,20,20);
            if(mPresenter.selectDate(getdate) > 0)
            {
                String result = mPresenter.selectEmoji(getdate);
                int id = getResources().getIdentifier(result, "drawable", getActivity().getPackageName());
                Drawable drawable = getResources().getDrawable(id);
                imageView.setImageDrawable(drawable);
            }
            imageView.setLayoutParams(lp);
            imageView.setTag(i + dayNum);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MoodSelectAcitivity.class);
                    intent.putExtra("Tag", imageView.getTag().toString());
                    startActivityForResult(intent, REQUEST_EMOJI);
                }
            });
            MoodSetClass.setImageView(imageView);
            dayList.add(MoodSetClass);
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Deprecated
    public void onAttach( Activity activity ) {
        super.onAttach( activity );
        SendDate = (SetDate)activity;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        SendDate = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
