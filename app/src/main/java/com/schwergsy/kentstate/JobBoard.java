package com.schwergsy.kentstate;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.app.ListActivity;

import com.parse.Parse;
import com.parse.ParseUser;

import java.io.IOException;
import java.text.ParseException;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import android.location.LocationManager;
import android.location.LocationListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.widget.TextView;


public class JobBoard extends ListActivity {

    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private SimpleAdapter adapter;

    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<Object> areas= new ArrayList<Object>();
    private ArrayList<Object> money = new ArrayList<Object>();
    private ArrayList<String> description = new ArrayList<String>();
    private ArrayList<Object> allowance = new ArrayList<Object>();

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] menuItems;
    private String[] btnMenutItems;
    private ActionBarDrawerToggle mDrawerToggle;
    SortType sortType;

    String user_id;

    //in km
    double maxDistance;
    //in minutes
    int minRemainingTime;
    //runner rating out of 5?
    double minRunnerRating;
    double minPayment;
    int jobSortingList;

    //temporary variables, don't really have much to do with JobBoard
    double runnerRating = 0;


    private MyLocation deviceLocation = null;

    private ListView lv;

    private Dialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_job_board);
//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
        showProgressBar("Loading available jobs");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        sortType = new SortType("proximity");

        try {
            maxDistance = Double.parseDouble(sharedPreferences.getString("max_distance", "default"));
            minRemainingTime = Integer.parseInt(sharedPreferences.getString("min_time", "default"));
            minRunnerRating = 0;//Double.parseDouble(sharedPreferences.getString("min_rating", "default"));
            minPayment = Double.parseDouble(sharedPreferences.getString("min_pay", "default"));
            jobSortingList = Integer.parseInt(sharedPreferences.getString("job_sorting_list", "no selection"));

        }
        catch (Exception e) {
            Log.v("Stop that shit", "You dumb as bitch, triflin ass motherfucker");
        }

        if (jobSortingList == 1) {
            sortType = new SortType("proximity");
        } else if (jobSortingList == 2) {
            sortType = new SortType("rating");
        } else if (jobSortingList == 3) {
            sortType = new SortType("payment");
        } else if (jobSortingList == 4) {
            sortType = new SortType("time");
        } else {
            Log.v("AY!", "Stop that shit");
        }

        lv = (ListView) findViewById(android.R.id.list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView title = (TextView) view.findViewById(R.id.title);
                TextView money = (TextView) view.findViewById(R.id.money);
                TextView area = (TextView) view.findViewById(R.id.area);
                TextView description = (TextView) view.findViewById(R.id.description);
                TextView allowance = (TextView) view.findViewById(R.id.allowance);

                Intent intent = new Intent(JobBoard.this, JobDetail.class);
                intent.putExtra("title", title.getText().toString());
                intent.putExtra("money", money.getText().toString());
                intent.putExtra("area", area.getText().toString());
                intent.putExtra("Description", description.getText().toString());
                intent.putExtra("allowance", allowance.getText().toString());
                startActivity(intent);
            }
        });


        getActionBar().setTitle("  Job Board");
        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        android.app.ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2B9BE6")));

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        View hdrview = View.inflate(this, R.layout.menu_header, null);
        mDrawerList.addHeaderView(hdrview);

        menuItems = getResources().getStringArray(R.array.menu_items);
        btnMenutItems = getResources().getStringArray(R.array.button_menu_items);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menuItems));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.threelines_white,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        Parse.initialize(this, "UGeJ5rIPNk2e4IHaMnw8Xu94vy55rZkxqTaws6x2", "2yXx7jFf8gBUnklgXlGKFYmMHt6VYX5N0lbGa8CX");


        try {
            this.initDeviceLocation();
        } catch (YouFuckedUpException e) {
            e.printStackTrace();
        }



        try {
            populateList();
        }
        catch (YouFuckedUpException d){

        }
        catch (IOException f){

        }
    }


    public void showProgressBar(String msg){
        progressDialog = ProgressDialog.show(this, "Please wait...", msg, true);
    }

    public void dismissProgressBar(){
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void initDeviceLocation() throws YouFuckedUpException {

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        deviceLocation = new MyLocation(locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER));

    }



    private void populateList() throws YouFuckedUpException, IOException {
        list.clear();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("database");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> data, com.parse.ParseException e) {
                if (e == null) {

                    ArrayList<JobMetaData> jobMetaDataList = new ArrayList<JobMetaData>();

                    for (ParseObject jobInfo : data) {

                        //job attributes
                        user_id = jobInfo.getString("user_id");
                        String zip = jobInfo.getInt("zipcode") + "";
                        int yearDue = jobInfo.getInt("year");
                        int monthDue = jobInfo.getInt("month");
                        int dayDue = jobInfo.getInt("day");
                        int hourDue = jobInfo.getInt("hour");
                        int minuteDue = jobInfo.getInt("minute");
                        double payment = jobInfo.getDouble("payment");
                        double allowance = jobInfo.getDouble("allowance");

                        //get the matching user and their rating
                        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("User");
                        userQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> userData, com.parse.ParseException e2) {
                                dismissProgressBar();
                                if (e2 == null) {

                                    for (ParseObject userInfo : userData) {
                                        if (user_id.equals(userInfo.getString("username").toString())) {
                                            runnerRating = userInfo.getDouble("Rating");
                                            break;
                                        }
                                    }
                                } else {
                                    e2.printStackTrace();
                                }
                            }
                        });


                        Calendar rightNow = Calendar.getInstance();

                        int currentYear = rightNow.get(Calendar.YEAR);
                        int currentMonth = rightNow.get(Calendar.MONTH);
                        int currentDay = rightNow.get(Calendar.DAY_OF_MONTH);
                        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
                        int currentMinute = rightNow.get(Calendar.MINUTE);

                        Log.v("YOOOOOOOO", "" + currentYear + currentMonth + currentDay + currentHour + currentMinute);

                        int remainingYears = yearDue - currentYear;

                        int remainingDays = (JobBoard.getDayOfYear(monthDue, JobBoard.isLeapYear(yearDue)) + dayDue) -
                                (JobBoard.getDayOfYear(currentMonth, JobBoard.isLeapYear(currentYear)) + currentDay);

                        int remainingHours = hourDue - currentHour;

                        int remainingMinutes = minuteDue - currentMinute;

                        //remaining time in minutes
                        int remainingTime;

                        remainingTime = 365 * remainingYears + remainingDays;
                        remainingTime = 24 * remainingTime + remainingHours;
                        remainingTime = 60 * remainingTime + remainingMinutes;

                        MyLocation jobLocation;

                        //in km
                        double distanceToJob = 100000;

                        try {
                            jobLocation = geoLocate(zip);
                            distanceToJob = deviceLocation.getDistance(jobLocation);
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        } catch (YouFuckedUpException e3) {
                            e3.printStackTrace();
                        }



