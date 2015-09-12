package com.durgaindia.findurga;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;


public class DurgaLocationService extends Service implements LocationListener{

    @Override
    public void onCreate(){
            Log.i("Sonika", "in on create serveice");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

    	int val = intent.getIntExtra("widget", -1);
    	if(val == 0){
    		DurgaUserActivity mDurgaUserActivity = new DurgaUserActivity();
    		User newUser = DurgaHelperUtility.getUserDetailFromSharedPref(this);
    		mDurgaUserActivity.findNearestDurga(newUser);
    	}
        Log.i("Sonika", "service started");
        final LocationManager locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,DurgaLocationService.this);

       new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
                        Log.i("Sonika", "in timer task");
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,DurgaLocationService.this);
                    }
                },
                15*60*1000

        );
        return Service.START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {
        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put("username", "sonika.srivastava@gmail.com");
            reqObj.put("loc_latitude", location.getLatitude());
            reqObj.put("loc_longitude", location.getLongitude());


        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        new UpdateDurgaLocationTask().execute(reqObj);
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

    class UpdateDurgaLocationTask extends AsyncTask<JSONObject, Void, HttpResponse> {


        protected HttpResponse doInBackground(JSONObject... reqObj) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response;
            Log.i("Sonika", reqObj[0].toString());

            //url with the post data
            HttpPost httpost = new HttpPost("http://192.168.0.169:6543/durga_update/");

            //passes the results to a string builder/entity
            StringEntity se = null;
            try {
                se = new StringEntity(reqObj[0].toString());

                //sets the post request as the resulting string
                httpost.setEntity(se);
                //sets a request header so the page receving the request
                //will know what to do with it
                //httpost.setHeader("Accept", "application/json");
                httpost.setHeader("Content-type", "application/json");

                response = httpClient.execute(httpost);

                Log.i("Sonika", response.getEntity().getContent() + "");

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return response;

        }
        protected void onPostExecute(HttpResponse response) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }




}
