package com.polytron.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Handler;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.Context;

import androidx.annotation.Nullable;

public class BoundService extends Service {

    public static final String TIMER_BR = "com.polytron.services.timer";
    private final IBinder binder = new LocalBinder();
    private final Handler handler = new Handler();
    private int counter = 0;
    private boolean isRunning = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(TIMER_BR);
            intent.putExtra("count", counter);
            sendBroadcast(intent);
            if (isRunning) {
                counter++;
                handler.postDelayed(this, 1000);
            }
        }
    };

    public void startTimer() {
        if (!isRunning) {
            isRunning = true;
            handler.postDelayed(runnable, 1000);
        }
    }

    public void stopTimer() {
        if (isRunning) {
            isRunning = false;
            handler.removeCallbacks(runnable);
        }
    }

    public void resetTimer() {
        counter = 0;
    }
}