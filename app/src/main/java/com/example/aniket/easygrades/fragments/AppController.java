package com.example.aniket.easygrades.fragments;


import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 *  The app controller class provides us with functionality of running
 *  the GET and POST request in a queue with their assigned parameters.
 *  The JSON functions call this class and this task is executed asnychronously.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        Log.d("test","reached here");
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        Log.d("complete","after volley request");
        return mRequestQueue;
    }

    /**
     * This functions add string requested with a tag labelling the request.
     * @param req
     * @param tag
     */

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        Log.d("tag volley","adding in queue");
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}