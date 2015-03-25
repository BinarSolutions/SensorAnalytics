package com.knddevs.wisensetx;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;

public class MyActivity extends ActionBarActivity  implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mLight;
    private Sensor mMagFld;
    private Sensor mGravity;
    private Sensor mAccel;
    private Sensor mGyro;
    private Sensor mLinAcc;
    private Sensor mProx;
    private Sensor mOrient;
    private Sensor mRV;
    private Sensor mGRV;
    private Sensor mBaro;
    private Sensor mTemp;
    private Sensor mHumid;
    private Sensor mHRM;

    public float lux;
    public float magX;
    public float magY;
    public float magZ;
    public float gravX;
    public float gravY;
    public float gravZ;
    public float accX;
    public float accY;
    public float accZ;
    public float gyrX;
    public float gyrY;
    public float gyrZ;
    public float linX;
    public float linY;
    public float linZ;
    public float prox;
    public float orientA;
    public float orientP;
    public float orientR;
    public float RVX;
    public float RVY;
    public float RVZ;
    public float GRVX;
    public float GRVY;
    public float GRVZ;
    public float bar;
    public float hum;
    public float temp;

    public int ttt = 0;
    public int tim;
    public int sec = 0;
    public int selected = 0;
    public int sel;

    //public String coll1;

    public ConnectivityManager cm;
    public NetworkInfo activeNetwork;

    private TextView mTextView1; //network status
    private TextView mTextView2; //job status
    private TextView mTextView3; //selected sensor
    private ListView mListView;
    private Button Button1;
    private Button Button2;
    private ArrayAdapter<String> listAdapter;

    public boolean isWiFi;
    public boolean isConnected;

    public int op = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(op == 0){
            setContentView(R.layout.activity_my);
            op = 1;
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mMagFld = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mLinAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mRV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mGRV = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        mProx = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mHumid = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        mBaro = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mTemp = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mOrient = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mHRM = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        Timer timer = new Timer();
        MyTimerTask myTask = new MyTimerTask();
        timer.schedule(myTask, 1000, 1000);

        mListView = (ListView)findViewById(R.id.sensor_list);
        mTextView1 = (TextView)findViewById(R.id.net_status);
        mTextView2 = (TextView) findViewById(R.id.jobStatus);
        mTextView3 = (TextView) findViewById(R.id.selSensor);

        if(ttt == 0){
            mTextView2.setText("Ready");
        }


        final List<Sensor> mList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        mTextView1 = (TextView)findViewById(R.id.net_status);

        ArrayList<String> sList = new ArrayList<>();

        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, sList);
        mListView.setAdapter(listAdapter);
        for (int i = 0; i < mList.size(); i++) {
            listAdapter.add(mList.get(i).getName());
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = position + 1;
                String item = ((TextView)view).getText().toString();
                mTextView3 = (TextView) findViewById(R.id.selSensor);
                mTextView3.setText(item);
                mTextView3.setTextSize(13);
                Button1 = (Button) findViewById(R.id.start_btt);
                Button1.setClickable(true);
                Button1.setTextColor(Color.parseColor("#ffffffff"));
                Button2 = (Button) findViewById(R.id.stop_btt);
                Button2.setClickable(true);
                Button2.setTextColor(Color.parseColor("#ffffffff"));


            }
        });

    if (checkInternetConnection()){
        mTextView1 = (TextView)findViewById(R.id.net_status);
        isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        if (isWiFi){
            mTextView1.setText(R.string.network_status_wifi);
        }
        if (!isWiFi) {
            mTextView1.setText(R.string.network_status_cell);
        }
    }
    if(!checkInternetConnection()){
        mTextView1.setText(R.string.network_status_no);
        mTextView2.setText("N/A");
    }


    }
    public boolean checkInternetConnection()
    {
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null){
            NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if(isConnected) return true;
        }
        return false;
    }


    public void start (View view) {
        EditText editText1 = (EditText) findViewById(R.id.edit_message);
        String message1 = editText1.getText().toString();
        if(Integer.parseInt(message1) >= 5) {
            mTextView2 = (TextView) findViewById(R.id.jobStatus);
            if (checkInternetConnection()) {
                cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                activeNetwork = cm.getActiveNetworkInfo();
                isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
                sec = 0;

                if (isWiFi) {

                    EditText editText = (EditText) findViewById(R.id.edit_message);
                    String message = editText.getText().toString();

                    if (selected == 0) {
                        Context context = getApplicationContext();
                        CharSequence text = "No sensor selected!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        Button1 = (Button) findViewById(R.id.start_btt);
                        Button1.setClickable(false);
                        Button1.setTextColor(Color.parseColor("#ff1e1e1e"));
                        Button2 = (Button) findViewById(R.id.stop_btt);
                        Button2.setClickable(false);
                        Button2.setTextColor(Color.parseColor("#ff1e1e1e"));
                    }

                    if (TextUtils.isEmpty(message)) {
                        editText.setError("Please fill in!");

                        return;
                    } else {
                        editText.setError(null);
                    }

                    if (TextUtils.isEmpty(message) == false) {
                        if (selected != 0) {
                            if (!wrongSensor()) {
                                ttt = 1;
                                tim = Integer.parseInt(message);
                                Context context = getApplicationContext();
                                CharSequence text = "Job started!";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                mTextView1 = (TextView) findViewById(R.id.net_status);
                                mTextView1.setText(R.string.network_status_wifi);
                                mTextView2.setText("Ongoing. Sending data every " + tim + " seconds");
                            } else {

                                mTextView3.setText("This sensor shows no values or is duplicating. Choose another one");

                                mTextView2.setText("N/A");
                            }

                        }
                    }
                } else if (!isWiFi) {
                    final EditText editText = (EditText) findViewById(R.id.edit_message);
                    Button1 = (Button) findViewById(R.id.start_btt);
                    Button2 = (Button) findViewById(R.id.stop_btt);
                    final String message = editText.getText().toString();
                    if (selected == 0) {
                        Context context = getApplicationContext();
                        CharSequence text = "No sensor selected!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        Button1.setClickable(false);
                        Button1.setTextColor(Color.parseColor("#ff1e1e1e"));
                        Button2.setClickable(false);
                        Button2.setTextColor(Color.parseColor("#ff1e1e1e"));
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle("Cellular data is being used")
                                .setMessage("You have enabled cellular data. This app uses a lot of data, so Wi-Fi is recommended.")
                                .setPositiveButton("Ignore and proceed", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {


                                        if (TextUtils.isEmpty(message)) {
                                            editText.setError("Please fill in!");

                                            return;
                                        } else {
                                            editText.setError(null);
                                        }

                                        if (TextUtils.isEmpty(message) == false) {
                                            if (selected != 0) {
                                                if (!wrongSensor()) {
                                                    ttt = 1;
                                                    tim = Integer.parseInt(message);
                                                    Context context = getApplicationContext();
                                                    CharSequence text = "Job started!";
                                                    int duration = Toast.LENGTH_SHORT;
                                                    Toast toast = Toast.makeText(context, text, duration);
                                                    toast.show();
                                                    mTextView1.setText(R.string.network_status_cell);
                                                    mTextView2.setText("Ongoing. Sending data every " + tim + " seconds");
                                                } else {
                                                    mTextView3.setText("This sensor shows no values or is duplicating. Choose another one");
                                                    mTextView2.setText("N/A");
                                                }

                                            }
                                        }
                                    }
                                })
                                .setNeutralButton("Go to Wi-Fi settings", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(intent);
                                        }
                                    }
                                })
                                .setNegativeButton("Quit the app", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        System.exit(0);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            } else {
                Context context = getApplicationContext();
                CharSequence text = "You should check your network connection!";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                mTextView1.setText("Disabled or interrupted");
                mTextView2.setText("N/A");
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You can't choose period less than 5 sec. Data will NOT be right.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert=builder.create();
            alert.show();
        }

    }
    public boolean wrongSensor(){
        sel = selected - 1;
        List<Sensor> mList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        if(selected!=0 && mList.get(sel).getType()!=1 && mList.get(sel).getType()!=2 && mList.get(sel).getType()!=3 && mList.get(sel).getType()!=4 && mList.get(sel).getType()!=5 && mList.get(sel).getType()!=6 && mList.get(sel).getType()!=8 && mList.get(sel).getType()!=9 && mList.get(sel).getType()!=10 && mList.get(sel).getType()!=11 && mList.get(sel).getType()!=12 && mList.get(sel).getType()!=13 && mList.get(sel).getType()!=15){
        return true;}
        else {return false;}
    }
    public void stop (View view) {
        mTextView2 = (TextView) findViewById(R.id.jobStatus);
        ttt = 0;
        tim = 0;
        sec = 0;
        selected = 0;
        sel = -1;
        mTextView3.setText(R.string.no_selected);
        Context context = getApplicationContext();
        CharSequence text = "Job stopped!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        mTextView2.setText("Stopped");

    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit?");
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

    class MyTimerTask extends TimerTask {

        public void run() {
            sec++;
            new GetMongoDB().execute("http://www.google.com");

        }

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public final void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 5) {
            lux = event.values[0];
        }
        if (event.sensor.getType() == 1){
            accX = event.values[0];
            accY = event.values[1];
            accZ = event.values[2];
        }
        if (event.sensor.getType() == 2){
            magX = event.values[0];
            magY = event.values[1];
            magZ = event.values[2];
        }
        if (event.sensor.getType() == 9){
            gravX = event.values[0];
            gravY = event.values[1];
            gravZ = event.values[2];
        }
        if (event.sensor.getType() == 3){
            orientA = event.values[0];
            orientP = event.values[1];
            orientR = event.values[2];
        }
        if (event.sensor.getType() == 4){
            gyrX = event.values[0];
            gyrY = event.values[1];
            gyrZ = event.values[2];
        }
        if (event.sensor.getType() == 10){
            linX = event.values[0];
            linY = event.values[1];
            linZ = event.values[2];
        }
        if (event.sensor.getType() == 11){
            RVX = event.values[0];
            RVY = event.values[1];
            RVZ = event.values[2];
        }
        if (event.sensor.getType() == 15){
            GRVX = event.values[0];
            GRVY = event.values[1];
            GRVZ = event.values[2];
        }
        if (event.sensor.getType() == 6) {
            bar = event.values[0];
        }
        if (event.sensor.getType() == 8) {
            prox = event.values[0];
            if (prox == 8)prox = 1;
        }
        if (event.sensor.getType() == 12) {
            hum = event.values[0];
        }
        if (event.sensor.getType() == 13) {
            temp = event.values[0];
        }
    }


    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagFld,  SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGravity,  SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mHRM,  SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGRV,  SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mRV,  SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAccel,  SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mLinAcc,  SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mHumid,  SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mBaro,  SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mTemp,  SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyro,  SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mProx,  SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mOrient,  SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }




    private class GetMongoDB extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            if (ttt == 1) {
                if (sec % tim == 0) {
                    MongoClient mongo = new MongoClient("213.197.167.222", 27317);

                    DB db = mongo.getDB("VJG");
                    List<Sensor> mList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
                    DBCollection coll;

                    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                    sel = selected - 1;
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String Date = df.format(c.getTime());

                    BasicDBObject doc;
                    if(mList.get(sel).getType() == 5){
                        coll = db.getCollection("Light");
                        doc = new BasicDBObject("Device",Build.MODEL)
                                        .append("Date",Date)
                                        .append("Value",lux);
                        coll.insert(doc);
                        }
                    if(mList.get(sel).getType() == 2){
                        coll = db.getCollection("Magnetic_field");
                        doc = new BasicDBObject("Device",Build.MODEL)
                            .append("Date", Date)
                            .append("Values", new BasicDBObject("X", magX)
                                    .append("Y", magY)
                                    .append("Z", magZ));
                        coll.insert(doc);
                    }
                    if(mList.get(sel).getType() == 1){
                        coll = db.getCollection("Accelerometer");
                        doc = new BasicDBObject("Device",Build.MODEL)
                                .append("Date", Date)
                                .append("Values", new BasicDBObject("X", accX)
                                        .append("Y", accY)
                                        .append("Z", accZ));
                        coll.insert(doc);
                    }
                    if(mList.get(sel).getType() == 9){
                        coll = db.getCollection("Gravity");
                        doc = new BasicDBObject("Device",Build.MODEL)
                            .append("Date", Date)
                            .append("Values", new BasicDBObject("X", gravX)
                                    .append("Y", gravY)
                                    .append("Z", gravZ));
                        coll.insert(doc);
                    }
                    if(mList.get(sel).getType() == 3){
                        coll = db.getCollection("Orientation");
                        doc = new BasicDBObject("Device",Build.MODEL)
                                .append("Date", Date)
                                .append("Values", new BasicDBObject("Azimuth", orientA)
                                        .append("Pitch", orientP)
                                        .append("Roll", orientR));
                        coll.insert(doc);
                    }
                    if(mList.get(sel).getType() == 4){
                        coll = db.getCollection("Gyroscope");
                        doc = new BasicDBObject("Device",Build.MODEL)
                                .append("Date", Date)
                                .append("Values", new BasicDBObject("X", gyrX)
                                        .append("Y", gyrY)
                                        .append("Z", gyrZ));
                        coll.insert(doc);
                    }
                    if(mList.get(sel).getType() == 10){
                        coll = db.getCollection("Linear_acceleration");
                        doc = new BasicDBObject("Device",Build.MODEL)
                                .append("Date", Date)
                                .append("Values", new BasicDBObject("X", linX)
                                        .append("Y", linY)
                                        .append("Z", linZ));
                        coll.insert(doc);
                    }
                    if(mList.get(sel).getType() == 11){
                        coll = db.getCollection("Rotation_vector");
                        doc = new BasicDBObject("Device",Build.MODEL)
                                .append("Date", Date)
                                .append("Vector values", new BasicDBObject("X", RVX)
                                        .append("Y", RVY)
                                        .append("Z", RVZ));
                        coll.insert(doc);
                    }
                    if(mList.get(sel).getType() == 15){
                        coll = db.getCollection("Game_rotation_vector");
                        doc = new BasicDBObject("Device",Build.MODEL)
                                .append("Date", Date)
                                .append("Vector values", new BasicDBObject("X", GRVX)
                                        .append("Y", GRVY)
                                        .append("Z", GRVZ));
                        coll.insert(doc);
                    }
                    if(mList.get(sel).getType() == 6){
                        coll = db.getCollection("Barometer");
                        doc = new BasicDBObject("Device",Build.MODEL)
                                .append("Date", Date)
                                .append("Value", bar);
                        coll.insert(doc);
                    }
                    if(mList.get(sel).getType() == 8){
                        coll = db.getCollection("Proximity");
                        doc = new BasicDBObject("Device",Build.MODEL)
                                .append("Date", Date)
                                .append("Value", prox);
                        coll.insert(doc);
                    }
                    if(mList.get(sel).getType() == 12){
                        coll = db.getCollection("Humidity");
                        doc = new BasicDBObject("Device",Build.MODEL)
                                .append("Date", Date)
                                .append("Value", hum);
                        coll.insert(doc);
                    }
                    if(mList.get(sel).getType() == 13){
                        coll = db.getCollection("Temperature");
                        doc = new BasicDBObject("Device",Build.MODEL)
                                .append("Date", Date)
                                .append("Value", temp);
                        coll.insert(doc);
                    }
                    else{

                    }

                }
            }
            return null;
        }
    }
}