//                        //TODO user could adjust in preferences their criteria for what jobs would show up in job list
//                        //in km
//                        double maxDistance = 5000;
//                        //in minutes
//                        int minRemainingTime = -1000000000;
//                        //runner rating out of 5?
//                        double minRunnerRating = 0;
//                        double minPayment = 0;

                        if (distanceToJob <= maxDistance &&
                                remainingTime >= minRemainingTime &&
                                runnerRating >= minRunnerRating &&
                                payment >= minPayment &&
                                !(user_id.equals(ParseUser.getCurrentUser().getUsername()))) {
                            Log.i("the user id is: ", user_id);
                            Log.i("Current user is: ",ParseUser.getCurrentUser().getUsername());
                            jobMetaDataList.add(new JobMetaData(jobInfo, distanceToJob, remainingTime, runnerRating, payment, sortType));
                        }
                    }

                    //sort jobMetaDataList by its sort type
                    Collections.sort(jobMetaDataList);

                    for (int i = 0; i < jobMetaDataList.size(); i++) {
                        add(jobMetaDataList.get(i).getJobInfo());
                    }
                }
                else {
                    e.printStackTrace();
                }
            }
        });
    }

    private static boolean isLeapYear(int currentYear) {

        if (currentYear % 4 == 0 && currentYear % 100 != 0)
            return true;
        else
            return false;
    }

    private static int getDayOfYear(int month, boolean isLeapYear) {

        int adjustment = 0;
        if (isLeapYear)
            adjustment = -1;

        switch (month) {
            case 1:
                return 31;
            case 2:
                return 31 + 28 + adjustment;
            case 3:
                return 31 + 28 + 31 + adjustment;
            case 4:
                return 31 + 28 + 31 + 30 + adjustment;
            case 5:
                return 31 + 28 + 31 + 30 + 31 + adjustment;
            case 6:
                return 31 + 28 + 31 + 30 + 31 + 30 + adjustment;
            case 7:
                return 31 + 28 + 31 + 30 + 31 + 30 + 31 + adjustment;
            case 8:
                return 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + adjustment;
            case 9:
                return 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + adjustment;
            case 10:
                return 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + adjustment;
            case 11:
                return 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30 + adjustment;
            case 12:
                return 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30 + 31 + adjustment;
        }


        Log.v("You Fucked up with an invalid month", "beeyotch");

        return -1;
    }


    public void add(ParseObject info) {
        list.clear();

        titles.add(info.get("title").toString());
        areas.add(info.get("zipcode"));
        money.add(info.get("payment"));
        allowance.add(info.get("allowance"));
        description.add(info.get("description").toString());

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
                new int[]{R.id.title, R.id.money, R.id.allowance, R.id.area, R.id.description });

        setListAdapter(adapter);
    }

    public MyLocation geoLocate(String zip) throws IOException, YouFuckedUpException {

        //'this' needs to be a context
        Geocoder gc = new Geocoder(this);

        Log.v("GeoBoolean", String.valueOf(gc.isPresent()));

        List<Address> list  = gc.getFromLocationName(zip, 1);

        Address address = list.get(0);

        //for future implementation, the area the address, city, or landmark is in.
        //String locality = address.getLocality();

        double latitude = address.getLatitude();
        double longitude = address.getLongitude();

        MyLocation location = new MyLocation(latitude, longitude);
        return location;

        //Log.v("Long", String.valueOf(longitude));
        //Log.v("Lat", String.valueOf(latitude));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.job_board, menu);
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
        else if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectItem(final int position) {
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
            selectItem(position);
        }

        private void selectItem(final int position) {
            switch (position) {
                case 1:
                    Intent intent = new Intent(JobBoard.this, addJob.class);
                    startActivity(intent);
                    break;
                case 2:
                    Intent intent2 = new Intent(JobBoard.this, MyJobs.class);
                    startActivity(intent2);
                    break;
                case 3:
                    Intent intent3 = new Intent(JobBoard.this, myRuns.class);
                    startActivity(intent3);
                    break;
                case 4:
                    Intent intent4 = new Intent(JobBoard.this, addJob.class);
                    startActivity(intent4);
                    break;
                case 5:
                    Intent intent5 = new Intent(JobBoard.this, Settings.class);
                    startActivity(intent5);
                    break;
                case 6:
                    ParseUser.logOut();
                    Intent intent6 = new Intent(JobBoard.this, Login.class);
                    startActivity(intent6);
                    JobBoard.this.finish();
                    break;
            }
        }

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
            View rootView = inflater.inflate(R.layout.fragment_job_board, container, false);
            return rootView;
        }
    }
}
