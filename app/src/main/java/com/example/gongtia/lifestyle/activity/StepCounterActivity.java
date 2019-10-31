package com.example.gongtia.lifestyle.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gongtia.lifestyle.R;
import com.example.gongtia.lifestyle.Room.StepCounterData;
import com.example.gongtia.lifestyle.Room.StepCounterTable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StepCounterActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mLinearAccelerometer;

    private Sensor mStepCounter;

    private final double mThreshold = 25.0;
    private int counter_Guestures = 0;

    //Previous positions
    private double last_x, last_y, last_z;
    private double now_x, now_y, now_z;
    private boolean mNotFirstTime;

    Timestamp lasttimestamp = new Timestamp(System.currentTimeMillis());
    long timeInterval = 1000;
    long lastUpdate = 0;

    public ArrayList<StepCounterData> mStepsList = new ArrayList<>();

    boolean isFirstTimeOfSC = true;
    int lastSC = 0;
    String currentDate;

    int stepsInt = 0;

    boolean isShake = false;


    //
    TextView todayDate, steps, stepsTag;


    public void initStepsList() {
        if (mStepsList.size()>0)
            mStepsList.clear();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... Voids) {

                List<StepCounterData> msls = WelcomeScreen.db.StepCounterDao().getAll();
                for (StepCounterData scd : msls) {
                    Log.e("SCA", "doInBackground: " + scd.date + " / " + scd.steps);
                    StepCounterData temp = new StepCounterData(scd.date, scd.steps);
                    mStepsList.add(temp);
                }
                return null;
            }
        }.execute();


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        //Get sensor manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Get the default light sensor
        mLinearAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        mStepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        steps = findViewById(R.id.stepcounterET);

        todayDate = findViewById(R.id.todayDate);


        currentDate = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());
        todayDate.setText("" + currentDate);

        initStepsList();

        RecyclerView rv = findViewById(R.id.stephisRV);
        LinearLayoutManager lm = new LinearLayoutManager(getBaseContext());
        rv.setLayoutManager(lm);
        StepCounterRVAdapter scRVA = new StepCounterRVAdapter(mStepsList);

        rv.setAdapter(scRVA);

    }

    private SensorEventListener mListener = new SensorEventListener() {


        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            long actualTime = sensorEvent.timestamp; //get the event's timestamp

            //Get the acceleration rates along the y and z axes
            now_x = sensorEvent.values[0];
            now_y = sensorEvent.values[1];
            now_z = sensorEvent.values[2];


            if (mNotFirstTime) {
//                    double dx = Math.abs(last_x - now_x);
//                    double dy = Math.abs(last_y - now_y);
//                    double dz = Math.abs(last_z - now_z);

                float ax = sensorEvent.values[0];
                float ay = sensorEvent.values[1];
                float az = sensorEvent.values[2];

//                    Log.e("SCA", "ax ay az: " + ax +"/"+ ay+ "/"+ az );

                if (Math.abs(ax) > mThreshold && ax * last_x < 0) {
                    last_x = ax;
                    isShake = true;
                } else if (Math.abs(ay) > mThreshold && ay * last_y < 0) {
                    last_y = ay;
                    isShake = true;

                } else if (Math.abs(az) > mThreshold && az * last_z < 0) {
                    last_z = az;
                    isShake = true;

                }
                //Check if the values of acceleration have changed on any pair of axes
                if (isShake) {
//                    //Change color of text view to some random color
                    counter_Guestures++;
                    if (counter_Guestures % 2 == 1) {
                        //To do start step counter
                        if (mStepCounter != null) {
                            Log.e("SCA", "SC start: " + "------------");
                            Toast.makeText(getBaseContext(), "Step Counter Started", Toast.LENGTH_SHORT).show();

                            mSensorManager.registerListener(mSCListener, mStepCounter, SensorManager.SENSOR_DELAY_UI);
                            isShake = false;
                        }


                    } else {
                        //To to stop step counter
                        if (mStepCounter != null && stepsInt != 0) {
                            Log.e("SCA", "SC end: " + "------------");
                            Toast.makeText(getBaseContext(), "Step Counter Ended", Toast.LENGTH_SHORT).show();
                            mSensorManager.unregisterListener(mSCListener);
                            isFirstTimeOfSC = true;
                            isShake = false;

                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... Voids) {

                                    StepCounterTable sct1 = new StepCounterTable();
                                    sct1.date = currentDate;
                                    sct1.steps = stepsInt;
                                    WelcomeScreen.db.StepCounterDao().insertStepCounterTable(sct1);


                                    return null;
                                }
                            }.execute();

                            initStepsList();

                            RecyclerView rv = findViewById(R.id.stephisRV);
                            LinearLayoutManager lm = new LinearLayoutManager(getBaseContext());
                            rv.setLayoutManager(lm);
                            StepCounterRVAdapter scRVA = new StepCounterRVAdapter(mStepsList);

                            rv.setAdapter(scRVA);

                        }
                    }

                }
            }
            last_x = now_x;
            last_y = now_y;
            last_z = now_z;
            mNotFirstTime = true;


        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private SensorEventListener mSCListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
//            Log.e("SCD", "onSensorChanged: " + "working" );
            if (isFirstTimeOfSC == true) {
                lastSC = (int) sensorEvent.values[0];
                isFirstTimeOfSC = false;
            }
//            Log.e("SCD", "last sc: " + lastSC);
//            Log.e("SCD", "last sc: " + (int) sensorEvent.values[0]);


            stepsInt = (int) sensorEvent.values[0] - lastSC;
            steps.setText("" + stepsInt);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (mLinearAccelerometer != null) {
//            Toast.makeText(this, "registered" , Toast.LENGTH_SHORT).show();
            mSensorManager.registerListener(mListener, mLinearAccelerometer, SensorManager.SENSOR_DELAY_UI);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLinearAccelerometer != null) {
//            Toast.makeText(this, "unregistered" , Toast.LENGTH_SHORT).show();

            mSensorManager.unregisterListener(mListener);
        }


    }
}
