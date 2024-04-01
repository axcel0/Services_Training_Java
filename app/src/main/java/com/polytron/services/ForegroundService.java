package com.polytron.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ForegroundService extends Service {

    private MediaPlayer mediaPlayer;
    private final int NOTIFICATION_ID = 1;
    private final String channelId = "100";
    //onStartCommand
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        playAudio();
        return START_STICKY;
    }
    //onDestroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    //onCreate
    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1, showNotification("Foreground Service is running"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void playAudio() {
        String audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
        mediaPlayer = new MediaPlayer();
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        mediaPlayer.setAudioAttributes(audioAttributes);
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Playing audio", Toast.LENGTH_SHORT).show();
    }

    private Notification showNotification(String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        return new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Foreground Service")
                .setContentText(content)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();
    }
}
