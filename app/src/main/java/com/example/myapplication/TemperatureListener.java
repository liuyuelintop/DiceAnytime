package com.example.myapplication;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

//this is the basic temperature listener
public class TemperatureListener implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private TemperatureListener.TemperatureChangeListener TemperatureChangeListener;

    private Context mContext;

    public TemperatureListener(Context c) {
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
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }
        // register
        if (sensor != null) {
            sensorManager.registerListener(this, sensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }

    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    // set sensor lister
    public void setOnTemperatureListener(TemperatureListener.TemperatureChangeListener listener) {
        TemperatureChangeListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float temp = event.values[0];
        temp = (float) (Math.round(temp * 10.0) / 10.0);
        TemperatureChangeListener.ChangeTemperature(temp);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public interface TemperatureChangeListener {
        public void ChangeTemperature(float temp);

    }
}
