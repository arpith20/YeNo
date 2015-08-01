package com.op.an.randomapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParsePush;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity implements
        OnMapClickListener {

    String query, lng, lat;

    AutoCompleteTextView textView = null;
    private ArrayAdapter<String> adapter;


    String toNumberValue = "";

    private GoogleMap map;
    LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //1
//        Intent resultIntent = new Intent(this, MainActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//
//        //2
//        Intent resultIntentyes = new Intent(this, WriteQueryDatabase.class);
//        PendingIntent resultPendingIntentyes =
//                PendingIntent.getActivity(
//                        this,
//                        0,
//                        resultIntentyes,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//
//
//        //3
//        Intent resultIntentno = new Intent(this, WriteQueryDatabase.class);
//        Bundle bundle3 = new Bundle();
//        bundle3.putString("query", "");
//        bundle3.putString("message", "You answered no");
//        resultIntentyes.putExtras(bundle3);
//        TaskStackBuilder stackBuilderno = TaskStackBuilder.create(this);
//        stackBuilderno.addParentStack(MainActivity.class);
//        stackBuilderno.addNextIntent(resultIntentno);
//        PendingIntent resultPendingIntentno =
//                stackBuilderno.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.incidentmarker)
//                        .setContentTitle("My notification")
//                        .setContentText("Hello World!")
//                        .addAction(R.drawable.nothing, "Yes", resultPendingIntentyes)
//                        .addAction(R.drawable.nothing, "No", resultPendingIntentno)
//                        .addAction(R.drawable.nothing, "Spam", resultPendingIntent);
//
//
//        mBuilder.setContentIntent(resultPendingIntent);
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//// mId allows you to update the notification later on.
//        mNotificationManager.notify(0, mBuilder.build());

        lat = 0.0 + "";
        lng = 0.0 + "";


        Button b_submit, b_search;
        b_submit = (Button) findViewById(R.id.b_submit);
        b_search = (Button) findViewById(R.id.b_search);
        final EditText et_serach = (EditText) findViewById(R.id.et_search);
        b_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


                String add = et_serach.getText().toString();
                if (!add.matches("")) {
                    Geocoder geo = new Geocoder(getBaseContext());
                    List<Address> gotAddresses = null;
                    try {
                        gotAddresses = geo.getFromLocationName(add, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = (Address) gotAddresses.get(0);
                    currentLocation = new LatLng(address.getLatitude(), address.getLongitude());
                    location();
                }
            }
        });

        b_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                EditText et = (EditText) findViewById(R.id.et_question);
                String question = et.getText().toString();
                if (!question.matches("")) {
                    SharedPreferences settings = getSharedPreferences("preference", 0);
                    String username = settings.getString("username", "");
                    Log.d("username", username);
                    Integer count = settings.getInt("count", 0);
                    count = count + 1;
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("count", count);
                    String uid = username + count;
                    editor.putString("lastuid", uid);
                    editor.commit();
                    bundle.putString("query", "INSERT INTO `questions`(`question`, `type`, `lat`, `lon`, `answered`, `uid`) VALUES (\"" + question + "\",\"1\",\"" + lat + "\",\"" + lng + "\",\"0\",\"" + uid + "\")");
                    bundle.putString("message", "Question Sent...Refresh to view results");
                    bundle.putString("question", question);
                    Intent i = new Intent(MainActivity.this, WriteQueryDatabase.class);
                    i.putExtras(bundle);

                    JSONObject data;
                    try {
                        data = new JSONObject();
                        data.put("action", "com.op.an.randomapp.UPDATE_STATUS");
                        data.put("question",
                                question);
                        data.put("uid", uid);
                        ParsePush push = new ParsePush();
                        push.setData(data);
                        push.sendInBackground();
                    } catch (JSONException e) {
                        Log.d("json", "error");
                        e.printStackTrace();
                    }

                    startActivity(i);
                }

            }
        });

    }

    private void location() {
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        Criteria criteria = new Criteria();
//        String provider = locationManager.getBestProvider(criteria, true);
//        Location lastKnownLocation = locationManager
//                .getLastKnownLocation(provider);
//        currentLocation = new LatLng(lastKnownLocation.getLatitude(),
//                lastKnownLocation.getLongitude());

        lng = String.valueOf(currentLocation.longitude);
        lat = String.valueOf(currentLocation.latitude);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        map.setOnMapClickListener(this);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15), 2000,
                null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    @Override
    public void onMapClick(LatLng POINT) {
        map.clear();
        lng = String.valueOf(POINT.longitude);
        lat = String.valueOf(POINT.latitude);

        Marker incident = map.addMarker(new MarkerOptions()
                .position(POINT)
                .title("Ask someone here")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.incidentmarker)));
        incident.showInfoWindow();

        // moves camera to specified location
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(POINT, 15), 2000,
                null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
