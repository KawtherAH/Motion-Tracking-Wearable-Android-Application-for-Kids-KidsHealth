package com.example.kidshealthv3;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataUpdateRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment implements OnDataPointListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "StepCounter";
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
    private static final int MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Handler handler;
    GoogleApiClient mGoogleApiClient;

    static final int REQUEST_OAUTH = 3;

    TextView steps,calo,dis,du;
    Button btn_ViewToday, btn_AddSteps, btn_UpdateSteps, btn_DeleteSteps;
    private Object TodayFragment;
    private Object MainActivity;

    public TodayFragment() {
        // Required empty public constructor
    }



    // TODO: Rename and change types and number of parameters
    public static TodayFragment newInstance(String param1, String param2) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View myView, @Nullable Bundle savedInstanceState) {

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 0) {
                    Bundle stuff = msg.getData();

                }
                return true;
            }
        });

        steps = myView.findViewById(R.id.stp);
        calo = myView.findViewById(R.id.calo);
        dis = myView.findViewById(R.id.dis);
        du = myView.findViewById(R.id.dur);


        // When permissions are revoked the app is restarted so onCreate is sufficient to check for
        // permissions core to the Activity's functionality.

        // insertAndVerifySessionWrapper();
        //make sure we have permission to read and change the step data
        //note, if you doing other things like run with location data, you will fine_access permissions too.
        if (!hasOAuthPermission()) {
            requestOAuthPermission();
        }



    }

    public void onResume(){
        super.onResume();


    }


    private boolean hasOAuthPermission() {
        FitnessOptions fitnessOptions = getFitnessSignInOptions();
        return GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(requireContext()), fitnessOptions);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED){


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
                GoogleSignIn.getLastSignedInAccount(requireContext()),
                fitnessOptions);

        // For Android 10 And Above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestActivityRecognitionPermissions();
        }


    }

    private void requestActivityRecognitionPermissions(){



        if (ContextCompat.checkSelfPermission(requireContext(),"android.permission.ACTIVITY_RECOGNITION")
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            ActivityCompat.requestPermissions((Activity) requireContext(),
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
        return Fitness.getHistoryClient(requireActivity(), GoogleSignIn.getAccountForExtension(requireContext(),fitnessOptions))
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

    private Task<DataReadResponse> displayLastWeeksData() {
        // Begin by creating the query.

        Log.i(TAG, "Reading History API results for last 7 days of Steps");
        //First create the DataReadRequest data.
        // Set a start and end time for our query, using a start time of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        //show the dates requested
        java.text.DateFormat dateFormat = DateFormat.getDateInstance();
        sendmessage("Range Start: " + dateFormat.format(startTime));
        sendmessage("Range End: " + dateFormat.format(endTime));

        //Check how many steps were walked and recorded in the last 7 days
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();


        // Now we can return the task object which will run.
        return Fitness.getHistoryClient(requireActivity(), GoogleSignIn.getLastSignedInAccount(requireContext()))
                //.readSession(readRequest)
                .readData(readRequest)
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        // Get a list of the sessions that match the criteria to check the result.
                        //Used for aggregated data
                        if (dataReadResponse.getBuckets().size() > 0) {
                            sendmessage("Number of buckets: " + dataReadResponse.getBuckets().size());
                            for (Bucket bucket : dataReadResponse.getBuckets()) {
                                List<DataSet> dataSets = bucket.getDataSets();
                                for (DataSet dataSet : dataSets) {
                                    showDataSet(dataSet);
                                }
                            }
                        }
                        //Used for non-aggregated data
                        else if (dataReadResponse.getDataSets().size() > 0) {
                            sendmessage("Number of returned DataSets: " + dataReadResponse.getDataSets().size());
                            for (DataSet dataSet : dataReadResponse.getDataSets()) {
                                showDataSet(dataSet);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        sendmessage("Failed to read DataResponse.");
                    }
                });

    }


    private void insertAndDisplayToday() {

        insertSteps().continueWithTask(new Continuation<Void, Task<DataSet>>() {
            @Override
            public Task<DataSet> then(@NonNull Task<Void> task) throws Exception {
                return displayTodayData();
            }
        });
    }


    /**
     * Creates and executes a insert Data request for 10,000 steps for today.
     */
    private Task<Void> insertSteps() {

        FitnessOptions fitnessOptions =FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        //First, create a new session and an insertion request.
        //Adds steps spread out evenly from start time to end time
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.HOUR_OF_DAY, -1);
        long startTime = cal.getTimeInMillis();

        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(requireActivity())
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setStreamName("Step Count")
                .setType(DataSource.TYPE_RAW)
                .build();

        int stepCountDelta = 10;  //we will add 10 steps for yesterday.


        /* before v17 it was this.
        DataSet dataSet = DataSet.create(dataSource);
        DataPoint point = dataSet.createDataPoint()
            .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        point.getValue(Field.FIELD_STEPS).setInt(stepCountDelta);
        dataSet.add(point);
*/
        //version 18+
        DataPoint point = DataPoint.builder(dataSource)
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                .setField(Field.FIELD_STEPS, stepCountDelta)
                .build();

        DataSet dataSet = DataSet.builder(dataSource)
                .add(point)
                .build();


        // Now insert the new dataset view the client.
        Log.i(TAG, "Inserting the session in the History API");
        return Fitness.getHistoryClient(requireActivity(), GoogleSignIn.getAccountForExtension(requireContext(),fitnessOptions))
                .insertData(dataSet)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // At this point, the session has been inserted and can be read.
                        sendmessage("dataSet of 10 steps inserted successfully!");
                    }


                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        sendmessage("There was a problem inserting the dataset: " +
                                e.getLocalizedMessage());
                    }
                });

    }





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

        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                steps.setText("" + finalStepsCount +" Steps" );
//

            }
        });

    }

    public void readCalorieData() {

        FitnessOptions fitnessOptions =FitnessOptions.builder()

                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .build();
        Fitness.getHistoryClient(requireActivity(), GoogleSignIn.getAccountForExtension(requireContext(),fitnessOptions))
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

                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        calo.setText("" + total +" cal" );
//

                                    }
                                });
                            }  })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "There was a problem getting the calorie count.", e);
                            }
                        });
    }
    public void readDurData() {

        FitnessOptions fitnessOptions =FitnessOptions.builder()

                .addDataType(DataType.TYPE_MOVE_MINUTES, FitnessOptions.ACCESS_READ)

                .build();
        Fitness.getHistoryClient(requireActivity(), GoogleSignIn.getAccountForExtension(requireContext(),fitnessOptions))
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

                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        du.setText("" + total +" min" );
//

                                    }
                                });
                            }  })
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
        FitnessOptions fitnessOptions =FitnessOptions.builder()

                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .build();
        Fitness.getHistoryClient(requireActivity(), GoogleSignIn.getAccountForExtension(requireContext(),fitnessOptions))
                .readDailyTotal(DataType.TYPE_DISTANCE_DELTA)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        int total =
                                (int) (dataSet.isEmpty()
                                        ? 0
                                        : dataSet.getDataPoints().get(0).getValue(Field.FIELD_DISTANCE).asFloat());
                        Log.i(TAG, "Total distance = " + total);

                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                dis.setText("" + total +" km" );
//

                            }
                        });
                    }  })
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
}