package com.example.myalarmclock;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.view.View;


public class TimerActivity extends Activity {
    ProgressBar progressBar;
    Button start_timer,stop_timer;
    MyCountDownTimer myCountDownTimer;
    NumberPicker hour_picker;
    NumberPicker minute_picker;
    NumberPicker second_picker;

    MediaPlayer media_song2;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        context = this;

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        start_timer=(Button)findViewById(R.id.button);
        stop_timer=(Button)findViewById(R.id.button2);
        hour_picker=(NumberPicker)findViewById(R.id.hoursPicker);

        //populating hours numberpicker with values 0-9
        String[] nums = new String[10];
        for(int i=0; i<nums.length; i++)
            nums[i] = Integer.toString(i);
        hour_picker.setMinValue(0);
        hour_picker.setMaxValue(9);
        hour_picker.setWrapSelectorWheel(false);
        hour_picker.setDisplayedValues(nums);
        hour_picker.setValue(0);

        //populating minute and second numberpickers with values 0-59
        minute_picker=(NumberPicker)findViewById(R.id.minutesPicker);
        second_picker=(NumberPicker)findViewById(R.id.secondsPicker);

        String[] nums2 = new String[60];
        for(int i=0; i<nums2.length; i++)
            nums2[i] = Integer.toString(i);
        minute_picker.setMinValue(0);
        minute_picker.setMaxValue(59);
        minute_picker.setWrapSelectorWheel(false);
        minute_picker.setDisplayedValues(nums2);
        minute_picker.setValue(0);

        second_picker.setMinValue(0);
        second_picker.setMaxValue(59);
        second_picker.setWrapSelectorWheel(false);
        second_picker.setDisplayedValues(nums2);
        second_picker.setValue(0);



        start_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                myCountDownTimer = new MyCountDownTimer(
                        (hour_picker.getValue()*60*60*1000 +
                        minute_picker.getValue()*60*1000 +
                        second_picker.getValue()*1000), 1000);
                myCountDownTimer.start();

            }
        });

        stop_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myCountDownTimer.cancel();

            }
        });

    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            int progress = (int) (millisUntilFinished/1000);

            progressBar.setProgress(progressBar.getMax()-progress);
        }

        @Override
        public void onFinish() {
            media_song2 = MediaPlayer.create(context, R.raw.timer_ringtone);
            media_song2.start();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    media_song2.stop();
                }
            }, 20000);

        }
    }
}

