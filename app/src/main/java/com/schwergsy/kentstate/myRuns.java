package com.schwergsy.kentstate;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class myRuns extends ListActivity {

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
        setContentView(R.layout.fragment_my_runs);
//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }

        lv = (ListView) findViewById(android.R.id.list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView title = (TextView) view.findViewById(R.id.title);
                TextView money = (TextView) view.findViewById(R.id.money);
                TextView area = (TextView) view.findViewById(R.id.area);
                TextView description = (TextView) view.findViewById(R.id.description);
                TextView allowance = (TextView) view.findViewById(R.id.allowance);

                Intent intent = new Intent(myRuns.this, JobDetailRuns.class);
                intent.putExtra("title", title.getText().toString());
                intent.putExtra("money", money.getText().toString());
                intent.putExtra("area", area.getText().toString());
                intent.putExtra("Description", description.getText().toString());
                intent.putExtra("allowance", allowance.getText().toString());
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
                        ArrayList<String> runners = (ArrayList) jobInfo.get("runners");
                        for (int i = 0; i < runners.size(); i++) {
                            if (ParseUser.getCurrentUser().getUsername().toString().equals(runners.get(i).toString())) {
                                add(jobInfo);
                                break;
                            }
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
        getMenuInflater().inflate(R.menu.my_runs, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_my_runs, container, false);
            return rootView;
        }
    }
}
