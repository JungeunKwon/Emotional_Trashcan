package com.example.jungeunkwon.myapplication.GoalPlanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.jungeunkwon.myapplication.Constants;
import com.example.jungeunkwon.myapplication.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class GoalPlannerActivity extends AppCompatActivity implements GoalView {
    GridView gridView;
    GoalGridAdapter goalGridAdapter;
    Calendar mCal;
    Date date;
    ArrayList<GoalSetClass> dayList;
    int dayNum = 0;
    long lnow;
    final int DIALOG_START_DATE = 1;
    final int DIALOG_END_DATE = 2;
    final int DIALOG_TIME = 3;
    Date now;
    final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.US);
    final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.US);
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    GoalPresenter mPresenter;
    Toolbar Toptoolbar;
    Toolbar Bottomtoolbar;
    ImageButton btnleft;
    ImageButton btnright;
    TextView txtmain;
    ImageButton btnfail;
    ImageButton btnsucces;
    TextView lblgoal;
    LinearLayout goallayout;
    ImageView imgIcon;
    SharedPreferences prefs;
    String insertdate;
    TextView innerstartDate;
    TextView innerendDate;
    ImageView icon;
    boolean iconcliked;
    PushAlarm pushAlarm;
    TextView lblgoalrate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_planner);
        gridView = (GridView)findViewById(R.id.goal_activity_calender);
        mPresenter = new GoalPresenter(GoalPlannerActivity.this, this);
        btnleft = (ImageButton)findViewById(R.id.goal_activity_btnleft);
        txtmain = (TextView)findViewById(R.id.goal_activity_maintxt);
        lnow = System.currentTimeMillis();
        date = new Date(lnow);
        btnright = (ImageButton)findViewById(R.id.goal_activity_btnright);
        lblgoalrate =(TextView)findViewById(R.id.goal_activity_lblgoalrate);
        prefs = GoalPlannerActivity.this.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        Boolean IsFirstOpen = prefs.getBoolean(Constants.SHARED_PREF_KEY_GOALPLANNER_ACTIVITY_FIRST, true);
        if (IsFirstOpen)
        {
            final Dialog dialog = new Dialog(GoalPlannerActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            LayoutInflater inflater = GoalPlannerActivity.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.intro_goal_planner_template, null);

            ImageButton exit = (ImageButton) dialogView.findViewById(R.id.intro_goal_planner_btngotit);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(Constants.SHARED_PREF_KEY_GOALPLANNER_ACTIVITY_FIRST, false);
                    editor.apply();
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.setContentView(dialogView);
            dialog.show();

        }
        btnfail = (ImageButton)findViewById(R.id.goal_activity_btnfail);
        btnfail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtnFail_Click();
            }
        });
        btnsucces = (ImageButton)findViewById(R.id.goal_activity_btnsuccess);
        btnsucces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtnSucces_Click();
            }
        });
        lblgoal = (TextView)findViewById(R.id.goal_activity_lblgoal);
        goallayout = (LinearLayout)findViewById(R.id.goal_activity_goal_layout);
        imgIcon = (ImageView)findViewById(R.id.goal_activity_imgicon);
        pushAlarm =  new PushAlarm(GoalPlannerActivity.this);
        Toptoolbar = (Toolbar) findViewById(R.id.goal_activity_top);
        Toptoolbar.setTitle(getString(R.string.multi_GoalPlanner));
        Toptoolbar.setNavigationIcon(R.drawable.goback);
        boolean ison = prefs.getBoolean(Constants.SHARED_PREF_KEY_NOTI_STATUS, false);
        if(ison)
        {
            Toptoolbar.getMenu().clear();
            Toptoolbar.inflateMenu(R.menu.top_menu_for_goal);
            //   pushAlarm.releaseAlarm();
        }
        else {
            Toptoolbar.getMenu().clear();
            Toptoolbar.inflateMenu(R.menu.top_menu_for_goal_2);
        }
        Toptoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        Toptoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.goal_menu_set_noti: {
                        BtnNoti_Click();
                        break;
                    }
                    case R.id.goal_menu_set_noti_2:
                    {
                        BtnNoti_Click();
                        break;
                    }
                    case R.id.goal_menu_history:
                    {
                        BtnHistory_Click();
                        break;
                    }
                    case R.id.goal_menu_history_2:
                    {
                        BtnHistory_Click();
                        break;
                    }

                }
                return false;
            }
        });
        Bottomtoolbar = (Toolbar) findViewById(R.id.goal_activity_bottom);
        Bottomtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //  Log.WriteLine("bottom clicked " + item.getItemId() + item.getTitle());
                switch (item.getItemId()) {
                    case R.id.bottom_menu_write_only_write:
                        BtnWrite_Click();
                        break;
                }
                return true;
            }
        });
        Bottomtoolbar.inflateMenu(R.menu.bottom_menu_write_only);

        btnleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    date.setDate(1);
                    date.setMonth(date.getMonth() -1);
                    getCalendar(date);
                }catch (Exception ex)
                {
                    Toast.makeText(GoalPlannerActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(GoalPlannerActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        getCalendar(date);
        getGoal();

    }
    public void BtnHistory_Click(){
        {

            List<GoalEntity> list = mPresenter.gettotalgoal();
            if(list != null && list.size() > 0)
            {
                final AlertDialog.Builder inneralert = new AlertDialog.Builder(GoalPlannerActivity.this);
                LayoutInflater inflater = GoalPlannerActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.goal_history_template, null);
                inneralert.setView(dialogView);
                inneralert.setCancelable(false);
                final AlertDialog alertDialog = inneralert.create();
                alertDialog.show();

                final LinearLayout success = (LinearLayout)dialogView.findViewById(R.id.goal_history_successlist);
                final LinearLayout fail = (LinearLayout)dialogView.findViewById(R.id.goal_history_faillist);

                for(int i =0; i <list.size(); i++)
                {
                    String Status = list.get(i).getStatus();
                    if(Status.equals(Constants.GOAL_STATUS_ING))
                        continue;
                    LinearLayout linearLayout = new LinearLayout(GoalPlannerActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    lp.gravity = Gravity.CENTER;

                    linearLayout.setLayoutParams(lp);
                    linearLayout.setGravity(Gravity.CENTER);

                    TextView comment = new TextView(GoalPlannerActivity.this);
                    lp = new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.weight = 4 ;
                    lp.gravity = Gravity.CENTER;
                    comment.setText(list.get(i).getContent());
                    comment.setLayoutParams(lp);

                    TextView ratio = new TextView(GoalPlannerActivity.this);
                    lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10,10,10,10);
                    lp.gravity = Gravity.CENTER;

                    String goalratio = mPresenter.getGoalrate(list.get(i).getID());
                    ratio.setText(goalratio);
                    ratio.setLayoutParams(lp);
                    ImageButton exitButton = new ImageButton(GoalPlannerActivity.this);

                    TextView status = new TextView(GoalPlannerActivity.this);
                    status.setGravity(Gravity.CENTER);
                    lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    status.setPadding(10,10,10,10);
                    lp.setMargins(10,10,10,10);
                    lp.gravity = Gravity.CENTER_VERTICAL;
                    status.setText(Status);
                    status.setLayoutParams(lp);

                    lp.gravity = Gravity.END;
                    exitButton.setLayoutParams(lp);
                    exitButton.setImageResource(R.drawable.close);
                    exitButton.setBackgroundColor(Color.TRANSPARENT);
                    exitButton.setTag(list.get(i).getID());
                    exitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String ID = v.getTag().toString();
                            mPresenter.RemoveGoal(ID);
                            alertDialog.dismiss();
                            BtnHistory_Click();
                        }
                    });
                    linearLayout.addView(comment);
                    linearLayout.addView(ratio);
                    linearLayout.addView(status);
                    linearLayout.addView(exitButton);
                    if(Status.equals(Constants.GOAL_STATUS_FAIL))
                    {
                        fail.addView(linearLayout);
                    }else
                    {
                        success.addView(linearLayout);
                    }

                }
                Button exit = (Button)dialogView.findViewById(R.id.goal_history__btnexit);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        getCalendar(date);
                    }
                });
            }else
            {
                Toast.makeText(GoalPlannerActivity.this, getString(R.string.multi_alert_no_data), Toast.LENGTH_SHORT).show();
            }


        }
    }
    public void BtnSucces_Click()
    {
        final AlertDialog.Builder inneralert = new AlertDialog.Builder(GoalPlannerActivity.this);
        LayoutInflater inflater = GoalPlannerActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_write,null);
        inneralert.setView(dialogView);
        inneralert.setCancelable(false);
        final AlertDialog alertDialog = inneralert.create();
        alertDialog.show();
        TextView title = (TextView)dialogView.findViewById(R.id.alert_dialog_write_title);
        title.setText(getString(R.string.multi_GoalPlanner_Success));
        TextView lbldate = (TextView)dialogView.findViewById(R.id.alert_dialog_write_lbldate);
        lbldate.setVisibility(View.INVISIBLE);
        TextView lbltext = (TextView)dialogView.findViewById(R.id.alert_dialog_write_txtDate);
        now = new Date();
        lbltext.setText(getString(R.string.multi_GoalPlanner_Succes_caption));
        lbltext.setGravity(Gravity.CENTER);
        ImageView btncalendar = (ImageView)dialogView.findViewById(R.id.alert_dialog_write_btnDate);
        btncalendar.setVisibility(View.GONE);
        final EditText diary = (EditText)dialogView.findViewById(R.id.alert_dialog_write_edittxt);
        diary.setVisibility(View.GONE);
        ImageButton Save = (ImageButton)dialogView.findViewById(R.id.alert_dialog_write_btnsave);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.update(Constants.GOAL_STATUS_FINISH);
                alertDialog.dismiss();
                Toptoolbar.getMenu().clear();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(Constants.SHARED_PREF_KEY_NOTI_STATUS, false);
                editor.apply();
                Toptoolbar.inflateMenu(R.menu.top_menu_for_goal_2);
                pushAlarm.releaseAlarm();
                getGoal();
            }
        });

        Button exit = (Button)dialogView.findViewById(R.id.alert_dialog_write_btnexit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
    public void BtnFail_Click()
    {
        final AlertDialog.Builder inneralert = new AlertDialog.Builder(GoalPlannerActivity.this);
        LayoutInflater inflater = GoalPlannerActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_write,null);
        inneralert.setView(dialogView);
        inneralert.setCancelable(false);
        final AlertDialog alertDialog = inneralert.create();
        alertDialog.show();
        TextView title = (TextView)dialogView.findViewById(R.id.alert_dialog_write_title);
        title.setText(getString(R.string.multi_GoalPlanner_fail));
        TextView lbldate = (TextView)dialogView.findViewById(R.id.alert_dialog_write_lbldate);
        lbldate.setVisibility(View.INVISIBLE);
        TextView lbltext = (TextView)dialogView.findViewById(R.id.alert_dialog_write_txtDate);
        now = new Date();
        lbltext.setText(getString(R.string.multi_GoalPlanner_fail_caption));
        lbltext.setGravity(Gravity.CENTER);
        ImageView btncalendar = (ImageView)dialogView.findViewById(R.id.alert_dialog_write_btnDate);
        btncalendar.setVisibility(View.GONE);
        final EditText diary = (EditText)dialogView.findViewById(R.id.alert_dialog_write_edittxt);
        diary.setVisibility(View.GONE);
        ImageButton Save = (ImageButton)dialogView.findViewById(R.id.alert_dialog_write_btnsave);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.update(Constants.GOAL_STATUS_FAIL);
                alertDialog.dismiss();
                Toptoolbar.getMenu().clear();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(Constants.SHARED_PREF_KEY_NOTI_STATUS, false);
                editor.apply();
                Toptoolbar.inflateMenu(R.menu.top_menu_for_goal_2);
                pushAlarm.releaseAlarm();
                getGoal();
            }
        });

        Button exit = (Button)dialogView.findViewById(R.id.alert_dialog_write_btnexit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
    public void getGoal()
    {
        GoalEntity goalEntity = mPresenter.IsGoal_getentity();
        if(goalEntity != null)
        {
            now = new Date();
            GoalEntity goaldateEntity = mPresenter.getGoal_ifexistatdate(dateFormat.format(now));
            if(goaldateEntity != null)
            {
                goallayout.setVisibility(View.VISIBLE);
                lblgoal.setText(goalEntity.getContent());
                String ratio = mPresenter.getGoalrate(goaldateEntity.getID());
                lblgoalrate.setText(ratio);
                String result = goalEntity.getIcon();
                int id = getResources().getIdentifier(result, "drawable", getPackageName());
                Drawable drawable = getResources().getDrawable(id);
                imgIcon.setImageDrawable(drawable);
                imgIcon.setTag(goalEntity.getID());
            }else
            {
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(GoalPlannerActivity.this);
                alertdialog.setTitle(goalEntity.getContent());
                alertdialog.setMessage(getString(R.string.multi_GoalPlanner_over_date_caption));
                alertdialog.setCancelable(false);
                alertdialog.setNegativeButton(getString(R.string.multi_GoalPlanner_fail), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            BtnFail_Click();
                    }
                });
                alertdialog.setPositiveButton(getString(R.string.multi_GoalPlanner_Success), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BtnSucces_Click();
                    }
                });
                alertdialog.show();
            }




        }else
        {
            goallayout.setVisibility(View.GONE);
        }
        getCalendar(date);
    }
    public void getCalendar(Date pDate)
    {
        txtmain.setText(curYearFormat.format(pDate) + "-" + curMonthFormat.format(pDate));

        dayList = new ArrayList<GoalSetClass>();
        mCal = Calendar.getInstance();

        mCal.set(Integer.parseInt(curYearFormat.format(pDate)),Integer.parseInt(curMonthFormat.format(pDate))-1,1);
        dayNum = mCal.get(Calendar.DAY_OF_WEEK) -1;
        if(dayNum != 0) {
            for (int i = 0; i < dayNum; i++) {
                GoalSetClass GoalSetClass = new GoalSetClass();
                GoalSetClass.setDay("");
                GoalSetClass.setMonth(curMonthFormat.format(pDate));
                GoalSetClass.setYear(curYearFormat.format(pDate));
                ImageView imageView = new ImageView(this);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100,100);
                lp.weight = 2;
                lp.gravity = Gravity.CENTER;
                lp.setMargins(20,10,20,20);
                imageView.setLayoutParams(lp);
                GoalSetClass.setImageView(imageView);
                dayList.add(GoalSetClass);
            }
        }
        setCalendarDate(mCal.get(Calendar.MONTH) + 1);
        goalGridAdapter = new GoalGridAdapter(this, dayList);
        gridView.setAdapter(goalGridAdapter);
        dayNum = 0;
    }
    private void setCalendarDate(final int Month)
    {
        mCal.set(Calendar.MONTH, Month-1);
        for(int i = 0; i<mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++)
        {
            GoalSetClass GoalSetClass = new GoalSetClass();
            GoalSetClass.setMonth(curMonthFormat.format(date));
            GoalSetClass.setYear(curYearFormat.format(date));
            String tmpday = "";
            if((i+1) < 10)
            {
                tmpday = "0" + (i+1);
            }else
            {
                tmpday = Integer.toString((i+1));
            }
            String getdate = curYearFormat.format(date)+"-"+ curMonthFormat.format(date) +"-"+ tmpday;
            GoalSetClass.setDay("" + tmpday);
            final ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams lp = null;
            lp = new LinearLayout.LayoutParams(100,0);
            lp.weight = 8;
            lp.gravity = Gravity.CENTER;
            lp.setMargins(20,10,20,20);
            if(mPresenter.selectDate(getdate) > 0)
            {
                String result = mPresenter.getIcon(getdate);
                int id = getResources().getIdentifier(result, "drawable", getPackageName());
                Drawable drawable = getResources().getDrawable(id);
                imageView.setImageDrawable(drawable);
            }
            imageView.setLayoutParams(lp);
            imageView.setTag(i + dayNum);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    goalGridAdapter.ChangeImage(Integer.parseInt(v.getTag().toString()), prefs.getString(Constants.SHARED_PREF_KEY_CURRENT_ID,""));
                    getgoal();
                }
            });
            GoalSetClass.setImageView(imageView);
            dayList.add(GoalSetClass);
        }
    }

    public void BtnWrite_Click()
    {

        if(mPresenter.IsGoal())
        {
            Toast.makeText(GoalPlannerActivity.this, getString(R.string.multi_GoalPlanner_already_msg), Toast.LENGTH_SHORT).show();
            return;
        }
        iconcliked = false;
        final AlertDialog.Builder inneralert = new AlertDialog.Builder(GoalPlannerActivity.this);
        LayoutInflater inflater = GoalPlannerActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.goal_insert_template,null);
        inneralert.setView(dialogView);
        inneralert.setCancelable(false);
        final AlertDialog alertDialog = inneralert.create();
        alertDialog.show();
        innerstartDate = (TextView)dialogView.findViewById(R.id.goal_insert_template_txtstartDate);
        innerendDate = (TextView)dialogView.findViewById(R.id.goal_insert_template_txtendDate);
        now = new Date();
        insertdate = dateFormat.format(now);
        innerstartDate.setText(insertdate);
        innerendDate.setText(insertdate);
        ImageView btnstartcalendar = (ImageView)dialogView.findViewById(R.id.goal_insert_template_btnstartDate);
        ImageView btnendcalendar = (ImageView)dialogView.findViewById(R.id.goal_insert_template_btnendDate);

        btnstartcalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_START_DATE);
            }
        });
        btnendcalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_END_DATE);
            }
        });
        final EditText Goal = (EditText)dialogView.findViewById(R.id.goal_insert_template_edittxt);
        icon = (ImageView)dialogView.findViewById(R.id.goal_insert_template_btnicon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtnIcon_Click();
                iconcliked =true;
            }
        });
        ImageButton Save = (ImageButton)dialogView.findViewById(R.id.goal_insert_template_btnsave);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = Goal.getText().toString();
                if(content.equals("")||content.isEmpty())
                {
                    Toast.makeText(GoalPlannerActivity.this, getString(R.string.multi_alert_inputmsg), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(content.length() > 50)
                {
                    Toast.makeText(GoalPlannerActivity.this, getString(R.string.multi_GoalPlanner_enter_out_of_text), Toast.LENGTH_SHORT).show();
                    return;
                }
                GoalEntity goalEntity = new GoalEntity();
                goalEntity.setStartDate(innerstartDate.getText().toString());
                goalEntity.setEndDate(innerendDate.getText().toString());
                goalEntity.setContent(content);
                goalEntity.setStatus(Constants.GOAL_STATUS_ING);
                String Icon;
                if(!iconcliked)
                {
                    Icon = "goal_1";
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(Constants.SHARED_PREF_KEY_CURRENT_GOAL_IMG,Icon );
                    editor.apply();
                }
                else {
                    Icon = prefs.getString(Constants.SHARED_PREF_KEY_CURRENT_GOAL_IMG, "goal_1");
                }
                SharedPreferences.Editor ediotr = prefs.edit();
                ediotr.putString(Constants.SHARED_PREF_KEY_CURRENT_GOAL_NAME, content);
                ediotr.apply();
                goalEntity.setIcon(Icon);
                alertDialog.dismiss();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//FromContext(this);
                imm.hideSoftInputFromWindow(Goal.getWindowToken(), 0);
                if(mPresenter.insert(goalEntity))
                {
                    Toast.makeText(GoalPlannerActivity.this, getString(R.string.multi_alert_insert_good), Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(GoalPlannerActivity.this, getString(R.string.multi_alert_insert_error), Toast.LENGTH_SHORT).show();
                }
                getGoal();

            }
        });

        Button exit = (Button)dialogView.findViewById(R.id.goal_insert_template_btnexit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        getGoal();

    }
    public void BtnIcon_Click()
    {
        final AlertDialog.Builder inneralert = new AlertDialog.Builder(GoalPlannerActivity.this);
        LayoutInflater inflater = GoalPlannerActivity.this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.goalplanner_icon_template,null);
        inneralert.setView(dialogView);
        inneralert.setCancelable(true);
        final AlertDialog alertDialog = inneralert.create();
        final ImageButton btnarray[] = new ImageButton[35];
        for(int i = 1; i <35; i++)
        {
            String aString = "goalplanner_icon_template_" + i;
            String packageName = getPackageName();

            int resId = getResources().getIdentifier(aString, "id", packageName);
            btnarray[i] = (ImageButton)dialogView.findViewById(resId);
            btnarray[i].setTag("goal_" + i);
            btnarray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Tag = v.getTag().toString();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(Constants.SHARED_PREF_KEY_CURRENT_GOAL_IMG,Tag );
                    editor.apply();
                    int id = getResources().getIdentifier(Tag, "drawable", getPackageName());
                    Drawable drawable = getResources().getDrawable(id);
                    icon.setImageDrawable(drawable);
                    alertDialog.dismiss();
                }
            });
        }
        alertDialog.show();

    }
    public void BtnNoti_Click()
    {
        try {
            SharedPreferences.Editor editor = prefs.edit();
            if(!mPresenter.IsGoal())
            {
                Toptoolbar.getMenu().clear();
                Toast.makeText(GoalPlannerActivity.this, getString(R.string.multi_GoalPlanner_set_goal_error), Toast.LENGTH_SHORT).show();
                editor.putBoolean(Constants.SHARED_PREF_KEY_NOTI_STATUS, false);
                editor.apply();
                Toptoolbar.inflateMenu(R.menu.top_menu_for_goal_2);
                return;
            }

            boolean ison = prefs.getBoolean(Constants.SHARED_PREF_KEY_NOTI_STATUS, false);
            boolean result = false;
            if(ison)
            {
                Toptoolbar.getMenu().clear();
                result = false;
                Toptoolbar.inflateMenu(R.menu.top_menu_for_goal_2);
                pushAlarm.releaseAlarm();
                Toast.makeText(GoalPlannerActivity.this, getString(R.string.multi_GoalPlanner_remove_alarm), Toast.LENGTH_SHORT).show();
            }
            else {
                Toptoolbar.getMenu().clear();
                result = true;
                Toptoolbar.inflateMenu(R.menu.top_menu_for_goal);
                showDialog(DIALOG_TIME);
            }
            editor.putBoolean(Constants.SHARED_PREF_KEY_NOTI_STATUS, result);
            editor.apply();
          //

            //pushAlarm =  new PushAlarm(GoalPlannerActivity.this);

        }catch (Exception ex)
        {
            ex.getMessage();
        }
    }
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        now = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minutes = calendar.get(Calendar.MINUTE);
        switch (id) {

            case DIALOG_START_DATE:
                DatePickerDialog dpd = new DatePickerDialog(GoalPlannerActivity.this, DatePickerDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        setDate(innerstartDate, year, monthOfYear, dayOfMonth);

                    }
                }, year, month, day);
                return dpd;

            case DIALOG_END_DATE:
                DatePickerDialog dpd2 = new DatePickerDialog(GoalPlannerActivity.this, DatePickerDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        setDate(innerendDate, year, monthOfYear, dayOfMonth);

                    }
                }, year, month, day);
                return dpd2;
            case DIALOG_TIME:
                TimePickerDialog tpd =  new TimePickerDialog(GoalPlannerActivity.this, TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String tmpmi = Integer.toString(minute);
                        if(minute <10)
                        {
                            tmpmi = "0" + minute;
                        }
                        String msg = getString(R.string.multi_GoalPlanner_alarm_set) +" " + hourOfDay + " : " + tmpmi  +" " + getString(R.string.multi_GoalPlanner_alarm_set_2);
                        Toast.makeText(GoalPlannerActivity.this, msg,Toast.LENGTH_LONG).show();
                        pushAlarm.Alarm(hourOfDay,minute);
                    }
                },hour,minutes, true);
                tpd.setTitle(getString(R.string.multi_GoalPlanner_choose_time));
                return tpd;
        }
        return super.onCreateDialog(id);
    }
    public void setDate(TextView lbl, int year, int month, int day) {
        String tmpmonth = "", tmpday = "";
        if ((month + 1) < 10) {
            tmpmonth = "0" + (month + 1);
        } else
            tmpmonth = Integer.toString(month + 1);
        if (day < 10) {
            tmpday = "0" + day;
        } else {
            tmpday = Integer.toString(day);
        }
        now.setYear(year);
        now.setMonth(month);
        now.setDate(day);
        insertdate = year + "-" + tmpmonth + "-" + tmpday;
        lbl.setText(insertdate);

    }
    @Override
    public void getgoal()
    {
        getGoal();
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
