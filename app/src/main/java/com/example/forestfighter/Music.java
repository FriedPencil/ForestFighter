package com.example.forestfighter;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

public class Music extends Service {
    MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        try{
            mediaPlayer = new MediaPlayer();
            mediaPlayer = MediaPlayer.create(Music.this,R.raw.music);
            mediaPlayer.prepare();
        } catch(IllegalStateException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
