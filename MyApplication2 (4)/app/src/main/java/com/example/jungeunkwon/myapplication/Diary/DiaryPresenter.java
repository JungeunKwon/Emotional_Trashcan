package com.example.jungeunkwon.myapplication.Diary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.jungeunkwon.myapplication.Constants;
import com.example.jungeunkwon.myapplication.GoalPlanner.GoalPlannerActivity;
import com.example.jungeunkwon.myapplication.GoalPlanner.GoalPresenter;
import com.example.jungeunkwon.myapplication.MoodChart.MoodChartActivity;
import com.example.jungeunkwon.myapplication.MoodChart.MoodPresenter;
import com.example.jungeunkwon.myapplication.R;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.jungeunkwon.myapplication.Imageset.exifOrientationToDegrees;
import static com.example.jungeunkwon.myapplication.Imageset.rotate;


public class DiaryPresenter {
    Context context;
    DiaryView mView;
    DiaryDAO diaryDAO = new DiaryDAO();
    MoodPresenter moodPresenter;
    GoalPresenter goalPresenter;
    public DiaryPresenter(Context _context, DiaryView view)
    {
        mView = view;
        context = _context;
        moodPresenter = new MoodPresenter(_context);
        goalPresenter = new GoalPresenter(_context, null);
    }
    public boolean insert(DiaryEntity entity) {
        boolean result = false;
        try {

            Date now = new Date();
            String id = Constants.iddateFormat.format(now);
            entity.setID(id);
            diaryDAO.insert(entity);
            result = true;
        }catch (Exception ex)
        {
            result = false;
            Toast.makeText(context, context.getString(R.string.multi_alert_insert_error), Toast.LENGTH_SHORT).show();
        }return result;
    }
    public void getList(String date) {
        final String pDate = date.substring(0,7);
        LinearLayout.LayoutParams lp = null;
        List<String> Monthly = new ArrayList<>();

        Monthly = diaryDAO.SelectDateMothly(pDate);
        LinearLayout linearLayout = new LinearLayout(context);
        if(Monthly != null && Monthly.size()>0)
        {

            linearLayout.setOrientation(LinearLayout.VERTICAL);
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            lp.setMargins(10, 5, 10, 5);
            linearLayout.setLayoutParams(lp);
            for(int i = 0; i< Monthly.size(); i++)
            {
                List<DiaryEntity> list = new ArrayList<>();
                LinearLayout dayallLayout = new LinearLayout(context);
                dayallLayout.setOrientation(LinearLayout.VERTICAL);
                lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER;
                lp.setMargins(10, 10, 10, 10);
                dayallLayout.setLayoutParams(lp);

                LinearLayout dateLayout = new LinearLayout(context);
                dateLayout.setOrientation(LinearLayout.HORIZONTAL);
                lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dateLayout.setGravity(Gravity.CENTER);
                lp.gravity = Gravity.CENTER;
                lp.setMargins(10, 10, 10, 10);
                dateLayout.setLayoutParams(lp);

                TextView datetext = new TextView(context);
                lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , 100);
                lp.setMargins(10, 10, 10, 10);
                datetext.setLayoutParams(lp);
                datetext.setTextSize(20);
                datetext.setTypeface(Typeface.DEFAULT_BOLD);
                DateFormat Fromserver = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat ToUser = new SimpleDateFormat("EEE, MMM dd", Locale.US);
                try {
                    datetext.setText(ToUser.format(Fromserver.parse(Monthly.get(i))));
                }catch (Exception ex)
                {

                }
                ImageView dateemonji = new ImageView(context);
                ImageView dategoal = new ImageView(context);
                lp = new LinearLayout.LayoutParams(100 , 100);
                lp.setMargins(10, 10, 10, 10);
                dateemonji.setLayoutParams(lp);
                dategoal.setLayoutParams(lp);
                if (moodPresenter.selectDate(Monthly.get(i)) > 0) {
                    String result = moodPresenter.selectEmoji(Monthly.get(i)
                    );
                    int id = context.getResources().getIdentifier(result, "drawable", context.getPackageName());
                    Drawable drawable = context.getResources().getDrawable(id);
                    dateemonji.setImageDrawable(drawable);
                    dateemonji.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, MoodChartActivity.class );
                            context.startActivity(intent);
                        }
                    });

                } else {
                    dateemonji.setImageResource(0);
                }
                String icon = goalPresenter.getIcon(Monthly.get(i));
                if(icon != null && ! icon.equals(""))
                {
                    int id = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());
                    Drawable drawable = context.getResources().getDrawable(id);
                    dategoal.setImageDrawable(drawable);
                    dategoal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, GoalPlannerActivity.class );
                            context.startActivity(intent);
                        }
                    });
                }else
                {
                    dategoal.setImageResource(0);
                }
                dateLayout.addView(dateemonji);
                dateLayout.addView(datetext);
                dateLayout.addView(dategoal);
                dayallLayout.addView(dateLayout);
                //경계선..?
                list = diaryDAO.SelectDate(Monthly.get(i));
                if (!list.isEmpty() && list.size() > 0) {
                    for (int j = 0; j < list.size(); j++) {
                        try {
                            LinearLayout daylayout = new LinearLayout(context);
                            daylayout.setOrientation(LinearLayout.HORIZONTAL);
                            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            lp.gravity = Gravity.CENTER;
                            lp.setMargins(10, 10, 10, 10);
                            daylayout.setLayoutParams(lp);
                            View tmpView = new View(context);
                            if (list.get(j).getContent().contains(".jpg")) {
                                lp = new LinearLayout.LayoutParams(300, 300);
                                lp.weight = 8;
                                lp.gravity = Gravity.LEFT;
                                BitmapFactory.Options factory = new BitmapFactory.Options();

                                factory.inJustDecodeBounds = true;
                                factory.inPurgeable = true;
                                final String imagePath = list.get(j).getContent();
                                BitmapFactory.decodeFile(imagePath, factory);
                                int photoW = factory.outWidth;
                                int photoH = factory.outHeight;
                                int targetW = 240;
                                int targetH = 400;

                                //int scaleFacotor = Math.min(photoH/targetH, photoW/targetW);

                                factory.inJustDecodeBounds = false;
                                factory.inSampleSize = 2;
                                factory.inPurgeable = true;
                                File file = new File(imagePath);
                                if (!file.exists()) {
                                    Toast.makeText(context, context.getString(R.string.multi_alert_file_not_exist), Toast.LENGTH_SHORT).show();
                                    diaryDAO.delete(list.get(j).getID());
                                    continue;
                                }

                                final ImageView iv = new ImageView(context);
                                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, factory);
                                ExifInterface exif = new ExifInterface(imagePath);
                                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                                int exifDegree = exifOrientationToDegrees(exifOrientation);
                                bitmap = rotate(bitmap, exifDegree);
                                iv.setLayoutParams(lp);
                                iv.setBackgroundResource(R.drawable.frame);
                                iv.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 300, 300, true));
                                // }
                                final FrameLayout fl = new FrameLayout(context);
                                fl.setLayoutParams(lp);
                                fl.addView(iv);
                                fl.setTag(imagePath);
                                fl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, DiaryImageView.class);
                                        intent.putExtra("imagePath", imagePath);
                                        context.startActivity(intent);
                                    }
                                });
                                tmpView = fl;


                            } else {
                                final TextView textView = new TextView(context);
                                lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                                lp.weight = 8;
                                lp.gravity = Gravity.LEFT;
                                textView.setTextColor(Color.BLACK);
                                textView.setText(list.get(j).getContent());
                                textView.setLayoutParams(lp);
                                tmpView = textView;
                            }
                            lp = new LinearLayout.LayoutParams(30, 30);
                            lp.topMargin = 10;
                            lp.gravity = Gravity.END;
                            final String id = list.get(j).getID();
                            final ImageButton imageButton = new ImageButton(context);
                            imageButton.setBackgroundResource(R.drawable.exit);
                            imageButton.setLayoutParams(lp);
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                    alert.setTitle("DELETE");
                                    alert.setMessage("Do you want to delete data?");
                                    alert.setCancelable(false);
                                    alert.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            BtnDelete_Click(id, pDate);
                                        }
                                    });
                                    alert.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //do nothing
                                        }
                                    });
                                    alert.show();
                                }
                            });
                            daylayout.addView(tmpView);
                            daylayout.addView(imageButton);
                            dayallLayout.addView(daylayout);
                        } catch (Exception ex) {
                            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                linearLayout.addView(dayallLayout);
            }
        }
        mView.AddView(linearLayout);

        /*
        list = diaryDAO.SelectDate(date);
        if (moodPresenter.selectDate(date) > 0) {
            String result = moodPresenter.selectEmoji(date);
            int id = context.getResources().getIdentifier(result, "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);
            mView.setImageView(drawable);

        } else {
            mView.ClearImageView();
        }

        if (!list.isEmpty() && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                try {
                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.gravity = Gravity.CENTER;
                    lp.setMargins(10, 10, 10, 10);
                    linearLayout.setLayoutParams(lp);
                    View tmpView = new View(context);
                    if (list.get(i).getContent().contains(".jpg")) {
                        lp = new LinearLayout.LayoutParams(300, 300);
                        lp.weight = 8;
                        lp.gravity = Gravity.LEFT;
                        BitmapFactory.Options factory = new BitmapFactory.Options();
                        factory.inSampleSize = 4;
                        factory.inJustDecodeBounds = false;
                        factory.inPurgeable = true;
                        final String imagePath = list.get(i).getContent();
                        File file = new File(imagePath);
                        if (!file.exists()) {
                            Toast.makeText(context, "File is not exist", Toast.LENGTH_SHORT).show();
                            diaryDAO.delete(list.get(i).getID());
                            continue;
                        }
                        final ImageView iv = new ImageView(context);
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, factory);
                        ExifInterface exif = new ExifInterface(imagePath);
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        bitmap = rotate(bitmap, exifDegree);
                        iv.setLayoutParams(lp);
                        iv.setBackgroundResource(R.drawable.frame);
                        iv.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100, 100, true));
                        // }
                        final FrameLayout fl = new FrameLayout(context);
                        fl.setLayoutParams(lp);
                        fl.addView(iv);
                        fl.setTag(imagePath);
                        fl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, DiaryImageView.class);
                                intent.putExtra("imagePath", imagePath);
                                context.startActivity(intent);
                            }
                        });
                        tmpView = fl;


                    } else {
                        final TextView textView = new TextView(context);
                        lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp.weight = 8;
                        lp.gravity = Gravity.LEFT;
                        textView.setTextColor(Color.BLACK);
                        textView.setText(list.get(i).getContent());
                        textView.setLayoutParams(lp);
                        tmpView = textView;
                    }
                    lp = new LinearLayout.LayoutParams(30, 30);
                    lp.topMargin = 10;
                    lp.gravity = Gravity.END;
                    final String id = list.get(i).getID();
                    final ImageButton imageButton = new ImageButton(context);
                    imageButton.setBackgroundResource(R.drawable.exit);
                    imageButton.setLayoutParams(lp);
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle("DELETE");
                            alert.setMessage("Do you want to delete data?");
                            alert.setCancelable(false);
                            alert.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    BtnDelete_Click(id, pDate);
                                }
                            });
                            alert.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            });
                            alert.show();
                        }
                    });
                    linearLayout.addView(tmpView);
                    linearLayout.addView(imageButton);
                    mView.AddView(linearLayout);
                } catch (Exception ex) {
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }*/


    }
    public void BtnDelete_Click(String id, String Date)
    {
        int result;
        result = diaryDAO.delete(id);
        mView.ClearListView();
        getList(Date);

    }

}
