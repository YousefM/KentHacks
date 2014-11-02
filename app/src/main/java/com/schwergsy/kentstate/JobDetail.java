package com.schwergsy.kentstate;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class JobDetail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_job_detail);
//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }

        Bundle extras = getIntent().getExtras();
        TextView title = (TextView) findViewById(R.id.textView2);
        TextView money = (TextView) findViewById(R.id.textView3);
        TextView area = (TextView) findViewById(R.id.textView);
        TextView description = (TextView) findViewById(R.id.textView4);
        TextView allowance = (TextView) findViewById(R.id.allowanceAmmount);


        title.setText(extras.getString("title"));
        area.setText(extras.getString("area"));
        description.setText(extras.getString("Description"));
        money.setText(extras.get("money").toString());
        allowance.setText(extras.get("allowance").toString());
    }


    public void addToList(View view){
        Bundle extras = getIntent().getExtras();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("database");
        query.whereEqualTo("job_id", extras.getString("Description").substring(17, extras.getString("Description").length()).hashCode());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject parsedObjects : parseObjects) {
//                        ArrayList<String> b = new parsedObjects.get("runners");
//                        for (int i = 0; i < b.length(); i++) {
//                            b.get(i);
//                        }
                        parsedObjects.add("runners", ParseUser.getCurrentUser().getUsername());
                        parsedObjects.saveInBackground();
                        Log.i("was it saved?", "Yeeeeeeeeeee");
                    }
                }
            }
        });
    }

    public void goBack(View view){
        Intent intent = new Intent(this, JobBoard.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.job_detail, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_job_detail, container, false);
            return rootView;
        }
    }
}
