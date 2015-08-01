package com.op.an.randomapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Login extends Activity {

    EditText user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button b_submit;
        b_submit = (Button) findViewById(R.id.b_submit);
        user = (EditText) findViewById(R.id.et_uid);

        b_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String username = user.getText().toString();
                if (!username.matches("")) {
                    Log.d("logging", "uname: " + username);
                    SharedPreferences settings = getSharedPreferences("preference", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("username", username);
                    editor.putInt("count", 0);
                    editor.commit();
                    Intent i = new Intent("android.intent.action.MAINACTIVITY");
                    startActivity(i);
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
