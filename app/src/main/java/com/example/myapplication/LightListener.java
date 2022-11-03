




package com.example.myapplication;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

//this is the basic light listener
public class LightListener implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private LightListener.LightChangeListener lightChange;

    private Context mContext;

    public LightListener(Context c) {
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
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
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
    public void setOnLightListener(LightListener.LightChangeListener listener) {
        lightChange = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        lightChange.ChangeLight(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public interface LightChangeListener {
        public void ChangeLight(SensorEvent temp);

    }
}
