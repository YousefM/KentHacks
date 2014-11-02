package com.schwergsy.kentstate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class Login extends Activity {

    ParseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            currentUser = ParseUser.getCurrentUser();
        }
        catch(Exception e){

        }
        if (currentUser != null) {
            if(currentUser.isAuthenticated()) {
                Intent intent = new Intent(Login.this, JobBoard.class);
                startActivity(intent);
                Login.this.finish();
            }
        } else
        {
            setContentView(R.layout.activity_login);
        }


        android.app.ActionBar bar = getActionBar();
        bar.hide();

        Parse.initialize(this, "UGeJ5rIPNk2e4IHaMnw8Xu94vy55rZkxqTaws6x2", "2yXx7jFf8gBUnklgXlGKFYmMHt6VYX5N0lbGa8CX");

    }

    public void goToSignUp(View v) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void signIn(View view){


        EditText username = (EditText)findViewById(R.id.username);
        EditText password = (EditText)findViewById(R.id.password);
        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {

            @Override
            public void done(ParseUser uniqueUser, ParseException e) {
                // TODO Auto-generated method stub
                if (uniqueUser != null) {
                    // Hooray! The user is logged in.
                    currentUser = ParseUser.getCurrentUser();
                    Intent intent = new Intent(Login.this, JobBoard.class);
                    startActivity(intent);
                    Login.this.finish();
                }
                else {
                    // Sign up failed. Look at the ParseException to see what happened.

                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setMessage(R.string.invalid);
                    builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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

