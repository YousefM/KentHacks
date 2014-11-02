package com.schwergsy.kentstate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MyJobDetails extends Activity {

    private ParseQuery<ParseObject> query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job_details);


        Bundle extras = getIntent().getExtras();
        final TextView user1 = (TextView) findViewById(R.id.username1);
        final TextView user2 = (TextView) findViewById(R.id.username2);
        final TextView user3 = (TextView) findViewById(R.id.username3);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("database");

        query.whereEqualTo("job_id", extras.getString("Description").substring(17, extras.getString("Description").length()).hashCode());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> data, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject jobInfo : data) {
                        ArrayList<String> runners = (ArrayList) jobInfo.get("runners");
                        if(runners.size() > 0)
                            user1.setText(runners.get(1));
                        if(runners.size() > 1)
                            user2.setText(runners.get(2));
                        if(runners.size() > 2)
                            user3.setText(runners.get(3));
                    }
                }
            }
        });
    }


    public void addfirst(final View view){
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null){
                    for(ParseObject x : parseObjects){
                        TextView text = (TextView) view;
                        x.put("accepted_runner_id", text.getText().toString());
                    }
                }
            }
        });
    }

    public void addsecond(final View view){
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null){
                    for(ParseObject x : parseObjects){
                        TextView text = (TextView) view;
                        x.put("accepted_runner_id", text.getText().toString());
                    }
                }
            }
        });
    }

    public void addthird(final View view){
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null){
                    for(ParseObject x : parseObjects){
                        TextView text = (TextView) view;
                        x.put("accepted_runner_id", text.getText().toString());
                    }
                }
            }
        });
    }


    public void goBack(View view){
        Intent intent = new Intent(this, myRuns.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_job_details, menu);
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
