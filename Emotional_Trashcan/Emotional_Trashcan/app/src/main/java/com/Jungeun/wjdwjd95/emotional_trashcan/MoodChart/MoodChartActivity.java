package com.Jungeun.wjdwjd95.emotional_trashcan.MoodChart;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toolbar;
import android.support.design.widget.TabLayout;

import com.Jungeun.wjdwjd95.emotional_trashcan.Constants;
import com.Jungeun.wjdwjd95.emotional_trashcan.R;


public class MoodChartActivity extends AppCompatActivity implements MoodChartFragment.SetDate {
    Toolbar Toptoolbar;
    Toolbar Bottomtoolbar;
    private static SharedPreferences prefs;

    PagerAdapter mPagerAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_chart);

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        prefs = MoodChartActivity.this.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        Boolean IsFirstOpen = prefs.getBoolean(Constants.SHARED_PREF_KEY_MOOD_ACTIVITY_FIRST, true);
        if (IsFirstOpen) {
            final Dialog dialog = new Dialog(MoodChartActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            LayoutInflater inflater = MoodChartActivity.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.intro_moodchart_template, null);

            ImageButton exit = (ImageButton) dialogView.findViewById(R.id.intro_moodchart_btngotit);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(Constants.SHARED_PREF_KEY_MOOD_ACTIVITY_FIRST, false);
                    editor.apply();
                    dialog.dismiss();
                }
            });
            dialog.setContentView(dialogView);
            dialog.setCancelable(false);
            dialog.show();
        }
        final ViewPager mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout mTab = (TabLayout)findViewById(R.id.tabs);
        mTab.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mViewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mViewPager.getAdapter().notifyDataSetChanged();
            }
        });
        Toptoolbar = (Toolbar) findViewById(R.id.mood_activity_top);
        Toptoolbar.setTitle(getString(R.string.multi_Moodchart));
        Toptoolbar.setNavigationIcon(R.drawable.goback);
        Toptoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });
        Bottomtoolbar = (Toolbar) findViewById(R.id.mood_activity_bottom);

    }

    @Override
    public void setdate(String _date)
    {

        FragmentManager fragmentManager = getSupportFragmentManager();
        MoodListFragment secondFragment = (MoodListFragment)fragmentManager.findFragmentById(R.id.mood_list);
        if(secondFragment != null)
        {
            secondFragment.setDate(_date);
        }
        else
        {

            Bundle args = new Bundle();
            args.putString("Date",_date);
            MoodListFragment newFragment = new MoodListFragment();
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.mood_list, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }

    }

    @Override
    public void onBackPressed()
    {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
    @Override
    public void onStop()
    {
        super.onStop();
    }
}
