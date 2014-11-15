package com.senz.service;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.senz.utils.L;

/***********************************************************************************************************************
 * @ClassName:   SensorInfo
 * @Author:      Woodie
 * @CreateAt:    Sat, Nov 15, 2014
 * @Description: It's a Sensor manager.
 ***********************************************************************************************************************/
public class SensorInfo implements SensorEventListener {

    // Sensor Manager
    private SensorManager mSensorManager;
    // Sensor
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private Sensor mLight;
    // The value of Gyroscope.
    private float GyroValues[] = new float[]{0,0,0};
    // The value of Accelerometer.
    private float AcceValues[] = new float[]{0,0,0};
    // The value of Light
    private float LightValues = 0;

    public SensorInfo(Context ctx) {
        if (ctx == null) {
            //L.d("yes");
        }
        // Get Sensor's Services.
        mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGyroscope     = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mLight         = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        // Check Accelerometer is exist.
        if (mAccelerometer != null){
            // Success! There's a accelerometer.
            L.i("There's a accelerometer!");
            // Sensor start listening.
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            // Failure! No accelerometer.
            L.e("Failure! No accelerometer!");
        }

        // Check Gyroscope is exist.
        if (mGyroscope != null){
            // Success! There's a mGyroscope.
            L.i("There's a mGyroscope!");
            // Sensor start listening.
            mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            // Failure! No mGyroscope.
            L.e("Failure! No mGyroscope!");
        }

        // Check Light is exist.
        if (mLight != null){
            // Success! There's a Light.
            L.i("There's a Light!");
            // Sensor start listening.
            mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            // Failure! No Light.
            L.e("Failure! No Light!");
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
