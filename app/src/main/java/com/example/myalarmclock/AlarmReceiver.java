package com.example.myalarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("We are in the receiver.", "Yay!");

        // fetch extra strings from the intent
        // tells the app whether the user pressed the alarm on button or the alarm off button
        String get_your_string = intent.getExtras().getString("extra");

        Log.e("What is the key? ", get_your_string);

        String alarmDate1 = intent.getStringExtra("alarmDate");
        int alarmID1 = intent.getIntExtra("alarmID", -1);

        Log.e("alarm_logs", "Read ID in receiver: " + alarmID1);
        Log.e("alarm_logs", "Read Date in receiver: " + alarmDate1);

        // create an intent to the ringtone service
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        // pass the extra string from Receiver to the Ringtone Playing Service
        service_intent.putExtra("extra", get_your_string);

        // start the ringtone service
        context.startService(service_intent);
    }
}
