package com.example.nashibah;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicPlayerService extends Service {
    private MediaPlayer mediaPlayer;
    private final IBinder binder = new MusicPlayerBinder();

    public class MusicPlayerBinder extends Binder {
        MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.rightnow);
        mediaPlayer.setLooping(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void start() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public void release() {
        mediaPlayer.release();
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}