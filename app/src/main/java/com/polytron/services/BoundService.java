package com.polytron.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class BoundService extends Service {

    private final IBinder binder = new LocalBinder();

    private MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends android.os.Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }

    private void startAudio() {
        String audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
    public void resumeAudio() {
        if (mediaPlayer == null) {
            startAudio();
        } else if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }
}
