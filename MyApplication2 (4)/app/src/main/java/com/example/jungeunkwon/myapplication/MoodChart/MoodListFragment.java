package com.example.jungeunkwon.myapplication.MoodChart;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.jungeunkwon.myapplication.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoodListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoodListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private String mParam1;


    private OnFragmentInteractionListener mListener;

    public MoodListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MoodListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoodListFragment newInstance(String param1) {
        MoodListFragment fragment = new MoodListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    static String Date = "";
    View view;
    MoodPresenter mPresenter;
    List<MoodListEntity> Moodlist;
    PieChart pieChart;
    ArrayList<PieEntry> yvlues ;
   // ArrayList<String> xVals ;
    PieDataSet dataset;
    PieData data;
    TableLayout tableHeader;
    TableLayout tableLayout;
    ScrollView scrollView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            if (isVisibleToUser && view != null) {
                onResume();
            }

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mood_list, container, false);
        Bundle extra = getArguments();
        mPresenter = new MoodPresenter(getActivity());
        pieChart = (PieChart)view.findViewById(R.id.fragment_mood_piechart);
        pieChart.removeAllViews();
        pieChart.setUsePercentValues(true);
       // pieChart.setRotationEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setHoleRadius(30f);
        Moodlist = new ArrayList<MoodListEntity>();
        tableHeader = (TableLayout)view.findViewById(R.id.fragment_mood_list_tableColumn);
        tableLayout = (TableLayout)view.findViewById(R.id.fragment_mood_list_tablelayout);
        scrollView = (ScrollView)view.findViewById(R.id.fragment_mood_list_scrollview);
        scrollView.setBackgroundColor(Color.WHITE);
        SetDataGrid();
        if(extra != null)
        {
            Date = extra.getString("Date");
            if(Date != null)
            {
                pieChart.removeAllViewsInLayout();
                tableLayout.removeAllViewsInLayout();
                tableHeader.removeAllViewsInLayout();
                ExecuteCount(Date);
            }else
            {
                pieChart.removeAllViewsInLayout();
                tableLayout.removeAllViewsInLayout();
                tableHeader.removeAllViewsInLayout();
            }
        }


        // Inflate the layout for this fragment
        return view;
    }
    private void SetDataGrid()
    {
        TableRow tableHeaderow = new TableRow(getActivity());
        tableHeaderow.addView(AddColumn(getString(R.string.multi_Moodchart_tablehaader_mood),0));
        tableHeaderow.addView(AddColumn(getString(R.string.multi_Moodchart_tablehaader_percent),1));
        if(tableHeader.getChildCount() == 0)
        {
            tableHeader.addView(tableHeaderow);
        }
    }
    private TextView AddColumn(String columName, int gravity)
    {
        TextView view = new TextView(getActivity());
        view.setText(columName);
        view.setBackgroundColor(Color.parseColor("#FADEE1"));
        view.setTextColor(Color.BLACK);
        if(gravity == 0)
        {
            view.setGravity(Gravity.CENTER);
            view.setWidth(150);
        }else
        {
            view.setGravity(Gravity.START);
            view.setWidth(260);
        }
        view.setTextSize(17);


        return view;
    }
    public void AddRow(Drawable emoji, String ratio) {
        if (tableLayout.getChildCount() == 0)
            tableLayout.removeAllViewsInLayout();
        TableRow tableRow = new TableRow(getActivity());
        try {

            tableRow.addView(AddColumEmoji(emoji));
            tableRow.addView(AddColumnData(ratio, 250));
            tableRow.setBackgroundResource(R.drawable.layout_corner_frame);
            tableRow.setPadding(5,5,5,5);
            tableLayout.addView(tableRow);


        } catch (Exception ex) {

        }
    }
    public ImageView AddColumEmoji (Drawable drawable)
    {
        ImageView colData = new ImageView(getActivity());
        colData.setImageDrawable(drawable);
        colData.setPadding(15, 15, 15, 15);

        return colData;

    }
    public TextView AddColumnData(String data, int width) {
        TextView colData = new TextView(getActivity());
        colData.setText(data);
        colData.setTextColor(Color.BLACK);
        colData.setTextSize(15);
        colData.setPadding(15, 15, 15, 15);
        colData.setWidth(width);
        return colData;
    }
    private Drawable resize(Drawable image, int size) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, size, size, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }
    public void ExecuteCount(String pDate)
    {
        int Total = mPresenter.SelectAllCount(pDate);
        yvlues = new ArrayList<PieEntry>();
       // xVals = new ArrayList<String>();
        if(Total > 0)
        {
            Moodlist = mPresenter.SelectCount(pDate);

           // layout.removeAllViewsInLayout();
            if(Moodlist != null && Moodlist.size() > 0) {
                tableLayout.removeAllViewsInLayout();
                for (int i = 0; i < Moodlist.size(); i++) {
                    String result = Moodlist.get(i).getEmoji();
                    int id = getResources().getIdentifier(result, "drawable", getActivity().getPackageName());
                    Drawable drawable = getResources().getDrawable(id);
                    drawable = resize(drawable,100);
                    double d1, d2, number;
                    d1 = (double) Moodlist.get(i).getCOUNT();
                    d2 = (double) Total;
                    number = d1 / d2 * 100;
                    String num = String.format("%.1f", number) + "%";
                    if(i <5 ) {
                        yvlues.add(new PieEntry((float) number, "\n\n\n\n\n\n\n\n\n\n             "+num ,drawable));
                    }
                    drawable = resize(drawable,60);
                    AddRow(drawable, num);
                }
                dataset = new PieDataSet(yvlues, "");

                //dataset.setDrawValues(false);
                int [] tmpcolor = new int[5];
                pieChart.getLegend().setEnabled(false);
                //dataset.setColors(ColorTemplate.PASTEL_COLORS);
                int[] color = {
                    Color.rgb(124, 186, 201), Color.rgb(242, 223, 167),Color.rgb(255, 139, 139),
                        Color.rgb(242, 208, 167), Color.rgb(170, 225, 191)
                };
                int[] color2 = {
                         Color.rgb(255, 139, 139),Color.rgb(242, 208, 167), Color.rgb(242, 223, 167),
                        Color.rgb(170, 225, 191),Color.rgb(124, 186, 201)
                };
                int[] color3 = {
                        Color.rgb(242, 223, 167), Color.rgb(255, 139, 139), Color.rgb(124, 186, 201),
                        Color.rgb(170, 225, 191), Color.rgb(242, 208, 167),
                };
                int[] color4 = {
                        Color.rgb(242, 208, 167), Color.rgb(124, 186, 201), Color.rgb(170, 225, 191),
                        Color.rgb(255, 139, 139), Color.rgb(242, 223, 167),
                };
                int[] color5 = {
                        Color.rgb(242, 223, 167), Color.rgb(170, 225, 191), Color.rgb(242, 208, 167),
                        Color.rgb(124, 186, 201), Color.rgb(255, 139, 139)

                };
                Random random = new Random();
                int randomNum;
                randomNum = random.nextInt(5) + 1;
                switch (randomNum)
                {
                    case 1:
                        tmpcolor = color;
                        break;
                    case 2:
                        tmpcolor = color2;
                        break;
                    case 3:
                        tmpcolor = color3;
                        break;
                    case 4:
                        tmpcolor = color4;
                        break;
                    case 5:
                        tmpcolor = color5;
                        break;
                }
            /*int[] color = {
                    Color.rgb(242, 194, 203), Color.rgb(217, 132, 155), Color.rgb(191, 122, 160),
                    Color.rgb(242, 208, 167), Color.rgb(242, 203, 189) Color.rgb(250, 205, 82) Color.rgb(159, 215, 191)Color.rgb(235, 211, 201),
            };
*/
                dataset.setColors(tmpcolor);
                dataset.setSliceSpace(3f);
                pieChart.setBackgroundColor(Color.WHITE);
                //
                // dataset.setColors(color);

                data = new PieData(dataset);
                data.setValueFormatter(new PercentFormatter());

                data.setValueTextColor(Color.BLACK);
               /* dataset.setValueLinePart1OffsetPercentage(90.f);
                dataset.setValueLinePart1Length(1f);
                dataset.setValueLinePart2Length(.2f);

                dataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);*/
                pieChart.removeAllViews();
                pieChart.clearAnimation();
                pieChart.setData(data);
                pieChart.setDescription(null);
                pieChart.setEntryLabelColor(Color.BLACK);
            }

        }else
        {
            // layout.removeAllViewsInLayout();
            //  layout.refreshDrawableState();

        }
                /*
                LinearLayout linearLayout = new LinearLayout(getActivity());
                LinearLayout.LayoutParams lp = null;
                lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
                lp.setMargins(5,5,5,5);
                lp.gravity = Gravity.CENTER;
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setLayoutParams(lp);

                ImageView imageView = new ImageView(getActivity());
                lp = new LinearLayout.LayoutParams( 0,120);
                lp.weight = 2;
                lp.gravity = Gravity.START;
                String result = Moodlist.get(i).getEmoji();
            int id = getResources().getIdentifier(result, "drawable", getActivity().getPackageName());
                Drawable drawable = getResources().getDrawable(id);
                   /* imageView.setImageDrawable(drawable);
                imageView.setLayoutParams(lp);

                TextView textView = new TextView(getActivity());
                lp = new LinearLayout.LayoutParams(0, 120);
                lp.weight = 4;
                lp.gravity = Gravity.CENTER;
                lp.setMargins(10,10,10,10);
                textView.setTextSize(30);
                double d1, d2, number;
                d1 = (double)Moodlist.get(i).getCOUNT();
                d2 = (double)Total;
                number = d1/d2 * 100;
                String num = String.format("%.1f", number);
                yvlues.add(new PieEntry((float)number, drawable));
                xVals.add(result);
                 textView.setText(num + "%");
                textView.setLayoutParams(lp);

                linearLayout.addView(imageView);
                linearLayout.addView(textView);
                layout.addView(linearLayout);*/

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void setDate(String _date)
    {
        Date = _date;
       // layout.removeViews(0,layout.getChildCount());
       // layout.removeAllViews();
       // layout.removeAllViewsInLayout();
       // layout.refreshDrawableState();
        if(Date != null)
        {

            ExecuteCount(Date);}

    }
    @Deprecated
    public void onAttach( Activity activity ) {
        super.onAttach( activity );


    }
    @Override
    public void onResume()
    {
        super.onResume();
        if(!getUserVisibleHint()){
            return;
        }else
        {
            if(Date!=null) {
                ExecuteCount(Date);
            }
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
      //  layout.removeAllViewsInLayout();
      //  layout.refreshDrawableState();
        mListener = null;
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
