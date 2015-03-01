package com.daranguiz.wizardstaff;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PollGameStatusService extends IntentService {
    private String TAG = "PollSparkService";
    static final public String DRINK_RESULT = "com.daranguiz.qizardstaff.PollGameStatusService.REQUEST_PROCESSED";
    private LocalBroadcastManager broadcaster;
    static final public String SCORE_KEY = "SCORES";

    public PollGameStatusService() {
        super("PollSparkService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /* Using Volley, seems to be a good way to handle HTTP GET requests
         * https://developer.android.com/training/volley/simple.html */

        Log.d(TAG, "PollSparkService Started");

        broadcaster = LocalBroadcastManager.getInstance(this);

        Firebase.setAndroidContext(this);
        final Firebase myDrinkDb = new Firebase("https://wizardstaff.firebaseio.com/Sparks");
        final JSONObject scores = new JSONObject();

        myDrinkDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot spark : dataSnapshot.getChildren()) {
                    if ((int) (long) spark.child("Owned").getValue() == 1) {
                        String curOwner = (String) spark.child("Owner").getValue();
                        int numDrinks = (int) (long) spark.child("NumDrinks").getValue();
                        try {
                            scores.put(curOwner, numDrinks);
                        } catch (JSONException e) {
                            Log.d(TAG, "JSONException");
                        }
                    }
                }

                String toBarGraphJSON = scores.toString();
                updateChart(toBarGraphJSON);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // Do nothing
            }
        });

        sendCheckFullRequest();
    }

    public void updateChart(String message) {
        Intent intent = new Intent(DRINK_RESULT);
        intent.putExtra(SCORE_KEY, message);
        broadcaster.sendBroadcast(intent);
    }

    private void sendCheckFullRequest() {
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
                // This responds in JSON, pull retval
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