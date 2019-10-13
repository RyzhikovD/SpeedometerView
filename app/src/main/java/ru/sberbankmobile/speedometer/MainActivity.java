package ru.sberbankmobile.speedometer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpeedometerView speedometerView = findViewById(R.id.speedometer);
        speedometerView.setCurrentSpeed(75);
    }
}
