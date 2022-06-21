package com.example.kidshealthv3;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class goal extends Activity implements SensorEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SensorManager sensorManager = null;
    private Boolean running = false;
    private float totalsteps = 0f;
    private float previousTotalSteps = 0f;
    private TextView tv1;
    private TextView tv2;
    private CircularProgressBar circularProgressBar;
    private float previousEventValue = 0f;
    private Boolean firstTime = true;
    private Boolean completed = false;
    private Button resetButton;
    private String [] stepGoals;
    float [] goalFloat;
    private int checked = 0;
    private MediaPlayer mp;
    SharedPreferences preferences;
    private final String FILE_NAME ="mysteps.txt";
    private float prevEventValue;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *


     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_goal);
        Log.d( "~waqqas", "onCreate: fragment1" );

        sensorManager = (SensorManager) this.getSystemService( Context.SENSOR_SERVICE );

        //Toast.makeText(getActivity(), "This is fragment 1", Toast.LENGTH_SHORT).show();

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        circularProgressBar = findViewById(R.id.circularProgressBar);

        loadData();

//        stepGoals.add("1000 steps");
        goalFloat = new float[]{1000f, 2500f, 5000f, 10000f};
        stepGoals = new String[]{"1000 steps", "2500 steps", "5000 steps", "10000 steps"};

        Log.d("~waqqas", "onCreateView: fragment1");






    }

    private void loadData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        Log.d("~waqqas", "loadData: "+sharedPreferences.getFloat("mysteps", 0f));
        totalsteps = sharedPreferences.getFloat("mysteps", 0f);
        float tempMax = sharedPreferences.getFloat("maxprogress", 1000f);
        tv2.setText("/"+(int)tempMax);
        circularProgressBar.setProgressMax(sharedPreferences.getFloat("maxprogress", 1000f));
        checked = (int) sharedPreferences.getFloat("checked", 0f);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("~waqqas", "onSensorChanged: onResume");
        running = true;
        //previousTotalSteps = totalsteps;

        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor == null){
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show();
        }
        else{
            sensorManager.registerListener((SensorEventListener) this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("~waqqas", "onPause: ");
        SharedPreferences sharedPreferences = this.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("mysteps", totalsteps);
        editor.putFloat("checked", checked);
        editor.putFloat("maxprogress", circularProgressBar.getProgressMax());
        editor.apply();







    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("~waqqas", "onSensorChanged: "+totalsteps);
        if(running) {

            //totalsteps = event.values[0];
            Log.d("~waqqas", "onSensorChanged: "+event.values[0]);
            if (event.values[0] > 0) {
                if(firstTime){
                    firstTime = false;
                    prevEventValue = event.values[0];
                    Log.d("~waqqas", "onSensorChanged: prevEventValue11 "+prevEventValue);
                    tv1.setText(""+Math.round( prevEventValue ));
                }else{
                    totalsteps += 1;
                    prevEventValue = event.values[0];
                    previousEventValue = event.values[0];
                    Log.d("~waqqas", "onSensorChanged: totalsteps "+totalsteps);
                    Log.d("~waqqas", "onSensorChanged: prevEventValue "+prevEventValue);

                }
            }
            //circularProgressBar.setProgressMax(20f); //for debugging purpose
            if((int)circularProgressBar.getProgressMax()>=(int)totalsteps){
                int currentSteps = (int)totalsteps; //- (int)previousTotalSteps;
                //tv1.setText(""+currentSteps);
                //tv1.setText(currentSteps);
                Log.d("~waqqas", "onSensorChanged: currentsteps"+currentSteps);



                circularProgressBar.setProgressWithAnimation((float)prevEventValue);
            }
            else {
                if(!completed) {
                    completed = true;
                    mp.start();
                    String msg = "";
                    if(circularProgressBar.getProgressMax()==10000f){
                        msg = "Research says as few as 6000 steps per day correlate with a lower death rate.";
                    }
                    else {
                        msg = "How about setting a higher goal next time?";
                    }
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this).setTitle("Set Goal")
                            .setTitle("Goal Accomplished")
                            .setMessage(msg + " Please reset your goal and keep going." )
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    materialAlertDialogBuilder.show();

                }
            }



        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void save(){
        if(tv1.getText().toString().equals("0")){

        }
        else {
            String currentTotalSteps = "\n" + getDateTime() + ": " + tv1.getText().toString() +" steps \n";
            FileOutputStream fos = null;
//        String previousText = readFromFile();
//        String newText = currentTotalSteps;

            try {
                fos = this.openFileOutput(FILE_NAME, Context.MODE_APPEND);
                fos.write(currentTotalSteps.getBytes());
                //Toast.makeText(getActivity(), "Saved to "+getActivity().getFilesDir()+"/"+FILE_NAME, Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(fos!=null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", new Locale("en", "BD"));
        Date date = new Date();
        return dateFormat.format(date);
    }


}
