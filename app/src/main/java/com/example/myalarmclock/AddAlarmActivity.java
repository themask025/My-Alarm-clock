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
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.makeText;

public class AddAlarmActivity extends AppCompatActivity {

    DatabaseHelper myDB;

    private TimePicker alarmTimePicker;
    private Button btnSetAlarm, btnCancel;
    private EditText setAlarmName;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    Calendar calendar;

    String extra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        //initializing the database helper
        myDB = new DatabaseHelper(this);

        //initializing the time picker
        alarmTimePicker = (TimePicker)findViewById(R.id.timepicker);

        //initializing the Alarm name input field
        setAlarmName = (EditText)this.findViewById(R.id.setAlarmName);


        //preparing the sleep calculator expandable list

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expLVTimes);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);


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

                //setting date and name of the alarm
                String newDate = (String.format("%02d:%02d", alarmTimePicker.getHour(), alarmTimePicker.getMinute()));
                String newName = setAlarmName.getText().toString();
                if (newName.equals(getResources().getString(R.string.setAlarmName)))
                    newName = getResources().getString(R.string.defaultAlarmName);

                //adding the alarm to the database
                if(setAlarmName.length()!= 0){

                    if (myDB.addData(newDate, newName)){
                        //returning result if the activity is called as an editor
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
                        showDialog(AddAlarmActivity.this);
                    }
                    else
                        Toast.makeText(AddAlarmActivity.this, "Something went wrong with DB :(.", Toast.LENGTH_LONG).show();
                }
                else
                    makeText(AddAlarmActivity.this, "Invalid alarm name!", Toast.LENGTH_LONG).show();

            }
        });

        // action if time suggestion from the sleep calculator suggestions is clicked
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            //changes the timepicker and collapses the list
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String[] alarmDate = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).split(":");
                final int hour = Integer.parseInt(alarmDate[0]);
                final int minute = Integer.parseInt(alarmDate[1]);
                alarmTimePicker.setHour(hour);
                alarmTimePicker.setMinute(minute);
                expListView.collapseGroup(0);
                return false;
            }
        });
    }

    //handles the "Sleep well" dialog, notifying the user that everything is done
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

    //calculates and displays the time suggestions for optimal sleep
    //a.k.a sleep calculator
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        calendar = Calendar.getInstance();
        calendar.getTime();


        // Adding ExpandableListView,
        // a.k.a list of alarm recommendations
        // header data
        listDataHeader.add(getResources().getString(R.string.alarm_recommendation_header));

        // Adding child data
        List<String> recommendations = new ArrayList<String>();

        //time for falling asleep
        calendar.add(Calendar.MINUTE, 15);

        //time for the first cycle
        calendar.add(Calendar.HOUR, 1);
        calendar.add(Calendar.MINUTE, 30);

        //calculating and adding 5 cycles
        for(int i = 0; i<5; i++){
            calendar.add(Calendar.HOUR, 1);
            calendar.add(Calendar.MINUTE, 30);
            recommendations.add(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        }

        //finally adding the data
        listDataChild.put(listDataHeader.get(0), recommendations); // Header, Child data

    }


}




