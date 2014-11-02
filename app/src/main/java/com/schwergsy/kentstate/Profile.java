package com.schwergsy.kentstate;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseUser;


public class Profile extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView user = (TextView)findViewById(R.id.username);
        Parse.initialize(this, "UGeJ5rIPNk2e4IHaMnw8Xu94vy55rZkxqTaws6x2", "2yXx7jFf8gBUnklgXlGKFYmMHt6VYX5N0lbGa8CX");

        String currentUser = ParseUser.getCurrentUser().getUsername().toString().toUpperCase();
        user.setText(currentUser);
        user.setTextSize(30);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
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
