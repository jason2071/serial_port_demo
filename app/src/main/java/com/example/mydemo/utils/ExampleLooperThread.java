package com.example.mydemo.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

public class ExampleLooperThread extends Thread {
    private static final String TAG = "ExampleLooperThread";

    public Looper looper;
    public Handler handler;

    @Override
    public void run() {
        for (int i = 0; i < 15; i++) {
            Log.d(TAG, "run: " + i);
            SystemClock.sleep(1000);
        }
        Log.d(TAG, "End of run()");
    }
}
