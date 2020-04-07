package com.Jungeun.wjdwjd95.emotional_trashcan.MoodChart;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by jungeun.kwon on 4/24/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    private static int PAGE_NUMBER = 2;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MoodChartFragment.newInstance();
            case 1:
                return MoodListFragment.newInstance(MoodListFragment.Date);
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Mood";
            case 1:
                return "Mood Chart";
            default:
                return null;
        }
    }
}
