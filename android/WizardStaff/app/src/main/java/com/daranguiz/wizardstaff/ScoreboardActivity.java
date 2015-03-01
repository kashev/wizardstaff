package com.daranguiz.wizardstaff;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.ValueFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ScoreboardActivity extends ActionBarActivity implements
        OnChartValueSelectedListener, OnChartGestureListener {

    static final private String TAG = "ScoreboardActivity";
    public String myName;
    public String myGlass;

    // Chart vars
    protected BarChart mChart;
    private Typeface mTf;
    ValueFormatter intFormat;

    // Alarm vars
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    // Service interaction
    public BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        dbInit(getIntent());

        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setOnChartGestureListener(this);

        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");

        // Dario flag changes
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);

        // Set max on the chart to 10 for now, should never go above 3 on demo
        mChart.setMaxVisibleValueCount(10);

        // Scaling can now only be done on the x and y-axis separately
        mChart.setPinchZoom(false);

        // Disable background
        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        intFormat = new MyValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(intFormat);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        setData();

        Legend l = mChart.getLegend();
        l.setEnabled(false);

        /*** Begin Repeating Alarm Setup ***/
        int pollFrequencySeconds = 5;
        Context context = getApplicationContext();
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                1000 * pollFrequencySeconds,
                1000 * pollFrequencySeconds,
                alarmIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();

        /*** Begin UI Update Listener ***/
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setDataFromJSON(intent.getStringExtra(PollGameStatusService.SCORE_KEY));
            }
        };
    }

    private void dbInit(Intent intent) {
        myName = intent.getStringExtra("name");
        myGlass = intent.getStringExtra("glass");

        Firebase.setAndroidContext(this.getApplicationContext());
        Firebase myDrinkDb = new Firebase("https://wizardstaff.firebaseio.com/Sparks");
        Firebase curGlass = myDrinkDb.child(myGlass);
        Log.d(TAG, myName + myGlass);
        curGlass.child("Owner").setValue(myName);
        curGlass.child("Owned").setValue(1);
        curGlass.child("NumDrinks").setValue(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(PollGameStatusService.DRINK_RESULT));
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scoreboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setData() {
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Dario");
        xVals.add("Kashev");
        xVals.add("Ahmed");
        xVals.add("Brady");
        int count = 4;

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        int maxVal = 0;
        for (int i = 0; i < count; i++) {
            int val = (int) (Math.random() * 10f);
            yVals.add(new BarEntry(val, i));
            if (val > maxVal) {
                maxVal = val;
            }
        }

        BarDataSet set = new BarDataSet(yVals, "Number of Drinks");
        set.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        data.setDrawValues(false);
        data.setValueFormatter(intFormat);

        mChart.getAxisLeft().setLabelCount(maxVal);
        mChart.setData(data);
        mChart.invalidate(); //refresh
    }

    private void setDataFromJSON(String stringJSON) {
        JSONObject fromDbJSON;
        try {
            fromDbJSON = new JSONObject(stringJSON);
        } catch(JSONException e) {
            Log.d(TAG, "Could not form JSONObject from string from service");
            return;
        }

        Iterator<String> it = fromDbJSON.keys();
        ArrayList<String> ownersList = new ArrayList<String>();
        ArrayList<Integer> numDrinksList = new ArrayList<Integer>();
        while (it.hasNext()) {
            String owner = it.next();
            int numDrinks = 0;
            try {
                numDrinks = fromDbJSON.getInt(owner);
            } catch(JSONException e) {
                Log.d(TAG, "Could not get numDrinks");
                continue;
            }

            ownersList.add(owner);
            numDrinksList.add(numDrinks);
        }

        int numOwners = ownersList.size();
//        Log.d(TAG, Integer.toString(numOwners));
        if (numOwners == 0) {
            return;
        }

        // Begin populating bar graph
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        int maxVal = 0;
        for (int i = 0; i < numOwners; i++) {
            int curNumDrinks = numDrinksList.get(i);
            yVals.add(new BarEntry(curNumDrinks, i));
            if (curNumDrinks > maxVal) {
                maxVal = curNumDrinks;
            }
        }

        BarDataSet set = new BarDataSet(yVals, "Number of Drinks");
        set.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set);

        BarData data = new BarData(ownersList, dataSets);
        data.setValueTextSize(10f);
        data.setDrawValues(false);
        data.setValueFormatter(intFormat);

        mChart.getAxisLeft().setLabelCount(maxVal);
        mChart.setData(data);
        mChart.invalidate(); //refresh
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null)
            return;

        RectF bounds = mChart.getBarBounds((BarEntry) e);
        PointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());
    }

    public void onNothingSelected() {
        // STUB
    }

    /******** GESTURE CALLBACKS *********/
    public void onChartLongPressed(MotionEvent me) {
        // DO NOTHING
    }

    /* Send DRINK command to Spark Core */
    public void onChartDoubleTapped(MotionEvent me) {
        sendTakeDrinkRequest();
    }

    public void onChartSingleTapped(MotionEvent me) {
        // DO NOTHING
    }

    public void onChartFling(MotionEvent me1, MotionEvent me2,
                             float velocityX, float velocityY) {
        // DO NOTHING
    }

    /******* HELPER *******/
    private void sendTakeDrinkRequest() {
        // Instantiate the RequestQueue
        // http://stackoverflow.com/questions/16626032/volley-post-get-parameters
        String sparkURL = "https://api.spark.io/v1/devices/53ff70065075535143191087/";
        final String sparkToken = "dee3d4daa012763c0bfd854647224b5e0883996f";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sparkReq = new StringRequest(Request.Method.POST,
                sparkURL + "takedrink",
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
                // Do nothing, we expect no return from takedrink
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