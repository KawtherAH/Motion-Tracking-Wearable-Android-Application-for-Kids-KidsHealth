package com.example.kidshealthv3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

public class MainWear extends Activity implements OnDataPointListener, SensorEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "StepCounter";
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
    private static final int MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 1;
    private static final int REQUEST_CODE =2 ;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SensorManager sensorManager;
    private Sensor mStepCountSensor;
    Handler handler;

    static final int REQUEST_OAUTH = 3;

    TextView steps, calo, dis, du;
    Button btn_ViewToday, btn_AddSteps, btn_UpdateSteps, btn_DeleteSteps;
    private Object TodayFragment;
    private Runnable runnable;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wear_main);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 0) {
                    Bundle stuff = msg.getData();

                }
                return true;
            }
        });

        steps = findViewById(R.id.textView);
        calo = findViewById(R.id.textView2);
        dis = findViewById(R.id.textView3);
        du = findViewById(R.id.textView4);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, mStepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);

        checkForUpdates();


        // When permissions are revoked the app is restarted so onCreate is sufficient to check for
        // permissions core to the Activity's functionality.

        // insertAndVerifySessionWrapper();
        //make sure we have permission to read and change the step data
        //note, if you doing other things like run with location data, you will fine_access permissions too.
        if (!hasOAuthPermission()) {
            requestOAuthPermission();
        }
    }


    private boolean hasOAuthPermission() {
        FitnessOptions fitnessOptions = getFitnessSignInOptions();
        return GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions);
    }



    float x1, x2, y1, y2;
    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if (x1 > x2) {
                    Intent i = new Intent(MainWear.this, goal.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {


        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        }


    }

    /**
     * Launches the Google SignIn activity to request OAuth permission for the user.
     */
    private void requestOAuthPermission() {

        FitnessOptions fitnessOptions = getFitnessSignInOptions();
        GoogleSignIn.requestPermissions(
                this,
                REQUEST_OAUTH,
                GoogleSignIn.getLastSignedInAccount(this),
                fitnessOptions);

        // For Android 10 And Above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestActivityRecognitionPermissions();
        }


    }

    private void requestActivityRecognitionPermissions() {


        if (ContextCompat.checkSelfPermission(this, "android.permission.ACTIVITY_RECOGNITION")
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{"android.permission.ACTIVITY_RECOGNITION"},
                    MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION);

        }

    }

    /**
     * Gets {@link FitnessOptions} in order to check or request OAuth permission for the user.
     */
    private FitnessOptions getFitnessSignInOptions() {
        return FitnessOptions.builder()

                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .build();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_OAUTH) {
//                displayLastWeeksData();
                displayTodayData();
            }
        }
    }

    private Task<DataSet> displayTodayData() {

        FitnessOptions fitnessOptions =FitnessOptions.builder()

                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)

                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)

                .build();

        Log.i(TAG, "Reading History API results for today of Steps");
        return Fitness.getHistoryClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        Log.i(TAG, "Reading History API results for today");
                        showDataSet(dataSet);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Failed to read DailyTotal for Steps. The Reason : "+e.getLocalizedMessage());
                    }
                });
    }

    /**
     * Creates and executes a insert Data request for 10,000 steps for today.
     */


    public void sendmessage(String logthis) {
        Bundle b = new Bundle();
        b.putString("logthis", logthis);
        Message msg = handler.obtainMessage();
        msg.setData(b);
        msg.arg1 = 1;
        msg.what = 0;
        handler.sendMessage(msg);

    }


    private void showDataSet(DataSet dataSet) {

        sendmessage("Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        int stepsCount = 0;

        for (DataPoint dp : dataSet.getDataPoints()) {
            //I'm using a handler here to cheat, since I'm not in the asynctask and can't call publishprogress.
            sendmessage("Data point:");
            sendmessage("\tType: " + dp.getDataType().getName());
            sendmessage("\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            sendmessage("\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                sendmessage("\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));

                stepsCount = Integer.parseInt(""+dp.getValue(field));
            }

        }

        int finalStepsCount = stepsCount;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                steps.setText("" + finalStepsCount +" Steps" );
//

            }
        });

    }


    public void readCalorieData() {

        FitnessOptions fitnessOptions = FitnessOptions.builder()

                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)

                .build();
        Fitness.getHistoryClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
                .readDailyTotal(DataType.TYPE_CALORIES_EXPENDED)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                int total =
                                        (int) (dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_CALORIES).asFloat());
                                Log.i(TAG, "Total calories = " + total);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        calo.setText("" + total +" Calo" );
//

                                    }
                                });

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "There was a problem getting the calorie count.", e);
                            }
                        });
    }

    public void readDurData() {

        FitnessOptions fitnessOptions = FitnessOptions.builder()

                .addDataType(DataType.TYPE_MOVE_MINUTES, FitnessOptions.ACCESS_READ)

                .build();
        Fitness.getHistoryClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
                .readDailyTotal(DataType.TYPE_MOVE_MINUTES)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                int total =
                                        (dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_DURATION).asInt());
                                Log.i(TAG, "Total Duration = " + total);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        du.setText("" + total +" min" );
//

                                    }
                                });










                            }




                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "There was a problem getting the calorie count.", e);
                            }
                        });
    }

    private void readDisData() {
        // Invoke the History API to fetch the data with the query
        FitnessOptions fitnessOptions = FitnessOptions.builder()

                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .build();
        Fitness.getHistoryClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
                .readDailyTotal(DataType.TYPE_DISTANCE_DELTA)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        int total =
                                (int) (dataSet.isEmpty()
                                        ? 0
                                        : dataSet.getDataPoints().get(0).getValue(Field.FIELD_DISTANCE).asFloat());
                        Log.i(TAG, "Total distance = " + total);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                dis.setText("" + total +" km" );
//

                            }
                        });


                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "There was a problem getting the dis count.", e);
                            }
                        });


    }

    public void onStart() {

        super.onStart();

        displayTodayData();
        readCalorieData();
        readDisData();
        readDurData();


    }

    @Override
    public void onDataPoint(DataPoint dataPoint) {


        for (final Field field : dataPoint.getDataType().getFields()) {
            final Value value = dataPoint.getValue(field);

//                 requireActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(requireContext(), value+" Steps",Toast.LENGTH_LONG).show();
//                        logger.setText("" + value+"Steps" );
////
//
//                    }
//                });

        }


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_STEP_COUNTER:
                steps.setText((int) sensorEvent.values[0] + " Steps");
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    private void checkForUpdates() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        int resultCode = availability.isGooglePlayServicesAvailable(this);
        if (resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
            availability.getErrorDialog(this, resultCode, REQUEST_CODE).show();
        }
    }

}