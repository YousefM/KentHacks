package com.schwergsy.kentstate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class JobDetailRuns extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail_runs);

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

    public void goBack(View view){
        Intent intent = new Intent(this, myRuns.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.job_detail_runs, menu);
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
