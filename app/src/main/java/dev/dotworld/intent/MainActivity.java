package dev.dotworld.intent;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_main);
        startServiceViaWorker();
    }

    public void onStartServiceClick(View v) {
        startService();
    }

    public void onStopServiceClick(View v) {
        stopService();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy called");
        stopService();
        super.onDestroy();
    }

    public void startService() {
        Log.d(TAG, "startService called");
        if (!MyService.isServiceRunning) {
            Intent serviceIntent = new Intent(this, MyService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        }
    }

    public void stopService() {
        Log.d(TAG, "stopService called");
        if (MyService.isServiceRunning) {
            Intent serviceIntent = new Intent(this, MyService.class);
            stopService(serviceIntent);
        }
    }

    public void startServiceViaWorker() {
        Log.d(TAG, "startServiceViaWorker called");
        String UNIQUE_WORK_NAME = "StartMyServiceViaWorker";
        WorkManager workManager = WorkManager.getInstance();
        String WORK_TAG_NOTES = null;
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        workManager.getInstance().enqueueUniqueWork(WORK_TAG_NOTES, ExistingWorkPolicy.REPLACE, (OneTimeWorkRequest) request);

    }
}