package com.example.jungeunkwon.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toolbar;


import com.example.jungeunkwon.myapplication.Diary.DiaryActivity;
import com.example.jungeunkwon.myapplication.GoalPlanner.GoalPlannerActivity;
import com.example.jungeunkwon.myapplication.MoodChart.MoodChartActivity;
import com.example.jungeunkwon.myapplication.Todo.Todoactivity;
import com.example.jungeunkwon.myapplication.Trash.TrashActivity;
import com.example.jungeunkwon.myapplication.R;

public class MainActivity extends AppCompatActivity {

    Toolbar Toptoolbar;
    Toolbar Bottomtoolbar;
    Button btndiary;
    Button btntrash;
    Button btnmood;
    Button btntodo;
    Button btngoal;
    private static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = MainActivity.this.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        Boolean IsFirstOpen = prefs.getBoolean(Constants.SHARED_PREF_KEY_MAIN_ACTIVITY_FIRST, true);
        if (IsFirstOpen) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.intro_main_template, null);

            ImageButton exit = (ImageButton) dialogView.findViewById(R.id.intro_main_btngotit);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(Constants.SHARED_PREF_KEY_MAIN_ACTIVITY_FIRST, false);
                    editor.apply();
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.setContentView(dialogView);
            dialog.show();
        }
        Toptoolbar = (Toolbar)findViewById(R.id.main_activity_top);
        Toptoolbar.setTitle(getString(R.string.multi_name));
        Toptoolbar.setNavigationIcon(R.drawable.main_toptool_icon);
        Bottomtoolbar = (Toolbar)findViewById(R.id.main_activity_bottom);
        btndiary  = (Button)findViewById(R.id.main_activit_btndiary);
        btnmood = (Button)findViewById(R.id.main_activit_btnMood);
        btntrash = (Button)findViewById(R.id.main_activit_btntrash);
        btntodo = (Button)findViewById(R.id.main_activit_btnTodo);
        btngoal = (Button)findViewById(R.id.main_activit_btngoal);
        btndiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiaryActivity.class );
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });
        btnmood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MoodChartActivity.class );
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });
        btntrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TrashActivity.class );
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);

            }
        });
        btntodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Todoactivity.class );
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });
        btngoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GoalPlannerActivity.class );
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

    }

}
