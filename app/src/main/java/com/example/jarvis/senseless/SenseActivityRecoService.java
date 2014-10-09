package com.example.jarvis.senseless;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;


/**
 * Created by jarvis on 10/7/14.
 */
public class SenseActivityRecoService extends IntentService{

   public SenseActivityRecoService(){
       super("Senseless Activity Recognition Service Provider!");
   }

    @Override

    protected void onHandleIntent(Intent intent) {

// If the intent contains an update

        if (ActivityRecognitionResult.hasResult(intent)) {

// Get the update on the Activity
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

            Log.i("Anuj", getType(result.getMostProbableActivity().getType()) + "\t" + result.getMostProbableActivity().getConfidence());

            Intent i = new Intent("com.example.jarvis.senseless.ACTIVITY_RECOG_DATA");
            i.putExtra("Activity", getType(result.getMostProbableActivity().getType()));
            i.putExtra("Confidence", getType(result.getMostProbableActivity().getConfidence()));
            sendBroadcast(i);

        }
    }

    private static String getType(int type) {

        switch(type){
            case DetectedActivity.IN_VEHICLE:
                return "in vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on bicycle";
            case DetectedActivity.WALKING:
                return "on foot";
            case DetectedActivity.TILTING:
                return "tilting";
            case DetectedActivity.STILL:
                return "still";
            default:
                return "unknown";
        }
    }
}
