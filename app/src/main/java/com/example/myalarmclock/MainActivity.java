package com.example.myalarmclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TableLayout table;
    DatabaseHelper myDB;
    TextView noAlarmsMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        table = (TableLayout)this.findViewById(R.id.alarmsTableView);
        myDB = new DatabaseHelper(this);
        noAlarmsMessage = (TextView)this.findViewById(R.id.textViewNoAlarms);

        populateAlarmsTable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateAlarmsTable();
    }

    public void populateAlarmsTable() {
        table.removeAllViews();
        Cursor cur = myDB.getData();
        try {
            if (cur.getCount() != 0) {
                noAlarmsMessage.setVisibility(View.INVISIBLE);
                if (cur.moveToFirst()) {
                    do {
                        int rows = cur.getCount();
                        int cols = cur.getColumnCount();

                            TableRow row = new TableRow(this);
                            row.setLayoutParams(new TableRow.LayoutParams(
                                    TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));

                            // inner for loop
                            for (int j = 1; j < cols; j++) {

                                TextView tv = new TextView(this);
                                tv.setLayoutParams(new TableRow.LayoutParams(
                                        TableLayout.LayoutParams.WRAP_CONTENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT));
                                tv.setGravity(Gravity.CENTER);
                                if(j==1)
                                    tv.setTextSize(40);
                                else
                                    tv.setTextSize(20);
                                tv.setPadding(15, 10, 0, 10);
                                tv.setText(cur.getString(j));
                                row.addView(tv);

                            }
                            table.addView(row);

                    } while (cur.moveToNext());
                }
            }
            else{
            noAlarmsMessage.setVisibility(View.VISIBLE);
            }
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        for(int i = 0, j = table.getChildCount(); i < j; i++) {

            if(i!=0){
                View view = table.getChildAt(i-1);
                View view1 = table.getChildAt(i);
                TableRow rowPrevious = (TableRow) view;
                TableRow rowCurrent = (TableRow) view1;
                if( rowCurrent == rowPrevious ) {
                    table.removeView(rowCurrent);
                }
            }

        }
    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, AddAlarmActivity.class);
        startActivity(intent);
    }
}
