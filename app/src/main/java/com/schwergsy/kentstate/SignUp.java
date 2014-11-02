package com.schwergsy.kentstate;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;


public class SignUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Parse.initialize(this, "UGeJ5rIPNk2e4IHaMnw8Xu94vy55rZkxqTaws6x2", "2yXx7jFf8gBUnklgXlGKFYmMHt6VYX5N0lbGa8CX");

        android.app.ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2B9BE6")));
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void cancel(View v) {
        finish();
    }

    public void done(View v) {
        ParseUser uniqueUser = new ParseUser();

        EditText username = (EditText)findViewById(R.id.signup_username);
        final EditText password = (EditText)findViewById(R.id.signup_password);
        EditText email = (EditText)findViewById(R.id.email);

        ParseObject info = new ParseObject("userinfo");

        ArrayList<Float> customer_ratings = new ArrayList<Float>();
        ArrayList<Float> runner_ratings = new ArrayList<Float>();

        info.put("username", username.getText().toString());
        info.put("email", email.getText().toString());
        info.put("customer_ratings", customer_ratings);
        info.put("runner_ratings", runner_ratings);
        info.saveInBackground();

        //Test
        Log.v("String", username.toString());

        uniqueUser.setUsername(username.getText().toString());
        uniqueUser.setPassword(password.getText().toString());
        uniqueUser.setEmail(email.getText().toString());



        uniqueUser.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null && password.length() >= 5) {
                    // Hooray! Let them use the app now.

                    Log.v("Signup", "TESTETSTETS");
                    Toast t = Toast.makeText(SignUp.this, "You are ready to start sharing!", Toast.LENGTH_LONG);
                    t.show();
                    finish();
                } else {
                    Log.v("WTF", "EEEEEEEEEEEE");
                    Log.v("ts", e.toString());
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    if(password.length() < 5) {
                        Toast t = Toast.makeText(SignUp.this, "Password must be at least 5 characters", Toast.LENGTH_LONG);
                        t.show();
                    }
                    else {
                        Toast t = Toast.makeText(SignUp.this, "Invalid username or email", Toast.LENGTH_LONG);
                        t.show();
                    }
                    e.printStackTrace();
                }
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up, menu);
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


