package com.example.gongtia.lifestyle.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.gongtia.lifestyle.R;

public class StepCounterActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mLinearAccelerometer;
    private final double mThreshold = 2.0;
    private int counter_Guestures = 0;

    //Previous positions
    private double last_x, last_y, last_z;
    private double now_x, now_y,now_z;
    private boolean mNotFirstTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        //Get sensor manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Get the default light sensor
        mLinearAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

    }

    private SensorEventListener mListener = new SensorEventListener() {


        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            //Get the acceleration rates along the y and z axes
            now_x = sensorEvent.values[0];
            now_y = sensorEvent.values[1];
            now_z = sensorEvent.values[2];

            if(mNotFirstTime){
                double dx = Math.abs(last_x - now_x);
                double dy = Math.abs(last_y - now_y);
                double dz = Math.abs(last_z - now_z);

                //Check if the values of acceleration have changed on any pair of axes
                if( (dx > mThreshold && dy > mThreshold) ||
                        (dx > mThreshold && dz > mThreshold)||
                        (dy > mThreshold && dz > mThreshold)){

                    //Change color of text view to some random color
                   counter_Guestures++;
                   if(counter_Guestures%2 == 1){
                       //To do start step counter
                       Toast.makeText(getBaseContext(), "Gesture 1", Toast.LENGTH_SHORT).show();
                   }
                   else {
                       //To to stop step counter
                       Toast.makeText(getBaseContext(), "Gesture 0", Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onResume() {
        super.onResume();
        if(mLinearAccelerometer!=null){
            mSensorManager.registerListener(mListener,mLinearAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLinearAccelerometer!=null){
            mSensorManager.unregisterListener(mListener);
        }
    }
}
