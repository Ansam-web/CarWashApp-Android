package com.example.carwash.utils;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton instance;
    private RequestQueue queue;
    private static Context ctx;

    private VolleySingleton(Context context) {
        ctx = context.getApplicationContext();
        queue = Volley.newRequestQueue(ctx);
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public <T> void add(Request<T> req) {
        queue.add(req);
    }
}
