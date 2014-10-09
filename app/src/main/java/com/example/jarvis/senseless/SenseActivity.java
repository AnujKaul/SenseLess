package com.example.jarvis.senseless;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;


public class SenseActivity extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    //Local declerations
    private ActivityRecognitionClient actRecClient;
    private PendingIntent callbackIntent;
    private BroadcastReceiver bcRecv;
    private TextView tvActivity;
    private Context context;

//    public static final int MILLISECONDS_PER_SECOND = 100;
//    public static final int DETECTION_INTERVAL_SECONDS = 2;
//    public static final int DETECTION_INTERVAL_MILLISECONDS = MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;

    public SenseActivity(Context context){
        this.context = context;
    }


    @Override
    public void onConnected(Bundle bundle) {
        Intent intent = new Intent(this, SenseActivityRecoService.class);
        callbackIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        actRecClient.requestActivityUpdates(0, callbackIntent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sense);
        tvActivity = (TextView) findViewById(R.id.tvActivity);

        int response = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(response == ConnectionResult.SUCCESS){
            actRecClient = new ActivityRecognitionClient(this, this , this);
            actRecClient.connect();
        }
        else{
            Toast.makeText(this, "Please check Play Services API", Toast.LENGTH_SHORT).show();
        }
        //CAN CHECK FOR UNAVAILABLITY AND THEN SWITCH TO SENSORS!

        bcRecv = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String actIntent = "Activity : " + intent.getStringExtra("Activity") + " " + "Confidence : " + intent.getExtras().getInt("Confidence") +  "\n";
                actIntent += tvActivity.getText();
                tvActivity.setText(actIntent);
            }
        };

        IntentFilter filter =  new IntentFilter();
        filter.addAction("com.example.jarvis.senseless.ACTIVITY_RECOG_DATA");
        registerReceiver(bcRecv,filter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(actRecClient != null){
            actRecClient.removeActivityUpdates(nextIntent);
            actRecClient.disconnect();
        }
        unregisterReceiver(bcRecv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sense, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
