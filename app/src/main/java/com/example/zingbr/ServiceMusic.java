package com.example.zingbr;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.zingbr.model.Music;

import java.util.List;

public class ServiceMusic extends Service {

    private MediaPlayer mediaPlayer;
    private String currentMusicTitle;
    public static final String ACTION_PLAY = "com.example.zingbr.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.example.zingbr.ACTION_PAUSE";
    public static final String ACTION_RESUME = "com.example.zingbr.ACTION_RESUME";
    public static final String ACTION_STOP = "com.example.zingbr.ACTION_STOP";
    public static final String EXTRA_URI = "com.example.zingbr.EXTRA_URI";
    public static final String EXTRA_TITLE = "com.example.zingbr.EXTRA_TITLE";
    private static final String MUSIC_ID = "MUSIC_ID";
    private static final String MUSIC_TITLE = "MUSIC_TITLE";
    private static final String MUSIC_URI = "MUSIC_URI";
    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    private final IBinder biner = new LocalBinder();

    public class LocalBinder extends Binder{
        ServiceMusic getService(){
            return ServiceMusic.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return biner;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent !=  null){
            String action = intent.getAction();
            Uri uri = intent.getParcelableExtra(EXTRA_URI);
            String title = intent.getStringExtra(EXTRA_TITLE);

            if(ACTION_PLAY.equals(action)){
                SharedPreferences.Editor editor = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
                editor.putString(MUSIC_TITLE, title);
                editor.apply();
                Log.e("title1", title+"");
                // Phát nhạc với URI và tiêu đề
                playMusic(uri, title);
            } else if (ACTION_PAUSE.equals(action)) {
                pauseMusic();
            } else if (ACTION_RESUME.equals(action)) {
                resumeMusic();
            } else if (ACTION_STOP.equals(action)) {
                stopMusic();
            }
        }
        return START_STICKY;
    }

    private void playMusic(Uri uri, String title){
        if(mediaPlayer != null){
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
    }
    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
    private void resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    public String getLastPlayedTitle() {
        SharedPreferences prefs = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE);
        return prefs.getString(MUSIC_TITLE, "");
    }
}
