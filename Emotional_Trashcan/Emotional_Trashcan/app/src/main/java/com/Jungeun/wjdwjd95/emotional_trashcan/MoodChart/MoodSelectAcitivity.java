package com.Jungeun.wjdwjd95.emotional_trashcan.MoodChart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.Jungeun.wjdwjd95.emotional_trashcan.R;

public class MoodSelectAcitivity extends Activity {
    ImageButton happy;
    ImageButton confused;
    ImageButton inlove;
    ImageButton sad;
    ImageButton smiling;
    ImageButton happy2;
    ImageButton quiet;
    ImageButton smile;
    ImageButton suspicious;
    ImageButton tongueout;
    ImageButton wink;
    ImageButton kissing;
    ImageButton embarrassed;
    ImageButton bored;
    ImageButton bored2;
    ImageButton secret;
    ImageButton crying;
    ImageButton crying2;
    ImageButton surprised;
    ImageButton angry;
    ImageButton nothing;
    String Tag = "";
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mood_select_acitivity);
        intent = getIntent();
        Tag = intent.getStringExtra("Tag");
        happy = (ImageButton)findViewById(R.id.activity_mood_select_happy);
        confused = (ImageButton)findViewById(R.id.activity_mood_select_confused);
        inlove = (ImageButton)findViewById(R.id.activity_mood_select_inlove);
        sad = (ImageButton)findViewById(R.id.activity_mood_select_sad);
        smiling = (ImageButton)findViewById(R.id.activity_mood_select_smiling);
        happy2 = (ImageButton)findViewById(R.id.activity_mood_select_happy2);
        quiet = (ImageButton)findViewById(R.id.activity_mood_select_quiet);
        smile = (ImageButton)findViewById(R.id.activity_mood_select_smile);
        suspicious = (ImageButton)findViewById(R.id.activity_mood_select_suspicious);
        tongueout = (ImageButton)findViewById(R.id.activity_mood_select_tongueout);
        wink = (ImageButton)findViewById(R.id.activity_mood_select_wink);
        kissing = (ImageButton)findViewById(R.id.activity_mood_select_kissing);
        embarrassed = (ImageButton)findViewById(R.id.activity_mood_select_embarrassed);
        bored = (ImageButton)findViewById(R.id.activity_mood_select_bored);
        bored2 = (ImageButton)findViewById(R.id.activity_mood_select_bored2);
        secret = (ImageButton)findViewById(R.id.activity_mood_select_secret);
        crying = (ImageButton)findViewById(R.id.activity_mood_select_crying);
        crying2 = (ImageButton)findViewById(R.id.activity_mood_select_crying2);
        surprised = (ImageButton)findViewById(R.id.activity_mood_select_surprised);
        angry = (ImageButton)findViewById(R.id.activity_mood_select_angry);
        nothing = (ImageButton)findViewById(R.id.activity_mood_select_nothing);
        intent = new Intent();
        intent.putExtra("Tag",Tag);
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.activity_mood_select_happy:
                        intent.putExtra("result", "happy");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_confused:
                        intent.putExtra("result", "confused");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_inlove:
                        intent.putExtra("result", "inlove");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_sad:
                        intent.putExtra("result", "sad");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_smiling:
                        intent.putExtra("result", "smiling");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_happy2:
                        intent.putExtra("result", "happy2");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_quiet:
                        intent.putExtra("result", "quiet");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_smile:
                        intent.putExtra("result", "smile");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_suspicious:
                        intent.putExtra("result", "suspicious");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_tongueout:
                        intent.putExtra("result", "tongueout");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_wink:
                        intent.putExtra("result", "wink");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_kissing:
                        intent.putExtra("result", "kissing");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_embarrassed:
                        intent.putExtra("result", "embarrassed");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_bored:
                        intent.putExtra("result", "bored");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_bored2:
                        intent.putExtra("result", "bored2");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_secret:
                        intent.putExtra("result", "secret");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_crying:
                        intent.putExtra("result", "crying");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_crying2:
                        intent.putExtra("result", "crying2");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_surprised:
                        intent.putExtra("result", "surprised");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_angry:
                        intent.putExtra("result", "angry");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case R.id.activity_mood_select_nothing:
                        intent.putExtra("result", "nothing");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                }
            }
        };
        happy.setOnClickListener(onClickListener);
        confused.setOnClickListener(onClickListener);
        inlove.setOnClickListener(onClickListener);
        sad.setOnClickListener(onClickListener);
        smiling.setOnClickListener(onClickListener);
        happy2.setOnClickListener(onClickListener);
        quiet.setOnClickListener(onClickListener);
         smile.setOnClickListener(onClickListener);
         suspicious.setOnClickListener(onClickListener);
         tongueout.setOnClickListener(onClickListener);
         wink.setOnClickListener(onClickListener);
         kissing.setOnClickListener(onClickListener);
         embarrassed.setOnClickListener(onClickListener);
         bored.setOnClickListener(onClickListener);
         bored2.setOnClickListener(onClickListener);
         secret.setOnClickListener(onClickListener);
         crying.setOnClickListener(onClickListener);
         crying2.setOnClickListener(onClickListener);
         surprised.setOnClickListener(onClickListener);
         angry.setOnClickListener(onClickListener);
        nothing.setOnClickListener(onClickListener);


    }

}
