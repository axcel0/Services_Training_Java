package com.polytron.services;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private BoundService mService;
    private boolean mBound = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btnStart = findViewById(R.id.btnStart);
        Button btnStop = findViewById(R.id.btnStop);
        btnStart.setOnClickListener(v -> startForegroundService(new Intent(MainActivity.this, ForegroundService.class)));
        btnStop.setOnClickListener(v -> stopService(new Intent(MainActivity.this, ForegroundService.class)));
        


    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BoundService.LocalBinder binder = (BoundService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    //onStart
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BoundService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }
    //onDestroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBound) {
            unbindService(serviceConnection);
            mBound = false;
        }
    }
}