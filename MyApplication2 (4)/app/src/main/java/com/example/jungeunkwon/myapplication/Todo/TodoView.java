package com.example.jungeunkwon.myapplication.Todo;

import android.view.View;
import android.widget.TextView;


public interface TodoView {
    public void addcheckedview(View view);
    public void adduncheckedview(View view);
    public void clear();
    public void chagedate(TextView view);
    public void updatepin();
}
