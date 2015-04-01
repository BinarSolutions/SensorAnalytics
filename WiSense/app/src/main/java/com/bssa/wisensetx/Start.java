package com.bssa.wisensetx;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;



public class Start extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ImageView iv = (ImageView)findViewById(R.id.imageView);
        iv.setImageResource(R.drawable.wisense);

        if (!checkInternetConnection()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No network connection detected. Check your network connection and restart the app.");
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);
                }
            });
            AlertDialog alert=builder.create();
            alert.show();
        }
        //Random rand = new Random();
        //int n = rand.nextInt(8);
        else{
        Timer timer = new Timer();
        MyTimerTask myTask = new MyTimerTask();
        timer.schedule(myTask, 1200);
        }
    }

    public void check (){
        Intent intent = new Intent(this, MyActivity.class);
        startActivity(intent);
        finish();
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
    class MyTimerTask extends TimerTask {
        public void run() {
            check();
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
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
}
