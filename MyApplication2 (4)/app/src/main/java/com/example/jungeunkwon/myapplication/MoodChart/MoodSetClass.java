package com.example.jungeunkwon.myapplication.MoodChart;

import android.widget.ImageView;

public class MoodSetClass {
    String day;
    String month;
    String year;
    ImageView imageView;
    public String getDay() {
        return day;
    }

    public String getYear() {
        return year;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getMonth() {
        return month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
