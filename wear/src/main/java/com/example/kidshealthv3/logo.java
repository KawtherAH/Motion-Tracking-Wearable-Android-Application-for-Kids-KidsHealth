package com.example.kidshealthv3;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Calendar;

public class logo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        setAlarm(Calendar.getInstance().getTimeInMillis());

        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(logo.this, MainWear.class);
                startActivity(i);
                finish();
            }
        }, 2000);

    }

    private void setAlarm(long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyNotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() , AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
        //Toast.makeText(this, "Alarm is set",Toast.LENGTH_LONG);
    }
    @Override
    public void onStart() {
        super.onStart();

       /* Calendar calendar =Calendar.getInstance();
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                17,
                51,
                0
        );*/
        //setAlarm(calendar.getTimeInMillis());
    }
}