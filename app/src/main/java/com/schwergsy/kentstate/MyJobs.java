package com.schwergsy.kentstate;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyJobs extends ListActivity{

    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private SimpleAdapter adapter;
    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<Object> areas = new ArrayList<Object>();
    private ArrayList<Object> money = new ArrayList<Object>();
    private ArrayList<String> description = new ArrayList<String>();
    private ArrayList<Object> allowance = new ArrayList<Object>();

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jobs);

        lv = (ListView) findViewById(android.R.id.list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView description = (TextView) view.findViewById(R.id.description);


                Intent intent = new Intent(MyJobs.this, MyJobDetails.class);
                intent.putExtra("Description", description.getText().toString());
                startActivity(intent);
            }
        });

        populateList();
    }

    public void populateList() {
        list.clear();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("database");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> data, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject jobInfo : data) {
                        if (jobInfo.get("user_id").toString().equals(ParseUser.getCurrentUser().getUsername().toString())) {
                            add(jobInfo);
                        }
                    }
                }
            }

        });
    }

    public void add(ParseObject info) {
        list.clear();

        titles.add(info.get("title").toString());
        areas.add(info.get("zipcode"));
        money.add(info.get("payment"));
        description.add(info.get("description").toString());
        allowance.add(info.get("allowance"));

        if (titles.size() > 0) {
            for (int i = 0; i < titles.size(); i++) {
                HashMap<String, String> temp = new HashMap<String, String>();

                temp.put("title", titles.get(i));
                temp.put("area", "Postal zip: " + areas.get(i).toString());
                temp.put("money", "Payment: $" + money.get(i).toString());
                temp.put("description", "Job Description: " + description.get(i));
                temp.put("allowance", "Allowance: $" + allowance.get(i).toString());

                list.add(temp);
            }
        }

        adapter = new SimpleAdapter(this, list, R.layout.custom_row, new String[]{"title", "money", "allowance", "area", "description"},
                new int[]{R.id.title, R.id.money, R.id.allowance, R.id.area, R.id.description});

        setListAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_jobs, menu);
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
