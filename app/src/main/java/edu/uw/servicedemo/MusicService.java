package edu.uw.servicedemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MusicService extends Service {

    private static final String TAG = "MusicService";
    private MediaPlayer player;
    private String songTitle = "The Entertainer";
    private int PENDING_INTENT_ID = 0;
    private int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        Log.v(TAG, "Service Started");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // here is where we want to start playing our music

        // because we can send multiple intents to service, we don't want to create
        // a new media player every single time.  Only when it hasn't been created
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.scott_joplin_the_entertainer_1902);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(), PENDING_INTENT_ID,
                new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        // make a notification, which functions as the UI for this particular service
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle("Music Player")
                .setContentText("Now playing: " + songTitle)
                .setContentIntent(pendingIntent) // what gets executed when notification is clicked on
                .setOngoing(true) // notification we are creating should stick around in the notification drawer
                .build();

        // now specify that our service is a Foreground service
        // start as foreground service using this notification, and here's a number for that notification in case we want to update it
        startForeground(NOTIFICATION_ID, notification);

        player.start();
        return Service.START_NOT_STICKY; // Flag that specifies...if android kills this service, it won't start up again.  if you die, stay down.
    }

    public void stopMusic() {
        stopForeground(true); // yes, you're done. Don't be foreground anymore.
        player.stop();
        player.release();
        player = null;
    }

    @Override
    public void onDestroy() {
        stopMusic();
        super.onDestroy();
    }

    // what to do when we want to bind a service to an application
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        return myBinder;
    }

    private final IBinder myBinder = new LocalBinder();

    // the interface that someone else will call on the service.  Acts like a bridge between Activity & Service
    public class LocalBinder extends Binder {
        // this gets passed to the activity, and since it's like an interface,
        // the activity will be able to call the methods of this binder, to do things like
        // pause music!

        public void pauseMusic() {
            player.pause();
        }

    }
}
