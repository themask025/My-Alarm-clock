package com.example.myalarmclock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.Calendar;

public class RingAlarmActivity extends Activity {

    DatabaseHelper myDB;
    Context context;
    Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_alarm);
        Button btnFinish = (Button)this.findViewById(R.id.btnDismiss);
        TextView displayTime = (TextView)this.findViewById(R.id.textView);
        context = this;
        myDB = new DatabaseHelper(this);
        calendar = Calendar.getInstance();
        //displayTime.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stops the ringtone
                Intent stopAlarm = new Intent(context, AlarmReceiver.class);
                stopAlarm.putExtra("extra", "alarm off");
                sendBroadcast(stopAlarm);

                //removes the alarm from the database since it's finished
                myDB.removeFirstEntry();

                //notifies the user
                Toast.makeText(RingAlarmActivity.this, "Alarm dismissed successfully", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
