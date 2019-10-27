package com.example.myalarmclock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import static android.widget.Toast.makeText;

public class AddAlarmActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    TimePicker picker;
    Button btnSetAlarm, btnCancel;
    EditText setAlarmName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        myDB = new DatabaseHelper(this);
        picker=(TimePicker)findViewById(R.id.timepicker);
        picker.setIs24HourView(true);
        btnSetAlarm = (Button)this.findViewById(R.id.btnSetAlarm);
        btnCancel = (Button)this.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        setAlarmName = (EditText)this.findViewById(R.id.setAlarmName);


        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newDate = (String.format("%02d:%02d", picker.getHour(), picker.getMinute()));
                String newName = setAlarmName.getText().toString();
                if (newName.equals("@string/setAlarmName"))
                    newName = "@string/defaultAlarmName";

                if(setAlarmName.length()!= 0){
                    boolean insertData = myDB.addData(newDate, newName);
                    if(insertData)
                        showDialog(AddAlarmActivity.this);
                    else
                        Toast.makeText(AddAlarmActivity.this, "Something went wrong with DB :(.", Toast.LENGTH_LONG).show();

                }else {
                    makeText(AddAlarmActivity.this, "Invalid alarm name!", Toast.LENGTH_LONG).show();
                }
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



