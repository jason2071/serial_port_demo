package com.example.mydemo.manager;

import android.annotation.SuppressLint;
import android.content.Context;


public class Contextor {

    @SuppressLint("StaticFieldLeak")
    private static Contextor instance;

    public static Contextor getInstance() {
        if (instance == null)
            instance = new Contextor();
        return instance;
    }

    private Context mContext;

    private Contextor() {
        
    }

    public void init(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

}
