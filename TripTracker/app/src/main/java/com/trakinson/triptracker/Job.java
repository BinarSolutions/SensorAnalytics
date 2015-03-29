package com.trakinson.triptracker;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.view.View;
import android.widget.EditText;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class Job extends ActionBarActivity implements LocationListener, SensorEventListener {

    public double latitude;
    public double longitude;
    public double altitude;

    public int isGPSon;


    public boolean gpsEnabled;

    private LocationManager locationManager;

    public int ttt = 0;
    //public int tim = 1;
    public int sec = 0;
    public int ep = 0;


    private SensorManager mSensorManager;
    private Sensor mLight;
    private Sensor mMagFld;
    private Sensor mLinAcc;
    public float lux;
    public float magX;
    public float magY;
    public float magZ;
    public float LinAccx;
    public float LinAccy;
    public float LinAccz;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mMagFld = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mLinAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        getSystemService(Context.LOCATION_SERVICE);



        LocationListener locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);


        if (!gpsEnabled) {
            GPSon();
        }

        Timer timer = new Timer();
        MyTimerTask myTask = new MyTimerTask();
        timer.schedule(myTask, 120000, 120000);

    }

    class MyTimerTask extends TimerTask {
        public void run() {
            //sec++;
            SendData();

        }

    }


    public boolean checkInternetConnection()
    {

        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null)
        {
            NetworkInfo[] inf = connectivity.getAllNetworkInfo();
            if (inf != null)
                for (int i = 0; i < inf.length; i++)
                    if (inf[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    public void GPSon() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("Gps is currently disabled, please enable!");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("Not now",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }
    public void NETon() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("Internet is currently unavailable, please enable!");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("Not now",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    public void SendData() {
        if (ttt == 1){
            new GetMongoDB().execute("http://www.google.com");
            ep++;
        }
    }

    public void start(View view) {
        
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        getSystemService(Context.LOCATION_SERVICE);
        //ConnectivityManager connectivityManager
        //        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
       // NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (!gpsEnabled) {
            GPSon();
        }else{
            if (checkInternetConnection()){
                ttt = 1;
                SendData();

            }else{
                NETon();
            }
        }
    }

    public void stop(View view)  {
        SendData();
        ttt = 0;
    }

    @Override
    public void onLocationChanged(Location loc) {
        latitude=loc.getLatitude();
        longitude=loc.getLongitude();
        altitude=loc.getAltitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 5) {
            lux = event.values[0];
        }

        if (event.sensor.getType() == 2){
            magX = event.values[0];
            magY = event.values[1];
            magZ = event.values[2];
        }

        if (event.sensor.getType() == 10){
            LinAccx = event.values[0];
            LinAccy = event.values[1];
            LinAccz = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener( this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,mMagFld,  SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener( this, mLinAcc, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            latitude=loc.getLatitude();
            longitude=loc.getLongitude();
            altitude=loc.getAltitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {


        }
    }


    private class GetMongoDB extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            //if (ttt == 1) {


            EditText editText = (EditText) findViewById(R.id.editText);
            String client = editText.getText().toString();
            EditText editText1 = (EditText) findViewById(R.id.editText2);
            String coll1 = editText1.getText().toString();
            MongoClient mongo = new MongoClient("213.197.167.222", 27317);

            DB db = mongo.getDB("TripTracker");

            DBCollection coll = db.getCollection(client + "_" + coll1);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String Date = df.format(c.getTime());
            BasicDBObject doc = new BasicDBObject("Client", client)
                    .append("Time", Date)
                    .append("Device", Build.MODEL)
                    .append("Trip", coll1)
                    .append("Episode", ep)
                    .append("Gps", new BasicDBObject("Latitude", latitude)
                            .append("Longitude", longitude)
                            .append("Altitude", altitude))
                    .append("Weather", new BasicDBObject("AirTemp", 0)
                        .append("WindDirection", 0)
                        .append("WindSpeed", 0)
                        .append("Atmospheric_pressure", 0)
                        .append("AirHumidity", 0)
                        .append("RainSnowType", 0)
                        .append("Iliumination", lux))
                    .append("Sensors", new BasicDBObject("light_value", lux)
                        .append("Magnetic_field", new BasicDBObject("Magnetic_x", magX)
                                .append("Magnetic_y", magY)
                                .append("Magnetic_z", magZ))
                        .append("LinearAcceleration", new BasicDBObject("X", LinAccx)
                            .append("Y", LinAccy)
                            .append("Z", LinAccz)))
                    .append("Health", new BasicDBObject("Pulse", 80)
                            .append("Blood_pressure", new BasicDBObject("UBP", 70)
                                    .append("LBP", 115)))
                    .append("Media", new BasicDBObject("Video", "null")
                        .append("Photo", "null"));

            coll.insert(doc);
            //}

            return null;

        }

    }
}
