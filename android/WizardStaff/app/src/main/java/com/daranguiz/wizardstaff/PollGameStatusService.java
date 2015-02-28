package com.daranguiz.wizardstaff;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class PollGameStatusService extends IntentService {
    String TAG = "PollSparkService";

    public PollGameStatusService() {
        super("PollSparkService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /* Using Volley, seems to be a good way to handle HTTP GET requests
         * https://developer.android.com/training/volley/simple.html */

        Log.d(TAG, "PollSparkService Started");

        // Instantiate the RequestQueue
        // http://stackoverflow.com/questions/16626032/volley-post-get-parameters
        String sparkURL = "https://api.spark.io/v1/devices/50ff6e065067545631270587/";
        final String sparkToken = "dee3d4daa012763c0bfd854647224b5e0883996f";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sparkReq = new StringRequest(Request.Method.POST,
                sparkURL + "checkfull",
                createMyReqSuccessListener(),
                createMyReqErrorListener()) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", sparkToken);
                return params;
            }
        };
        queue.add(sparkReq);
    }

    private Response.Listener<String> createMyReqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "HTTP POST Error");
            }
        };
    }
}