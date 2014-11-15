package com.senz.service;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.senz.utils.L;
//import com.senz.utils.Writer;

/***********************************************************************************************************************
 * @ClassName:   SensorInfo
 * @Author:      Woodie
 * @CreateAt:    Sat, Nov 15, 2014
 * @Description: It's a Sensor manager.
 ***********************************************************************************************************************/
public class SensorInfo{

    // Sensor Manager
    private SensorManager mSensorManager;
    // Sensor
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private Sensor mLight;
    // The value of Gyroscope.
    public float GyroValues[] = new float[]{0,0,0};
    // The value of Accelerometer.
    public float AcceValues[] = new float[]{0,0,0};
    // The value of Light
    public float LightValues = 0;

    private SensorHandler sensorHandler;

    //Writer
    //private Writer gyroWriter = null;
    //private Writer acceWriter = null;
    //private Writer lightWriter = null;

    private SensorEventListener sensorEventListener = new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Get the sensors' data.
            // HIGH FREQUENCY
            if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
            {
                // Acceleration force along the x axis(Excluding gravity, m/s2)
                AcceValues[0] = event.values[0];
                // Acceleration force along the y axis(Excluding gravity, m/s2)
                AcceValues[1] = event.values[1];
                // Acceleration force along the z axis(Excluding gravity, m/s2)
                AcceValues[2] = event.values[2];
                // Save the accelerometers' data to local file.
                HandleAcceData();
            }
            // HIGH FREQUENCY
            else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
            {
                // Gyroscope force along the x axis(with drift compensation, rad/s)
                GyroValues[0] = event.values[0];
                // Gyroscope force along the y axis(with drift compensation, rad/s)
                GyroValues[1] = event.values[1];
                // Gyroscope force along the z axis(with drift compensation, rad/s)
                GyroValues[2] = event.values[2];
                // Save the Gyroscopes' data to local file.
                HandleGyroData();
            }
            // LOW FREQUENCY
            else if(event.sensor.getType() == Sensor.TYPE_LIGHT)
            {
                // Illuminance(lx)
                LightValues = event.values[0];
                HandleLightData();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public SensorInfo(Context ctx, SensorHandler ltn) {
        if (ctx == null) {
            L.e("The context of SensorInfo is null.");
        }
        else {
            sensorHandler = ltn;
            // Get Sensor's Services.
            mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            // Check Accelerometer is exist.
            if (mAccelerometer != null) {
                // Success! There's a accelerometer.
                L.i("There's a accelerometer!");
                // Sensor start listening.
                mSensorManager.registerListener(sensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
            else {
                // Failure! No accelerometer.
                L.e("Failure! No accelerometer!");
            }

            // Check Gyroscope is exist.
            if (mGyroscope != null) {
                // Success! There's a mGyroscope.
                L.i("There's a mGyroscope!");
                // Sensor start listening.
                mSensorManager.registerListener(sensorEventListener, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
            }
            else {
                // Failure! No mGyroscope.
                L.e("Failure! No mGyroscope!");
            }

            // Check Light is exist.
            if (mLight != null) {
                // Success! There's a Light.
                L.i("There's a Light!");
                // Sensor start listening.
                mSensorManager.registerListener(sensorEventListener, mLight, SensorManager.SENSOR_DELAY_NORMAL);
            }
            else {
                // Failure! No Light.
                L.e("Failure! No Light!");
            }
        }
    }

    private void HandleAcceData()
    {
        // It's not pretty, but it works(any calculations in onSensorChanged aren't pretty to be fair).
        new Thread(new Runnable(){
            @Override
            public void run()
            {
                //acceWriter.writeAcceToFile(AcceValues);
                sensorHandler.AcceHandler(AcceValues);
            }
        }).start();
    }

    private void HandleGyroData()
    {
        // It's not pretty, but it works(any calculations in onSensorChanged aren't pretty to be fair).
        new Thread(new Runnable(){
            @Override
            public void run()
            {
                //gyroWriter.writeGyroToFile(GyroValues);
                sensorHandler.GyroHandler(GyroValues);
            }
        }).start();
    }

    private void HandleLightData()
    {
        // It's not pretty, but it works(any calculations in onSensorChanged aren't pretty to be fair).
        new Thread(new Runnable(){
            @Override
            public void run()
            {
                //gyroWriter.writeGyroToFile(GyroValues);
                sensorHandler.LightHandler(LightValues);
            }
        }).start();
    }

    // It's a user - interface to define callback.
    public interface SensorHandler {
        public void AcceHandler(float Acce[]);
        public void GyroHandler(float Gyro[]);
        public void LightHandler(float Light);
    }

}