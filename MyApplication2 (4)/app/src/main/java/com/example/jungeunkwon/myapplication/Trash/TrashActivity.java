package com.example.jungeunkwon.myapplication.Trash;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.jungeunkwon.myapplication.Constants;
import com.example.jungeunkwon.myapplication.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class TrashActivity extends Activity implements TrashView{
    Toolbar Toptoolbar;
    Toolbar Bottomtoolbar;
    LinearLayout layout;
    final int DIALOG_DATE = 1;
    Date now;

    TrashPresenter mPresenter;
    private static SharedPreferences prefs;
    String Today;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
        try {
            mPresenter = new TrashPresenter(TrashActivity.this, this);
            prefs = TrashActivity.this.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
            Boolean IsFirstOpen = prefs.getBoolean(Constants.SHARED_PREF_KEY_THROW_ACTIVITY_FIRST, true);
            if (IsFirstOpen) {
                mPresenter.insertTrashCommentFirst();
                final Dialog dialog = new Dialog(TrashActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            LayoutInflater inflater = TrashActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.intro_trash_template, null);

                ImageButton exit = (ImageButton) dialogView.findViewById(R.id.intro_btngotit);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean(Constants.SHARED_PREF_KEY_THROW_ACTIVITY_FIRST, false);
                        editor.apply();
                    }
                });
                dialog.setContentView(dialogView);
                dialog.show();


            /*
            final AlertDialog.Builder inneralert = new AlertDialog.Builder(TrashActivity.this);

            dialogView.setBackgroundColor(Color.TRANSPARENT);
            inneralert.setView(dialogView);
            inneralert.setCancelable(false);
            final AlertDialog alertDialog = inneralert.create();
            alertDialog.show();
            */
                //설명창 시작
            }}catch (Exception ex)
        {
            Toast.makeText(this,ex.getMessage(), Toast.LENGTH_LONG).show();
        }
            Date date = new Date();

            Today = Constants.dateFormat.format(date);
            mPresenter.removepasseddat(Today);
            layout = (LinearLayout) findViewById(R.id.activity_trash_layout);
            Toptoolbar = (Toolbar) findViewById(R.id.trash_activity_top);
            Toptoolbar.setTitle(getString(R.string.multi_trashcan));
            Toptoolbar.setNavigationIcon(R.drawable.goback);
            Toptoolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                }
            });
            Toptoolbar.inflateMenu(R.menu.top_menu_for_trash);
            Toptoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.trash_menu_add_heart: {
                            BtnAdd_Click();
                            break;
                        }
                        case R.id.trash_menu_remove_heart: {
                            BtnRemove_Click();
                            break;
                        }
                    }
                    return false;
                }
            });
            Bottomtoolbar = (Toolbar) findViewById(R.id.trash_activity_bottom);
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
            mPresenter.getList(Today);

    }
    public void BtnRemove_Click()
    {
        List<TrashCommentEntity> list = mPresenter.getCommentList();
        if(list != null && list.size() > 0)
        {
            final AlertDialog.Builder inneralert = new AlertDialog.Builder(TrashActivity.this);
            LayoutInflater inflater = TrashActivity.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.heartremove_template, null);
            inneralert.setView(dialogView);
            inneralert.setCancelable(false);
            final AlertDialog alertDialog = inneralert.create();
            alertDialog.show();
            final LinearLayout parent = (LinearLayout)dialogView.findViewById(R.id.heart_remove_template_layout);
            for(int i =0; i <list.size(); i++)
            {
                LinearLayout linearLayout = new LinearLayout(TrashActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setBackgroundResource(R.drawable.frame);
                lp.gravity = Gravity.CENTER;

                lp.setMargins(0,5,0,5);
                linearLayout.setLayoutParams(lp);  linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setPadding(10,10,10,10);
                TextView comment = new TextView(TrashActivity.this);
                lp = new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.weight = 9;
                comment.setText(list.get(i).getComment());
                comment.setLayoutParams(lp);
                ImageButton exitButton = new ImageButton(TrashActivity.this);
                lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                lp.gravity = Gravity.END;
                exitButton.setLayoutParams(lp);
                exitButton.setImageResource(R.drawable.close);
                exitButton.setBackgroundColor(Color.TRANSPARENT);
                exitButton.setTag(list.get(i).getSeq());
                exitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String seq = v.getTag().toString();
                       if(!mPresenter.IsUsing(seq))
                        {
                            mPresenter.deletecomment(seq);
                            alertDialog.dismiss();
                            BtnRemove_Click();
                        }else
                       {
                           Toast.makeText(TrashActivity.this, getString(R.string.multi_alert_comment_using), Toast.LENGTH_SHORT).show();
                           return;
                       }
                    }
                });
                linearLayout.addView(comment);
                linearLayout.addView(exitButton);
                parent.addView(linearLayout);
            }
            Button exit = (Button)dialogView.findViewById(R.id.remove_heart_btnexit);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }else
        {
            Toast.makeText(TrashActivity.this, getString(R.string.multi_alert_no_data), Toast.LENGTH_SHORT).show();
        }


    }
    public void BtnAdd_Click()
    {
        final AlertDialog.Builder inneralert = new AlertDialog.Builder(TrashActivity.this);
        LayoutInflater inflater = TrashActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_write,null);
        inneralert.setView(dialogView);
        inneralert.setCancelable(false);
        final AlertDialog alertDialog = inneralert.create();
        alertDialog.show();
        final EditText diary = (EditText)dialogView.findViewById(R.id.alert_dialog_write_edittxt);
        TextView title = (TextView)dialogView.findViewById(R.id.alert_dialog_write_title);
        title.setText(getString(R.string.multi_throw_add_note));
        ImageView btncalendar = (ImageView)dialogView.findViewById(R.id.alert_dialog_write_btnDate);
        btncalendar.setVisibility(View.GONE);
        TextView dateview = (TextView)dialogView.findViewById(R.id.alert_dialog_write_lbldate);
        dateview.setText(getString(R.string.multi_throw_caption));
        ImageButton Save = (ImageButton)dialogView.findViewById(R.id.alert_dialog_write_btnsave);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = diary.getText().toString();
               /* if(content.length() > 200)
                {
                    Toast.makeText(DiaryActivity.this, "It should be less than 200 charter", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                if(content.equals("")||content.isEmpty())
                {
                    Toast.makeText(TrashActivity.this, getString(R.string.multi_alert_inputmsg), Toast.LENGTH_SHORT).show();
                    return;
                }
                TrashCommentEntity trashCommentEntity = new TrashCommentEntity();
                trashCommentEntity.setComment(content);
                alertDialog.dismiss();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//FromContext(this);
                imm.hideSoftInputFromWindow(diary.getWindowToken(), 0);
                if(mPresenter.insertTrashComment(trashCommentEntity))
                {
                    Toast.makeText(TrashActivity.this, getString(R.string.multi_alert_insert_good), Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(TrashActivity.this, getString(R.string.multi_alert_insert_error), Toast.LENGTH_SHORT).show();
                }



//                ClearListView();
                mPresenter.getList(Today);
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
    public void BtnWrite_Click()
    {
        final AlertDialog.Builder inneralert = new AlertDialog.Builder(TrashActivity.this);
        LayoutInflater inflater = TrashActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_write,null);
        inneralert.setView(dialogView);
        inneralert.setCancelable(false);
        final AlertDialog alertDialog = inneralert.create();
        alertDialog.show();
        TextView title = (TextView)dialogView.findViewById(R.id.alert_dialog_write_title);
        title.setText(getString(R.string.multi_add_trash));
        TextView lbldate = (TextView)dialogView.findViewById(R.id.alert_dialog_write_lbldate);
        TextView txtdate = (TextView)dialogView.findViewById(R.id.alert_dialog_write_txtDate);
        ImageView btndate = (ImageView)dialogView.findViewById(R.id.alert_dialog_write_btnDate);
        txtdate.setVisibility(View.INVISIBLE);
        btndate.setVisibility(View.INVISIBLE);
        lbldate.setText(getString(R.string.multi_add_trash_caption));
        final EditText diary = (EditText)dialogView.findViewById(R.id.alert_dialog_write_edittxt);
        ImageButton Save = (ImageButton)dialogView.findViewById(R.id.alert_dialog_write_btnsave);
        //Save.setText(getString(R.string.multi_throw));
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = diary.getText().toString();
               /* if(content.length() > 200)
                {
                    Toast.makeText(DiaryActivity.this, "It should be less than 200 charter", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                if(content.equals("")||content.isEmpty())
                {
                    Toast.makeText(TrashActivity.this, getString(R.string.multi_alert_inputmsg), Toast.LENGTH_SHORT).show();
                    return;
                }
                TrashEntity trashEntity = new TrashEntity();
                Date now = new Date();
                trashEntity.setDate(Constants.dateFormat.format(now));
                trashEntity.setContent(content);
                alertDialog.dismiss();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//FromContext(this);
                imm.hideSoftInputFromWindow(diary.getWindowToken(), 0);
                if(mPresenter.insertTrash(trashEntity))
                {
                    Toast.makeText(TrashActivity.this, getString(R.string.multi_alert_insert_good), Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(TrashActivity.this, getString(R.string.multi_alert_insert_error), Toast.LENGTH_SHORT).show();
                }



//                ClearListView();
                mPresenter.getList(Today);
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

    @Override
    public void clear()
    {
        layout.removeAllViews();
    }
    @Override
    public void addView(LinearLayout view)
    {
        layout.addView(view);
    }
    @Override
    public void onBackPressed()
    {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
}
