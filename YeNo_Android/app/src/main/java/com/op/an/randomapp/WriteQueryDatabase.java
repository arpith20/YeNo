package com.op.an.randomapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WriteQueryDatabase extends Activity {


    String query, message, uid, yescount, nocount, uid2;
    Boolean suc;
    // Progress Dialog
    TextView tv_message, tv_yes, tv_no;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    Button b_refresh;

    private static String url_query, url_query2, question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_query_database);

        SharedPreferences settings = getSharedPreferences("preference", 0);
        uid = settings.getString("lastuid", "");





        tv_message = (TextView) findViewById(R.id.tv_message);
        tv_yes = (TextView) findViewById(R.id.tv_yes);
        tv_no = (TextView) findViewById(R.id.tv_no);
        b_refresh = (Button) findViewById(R.id.b_refresh);

        query = "";
        Bundle bundle = this.getIntent().getExtras();
        query = bundle.getString("query");
        message = bundle.getString("message");
        question = bundle.getString("question");
        Toast.makeText(getBaseContext(),message,Toast.LENGTH_SHORT).show();
        notification();
        tv_message.setText(message);
        url_query = "http://192.168.43.111/kl/query.php";
        url_query2 = "http://192.168.43.111/kl/result.php";
        new AsyncWriteDatabase().execute();

        b_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("preference", 0);
                uid = settings.getString("lastuid", "");
                new AsyncReadDatabase().execute();
            }
        });
    }

    public void notification(){

        Intent resultIntentyes = new Intent(this, WriteQueryDatabase.class);
        Bundle bundle3 = new Bundle();
        bundle3.putString("query", "update questions set yescount = yescount+1 where uid = \""+uid+"\"");
        Toast.makeText(getBaseContext(),uid,Toast.LENGTH_SHORT).show();
        bundle3.putString("message", "You answered Yes");
        resultIntentyes.putExtras(bundle3);
        TaskStackBuilder stackBuilderyes = TaskStackBuilder.create(this);
        stackBuilderyes.addParentStack(WriteQueryDatabase.class);
        stackBuilderyes.addNextIntent(resultIntentyes);
        PendingIntent resultPendingIntentyes =
                stackBuilderyes.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Intent resultIntentno = new Intent(this, WriteQueryDatabase.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("query", "update questions set nocount = nocount+1 where uid = \""+uid+"\"");
        bundle2.putString("message", "You answered No");
        resultIntentno.putExtras(bundle2);
        TaskStackBuilder stackBuilderno = TaskStackBuilder.create(this);
        stackBuilderno.addParentStack(WriteQueryDatabase.class);
        stackBuilderno.addNextIntent(resultIntentno);
        PendingIntent resultPendingIntentno =
                stackBuilderno.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.incidentmarker)
                        .setContentTitle(question)
                        .setContentText("Swipe to answer")
                        .addAction(R.drawable.nothing, "Yes", resultPendingIntentyes)
                        .addAction(R.drawable.nothing, "No", resultPendingIntentno)
                        .addAction(R.drawable.nothing, "Spam", resultPendingIntentyes);


        mBuilder.setContentIntent(resultPendingIntentyes);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());

    }

    class AsyncWriteDatabase extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(WriteQueryDatabase.this);
            pDialog.setMessage("Writing to Database...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("query", query));

            Log.d("Create Response", "Works till here");

            JSONObject json = jsonParser.makeHttpRequest(url_query, "POST",
                    params);

            // check log cat for response
            Log.d("Create Response", query + "  " + json.toString());
            message = json.toString();
            // check for success tag
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    suc = true;
                } else {
                    suc = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if (!suc) {
                tv_message.setText("Something went wrong:  " + message);
            }
        }

    }

    class AsyncReadDatabase extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(WriteQueryDatabase.this);
            pDialog.setMessage("Reading the Database...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("uid", uid));


            Log.d("Create Response", "Works till here" + uid);

            JSONObject json = jsonParser.makeHttpRequest(url_query2, "GET",
                    params);

            // check log cat for response
            Log.d("Create Response", uid + "  " + json.toString());
            message = json.toString();
            // check for success tag
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    suc = true;
                } else {
                    suc = false;
                }
                yescount = json.getString("yes");
                nocount = json.getString("no");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if (!suc) {
                tv_message.setText("Something went wrong:  " + message);
            } else {
                tv_yes.setText("Yes: " + yescount);
                tv_no.setText("No: " + nocount);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write_query_database, menu);
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
    protected void onPause() {
        super.onPause();
        finish();
    }
}
