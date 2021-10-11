package dev.dotworld.intent;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.ACTIVITY_SERVICE;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import android.app.Activity;

import java.util.Iterator;
import java.util.List;

public class MyWorker extends Worker {
    private final Context context;
    private String TAG = "MyWorker";
    public MyWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    public boolean isAppForeground(final Context context) {
        ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();
        for (int i = 0; i < runningAppProcessInfo.size(); i++) {
            if (runningAppProcessInfo.get(i).processName.equals("dev.dotworld.intent")) {
                // Do any thing
            }
        }
        if (am == null) {
            return false;
        }
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        if (info == null || info.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (aInfo.processName.equals(getApplicationContext().getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork called for: " + this.getId());
        Log.d(TAG, "Service Running: " + MyService.isServiceRunning);
        if (!isAppForeground(context)) {
            startNewActivity(context, "dev.dotworld.intent");
        }
        if (!MyService.isServiceRunning) {
            Log.d(TAG, "starting service from doWork");
            Intent intent = new Intent(this.context, MyService.class);
            ContextCompat.startForegroundService(context, intent);
        }
        return Result.success();
    }

    @Override
    public void onStopped() {
        Log.d(TAG, "onStopped called for: " + this.getId());
        super.onStopped();
        Intent intent = new Intent(this.context, MyService.class);
    }
}