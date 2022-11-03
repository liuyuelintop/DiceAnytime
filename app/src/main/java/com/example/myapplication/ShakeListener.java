package com.example.myapplication;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

//the basic shake listener
public class ShakeListener implements SensorEventListener {
    String TAG = "ShakeListener";
    // speed threshold
    private static final int SPEED_SHRESHOLD = 3000;
    // stop speed threshold
    private static final int STOP_SPEED_SHRESHOLD = 500;
    // testing gap
    private static final int UPTATE_INTERVAL_TIME = 70;
    private SensorManager sensorManager;
    private Sensor sensor;
    private OnShakeListener onShakeListener;

    private Context mContext;
    // last time speed location
    private float lastX;
    private float lastY;
    private float lastZ;
    // last time
    private long lastUpdateTime;
    // start sharking flag
    private boolean startSharking;

    // constructor
    public ShakeListener(Context c) {
        // get the monitored context
        mContext = c;
        start();
    }
    public void start() {
        // get manager
        sensorManager = (SensorManager) mContext
                .getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            // get sensor
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        // register
        if (sensor != null) {
            sensorManager.registerListener(this, sensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
        // reset flag
        startSharking = false;
    }

    // stop detecte
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    // set sensor lister
    public void setOnShakeListener(OnShakeListener listener) {
        onShakeListener = listener;
    }

    // sensor value changed
    public void onSensorChanged(SensorEvent event) {
        // time
        long currentUpdateTime = System.currentTimeMillis();
        // gap
        long timeInterval = currentUpdateTime - lastUpdateTime;
        if (timeInterval < UPTATE_INTERVAL_TIME)
            return;
        lastUpdateTime = currentUpdateTime;
        // get location
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        // location value
        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;
        lastX = x;
        lastY = y;
        lastZ = z;
        //
        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
                * deltaZ)
                / timeInterval * 10000;
        Log.v(TAG, "===========log===================");
        // if speed is larger than threshold start sharking
        if (speed >= SPEED_SHRESHOLD) {
            startSharking = true;
            onShakeListener.StartShake();
        }
        if (startSharking){
            onShakeListener.OnShaking();
        }
        // if stop sharking than do action
        if (speed < STOP_SPEED_SHRESHOLD && startSharking){
            onShakeListener.AfterShake();
            startSharking = false;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // shark interface
    public interface OnShakeListener {
        public void AfterShake();
        public void StartShake();
        public void OnShaking();
    }
}