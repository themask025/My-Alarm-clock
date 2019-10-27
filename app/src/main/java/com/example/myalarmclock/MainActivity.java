package com.example.myalarmclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
                        final String rowIndex = cur.getString(1);

                        TableRow row = new TableRow(this);
                        row.setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

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
                        row.setLongClickable(true);
                        row.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                String id = rowIndex;
                                setIntent(getIntent().putExtra("rowID", rowIndex));
                                registerForContextMenu(v);
                                openContextMenu(v);
                                unregisterForContextMenu(v);
                                return true;
                            }
                        });

                    } while (cur.moveToNext());
                }
            }
            else{
            noAlarmsMessage.setVisibility(View.VISIBLE);
            }
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.context_menu_title);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_alarms_table, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent = getIntent();
        String id = intent.getStringExtra("rowID");
        switch (item.getItemId()) {
            case R.id.edit:
                Toast.makeText(this, "Pressed \"Edit\"", Toast.LENGTH_LONG).show();
                return true;
            case R.id.delete:
                Toast.makeText(this, String.valueOf(item.getItemId()), Toast.LENGTH_LONG).show();
                myDB.removeData (id);
                populateAlarmsTable();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, AddAlarmActivity.class);
        startActivity(intent);
    }
}
