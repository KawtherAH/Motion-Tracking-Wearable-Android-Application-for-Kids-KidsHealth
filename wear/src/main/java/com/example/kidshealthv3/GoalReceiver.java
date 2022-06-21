package com.example.kidshealthv3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class GoalReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.example.kidshealthv3.EXTRA_ACTION")){
            String Text1 = intent.getStringExtra("jump");

            Toast.makeText(context, Text1, Toast.LENGTH_LONG).show();

        }
    }
}
