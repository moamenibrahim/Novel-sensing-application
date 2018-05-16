package com.example.moamen.exercise2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import android.view.View;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView value;
    private MediaPlayer mediaPlayer;
    public SensorManager sensorManager;
    public static DecimalFormat DECIMAL_FORMATTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        value = findViewById(R.id.value);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMATTER = new DecimalFormat("#.000", symbols);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.infile);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // get values for each axes X,Y,Z
            float magX = event.values[0];
            float magY = event.values[1];
            float magZ = event.values[2];

            double magnitude = Math.sqrt((magX * magX) + (magY * magY) + (magZ * magZ));


            if (magnitude>=70){
                TextView txt = findViewById(R.id.metal);
                txt.setVisibility(View.VISIBLE);
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(50); //You can manage the time of the blink with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                txt.startAnimation(anim);
                mediaPlayer.start();

            }
            else
            {
                TextView txt = findViewById(R.id.metal);
                txt.setVisibility(View.INVISIBLE);
                txt.clearAnimation();
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }

            }
            // set value on the screen
            value.setText(DECIMAL_FORMATTER.format(magnitude) + " \u00B5Tesla");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}