package edu.uw.servicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, ServiceConnection{

    private static final String TAG = "Main";

    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // raw is just binary data, so that's where we have mp3's stored.
//        player = MediaPlayer.create(this, R.raw.scott_joplin_the_entertainer_1902);
//        player.setOnCompletionListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(MainActivity.this, MusicService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE); // instead of using startService, we use bindService to establish some sort of ongoing relationship
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //when "Start" button is pressed
    public void handleStart(View v){
        Log.i(TAG, "Start pressed");
        Intent intent = new Intent(MainActivity.this, CountingService.class); // creates explicit intent
        // instead of calling startActivity function...use startService
        // Then, make sure to register service in manifest
        startService(intent);
    }

    //when "Stop" button is pressed
    public void handleStop(View v){
        Log.i(TAG, "Stop pressed");
    }


    /* Media controls */
    public void playMedia(View v){
//        player.start(); // start playing media (when we specified player in MainActivity)
        startService(new Intent(MainActivity.this, MusicService.class));
    }

    public void pauseMedia(View v){
//        player.pause(); // (when we specified player in MainActivity)
        musicService.pauseMusic();
    }

    public void stopMedia(View v){
//        player.stop(); // (when we specified player in MainActivity)

        // tell service to stop, be finished, destroy yourself
        stopService(new Intent(MainActivity.this, MusicService.class));
    }


    //when "Quit" button is pressed
    public void handleQuit(View v){
        // make sure that player is stopped when application is quit
        handleStop(null);
        player.release();
        player = null;
        finish(); //end the Activity
}

    @Override
    protected void onDestroy() {
        Log.v(TAG, "Activity destroyed");
        super.onDestroy();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.release();
    }

    private MusicService.LocalBinder musicService;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        musicService = (MusicService.LocalBinder)service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
