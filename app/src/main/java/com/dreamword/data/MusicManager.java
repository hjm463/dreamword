package com.dreamword.data;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MusicManager {
    private static final String TAG = "MusicManager";
    private static MusicManager instance;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private String tempMusicPath;
    private Context context;

    public static synchronized MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
    }

    public boolean hasMusic() {
        return true;
    }

    public void play() {
        if (isPlaying) return;

        try {
            if (mediaPlayer == null) {
                String musicData = getEmbeddedMusicData();
                if (musicData != null && !musicData.isEmpty()) {
                    byte[] decoded = Base64.decode(musicData, Base64.DEFAULT);
                    tempMusicPath = context.getCacheDir() + "/background_music.mp3";
                    FileOutputStream fos = new FileOutputStream(tempMusicPath);
                    fos.write(decoded);
                    fos.close();

                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(tempMusicPath);
                    mediaPlayer.prepare();
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setVolume(0.3f, 0.3f);
                } else {
                    Log.w(TAG, "No embedded music data");
                    return;
                }
            }

            mediaPlayer.start();
            isPlaying = true;
            Log.d(TAG, "Music started");
        } catch (IOException e) {
            Log.e(TAG, "Failed to play music", e);
            stop();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                Log.e(TAG, "Failed to stop music", e);
            }
        }
        isPlaying = false;
        
        if (tempMusicPath != null && new File(tempMusicPath).exists()) {
            new File(tempMusicPath).delete();
        }
        Log.d(TAG, "Music stopped");
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    private String getEmbeddedMusicData() {
        return "";
    }
}