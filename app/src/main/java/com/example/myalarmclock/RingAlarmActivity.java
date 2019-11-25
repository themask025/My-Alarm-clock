package com.example.myalarmclock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class RingAlarmActivity extends Activity {

    DatabaseHelper myDB;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_alarm);
        Button btnFinish = (Button)this.findViewById(R.id.btnDismiss);
        context = this;
        myDB = new DatabaseHelper(this);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stops the ringtone
                Intent stopAlarm = new Intent(context, AlarmReceiver.class);
                stopAlarm.putExtra("extra", "alarm off");
                sendBroadcast(stopAlarm);

                //removes the alarm since it's finished
                myDB.removeLastEntry();

                //notifies the user
                Toast.makeText(RingAlarmActivity.this, "Alarm dismissed successfully", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
