package edu.uw.servicedemo;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/*
    There is a difference between started services and bound services.
    This service here is a startService.  What is important is the
    onStartCommand() function.  That takes in what intent we send to this
    background thread. IntentService has already filled in a bunch of details
    in the onStartCommand().  Basically works as a queueing service, which keeps
    track of all of the intents that have been sent to it.
 */

public class CountingService extends IntentService {

    public static final String TAG = "CountingService";
    private int i;
    private Handler mHandler;

    public CountingService() {
        super("CountingService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
    }

    // returns an int, which is a flag that says what service should do in the
    // case that it is killed by system.
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.v(TAG, "Received: " + intent.toString());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // run in the background
        for (i = 0; i < 10; i++) {
            Log.v(TAG, "count: " + i);
            // You can't display UI elements from the background thread, but you can
            // communicate from this background thread to the main UI thread. do this with handler
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // From the background thread, let's make a class that does a little UI work...
                    // Handler will send this to main thread
                    Toast.makeText(CountingService.this, "" + i, Toast.LENGTH_SHORT).show();
                }
            });

            try {
                Thread.sleep(5000); // hey thread, don't do anything for 5 seconds.  Just wait.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}