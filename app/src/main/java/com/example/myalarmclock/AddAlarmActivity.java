package com.example.myalarmclock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.widget.Toast.makeText;

public class AddAlarmActivity extends AppCompatActivity {

    DatabaseHelper myDB;

    AlarmManager alarmManager;
    private  PendingIntent pending_intent;

    private TimePicker alarmTimePicker;
    private Button btnSetAlarm, btnCancel;
    private EditText setAlarmName;

    private AlarmReceiver alarm;

    AddAlarmActivity inst;
    Context context;

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        setAlarmName = (EditText)this.findViewById(R.id.setAlarmName);

        myDB = new DatabaseHelper(this);

        alarmTimePicker = (TimePicker)findViewById(R.id.timepicker);

        //functionality of Cancel button
        btnCancel = (Button)this.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        //functionality of Set button
        btnSetAlarm = (Button)this.findViewById(R.id.btnSetAlarm);
        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newDate = (String.format("%02d:%02d", alarmTimePicker.getHour(), alarmTimePicker.getMinute()));
                String newName = setAlarmName.getText().toString();
                if (newName.equals(getResources().getString(R.string.setAlarmName)))
                    newName = getResources().getString(R.string.defaultAlarmName);

                if(setAlarmName.length()!= 0){
                    /*
                    if (cur.moveToFirst()) {
                        do {
                            if (newDate.equals(cur.getString(1))) {
                                Toast.makeText(AddAlarmActivity.this, "Alarm already set.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } while (cur.moveToNext());
                    }
                    */
                    if (myDB.addData(newDate, newName))
                        showDialog(AddAlarmActivity.this);
                    else
                        Toast.makeText(AddAlarmActivity.this, "Something went wrong with DB :(.", Toast.LENGTH_LONG).show();
                }
                else
                    makeText(AddAlarmActivity.this, "Invalid alarm name!", Toast.LENGTH_LONG).show();

            }
        });
    }


    public void showDialog(Activity dialog_layout){
        final Dialog dialog = new Dialog(dialog_layout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_ready);

        Button dialogButton1 = (Button) dialog.findViewById(R.id.btnCloseDialog);
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}



