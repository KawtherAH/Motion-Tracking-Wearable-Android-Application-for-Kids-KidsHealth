package com.example.kidshealthv3;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.kidshealthv3.databinding.ActivityShowGoalsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
//import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.util.Calendar;

public class ShowGoalsActivity extends Activity{

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String UserID;
    ImageView run, jump, pushUp, food, water;

    GoalReceiver receiver = new GoalReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_goals);

        IntentFilter filter = new IntentFilter("com.example.kidshealthv3.EXTRA_ACTION");
        registerReceiver(receiver, filter);

        /*firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserID = firebaseUser.getUid();

        run = (ImageView) findViewById(R.id.runningIcon);
        jump = (ImageView) findViewById(R.id.jumpIcon);
        pushUp = (ImageView) findViewById(R.id.pushupIcon);
        food = (ImageView) findViewById(R.id.foodIcon);
        water = (ImageView) findViewById(R.id.waterIcon);

        reference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User UserProfile = snapshot.getValue(User.class);

                if( UserProfile != null){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


}