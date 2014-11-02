package com.schwergsy.kentstate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.text.Spanned;

import com.parse.ParseUser;
import com.schwergsy.kentstate.VenmoLibrary.VenmoResponse;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.parse.Parse;
import com.parse.ParseObject;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class addJob extends FragmentActivity implements OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    public Integer month,day,year,hour,minute,id;
    public String accepted_runner_id = "";
    public ArrayList<String> runners = new ArrayList<String>();
    public EditText allowanceinput, paymentinput, zipcodeinput, descriptioninput, titleinput;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        //initializes parse so we can be hooked up with the backend
        Parse.initialize(this, "UGeJ5rIPNk2e4IHaMnw8Xu94vy55rZkxqTaws6x2", "2yXx7jFf8gBUnklgXlGKFYmMHt6VYX5N0lbGa8CX");

        //initializing views and such
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);

        allowanceinput = (EditText) layout.findViewById(R.id.allowanceinput);
        paymentinput = (EditText) layout.findViewById(R.id.paymentinput);
        zipcodeinput = (EditText) layout.findViewById(R.id.zipcodeinput);
        descriptioninput = (EditText) layout.findViewById(R.id.descriptioninput);
        titleinput = (EditText) layout.findViewById(R.id.titleinput);

        //only allow reasonable amounts of money and 2 decimal places for cents
        allowanceinput.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        paymentinput.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});


        //stuff that handles the nice UI for calendar and time
        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);

        findViewById(R.id.dateButton).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(true);
                datePickerDialog.setYearRange(2014, 2028);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        findViewById(R.id.timeButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.setVibrate(true);
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });

        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }

            TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeSetListener(this);
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int years, int months, int days) {
        year = years;
        month = months;
        day = days;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minutes) {
        minute = minutes;
        hour = hourOfDay;
    }

    public void submitForm(View view) throws JSONException{

        if(month==null||day==null||year==null)
            Toast.makeText(addJob.this, "Please select date of delivery", Toast.LENGTH_LONG).show();
        else if(hour==null||minute==null)
            Toast.makeText(addJob.this, "Please select time of delivery", Toast.LENGTH_LONG).show();
        else if(allowanceinput==null)
            Toast.makeText(addJob.this, "Please enter an amount of allowance for purchase", Toast.LENGTH_LONG).show();
        else if(paymentinput==null)
            Toast.makeText(addJob.this, "Please enter the payment for your runner", Toast.LENGTH_LONG).show();
        else if(zipcodeinput==null)
            Toast.makeText(addJob.this, "Please enter zipcode of delivery area", Toast.LENGTH_LONG).show();
        else if(descriptioninput==null)
            Toast.makeText(addJob.this, "Please enter a description of your job", Toast.LENGTH_LONG).show();
        else if(titleinput==null)
            Toast.makeText(addJob.this, "Please enter a title of your job", Toast.LENGTH_LONG).show();

        else {

            id = descriptioninput.getText().toString().hashCode();
            if (id < 0)
                id *= -1;

            double total = Double.parseDouble(allowanceinput.getText().toString()) + Double.parseDouble(paymentinput.getText().toString());
            String amount = "" + total;
            String note = "" + id;


            //venmo will try to do payment stuff, if successful, then moves to pushing info to parse
            try {
                Intent venmoIntent = VenmoLibrary.openVenmoPayment("2052", "testapp", "pkhxcp@gmail.com", amount, note, "pay");
                startActivityForResult(venmoIntent, 1); //1 is the requestCode we are using for Venmo. Feel free to change this to another number.
            } catch (android.content.ActivityNotFoundException e) //Venmo native app not install on device, so let's instead open a mobile web version of Venmo in a WebView
            {
                Intent venmoIntent = new Intent(addJob.this, VenmoWebViewActivity.class);
                String venmo_uri = VenmoLibrary.openVenmoPaymentInWebView("2052", "testapp", "pkhxcp@gmail.com", amount, note, "pay");
                venmoIntent.putExtra("url", venmo_uri);
                startActivityForResult(venmoIntent, 1);
            }
        }

    }


    //the result method of when venmo is finished initializing its stuff
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode) {
            case 1: { //1 is the requestCode we picked for Venmo earlier when we called startActivityForResult
                if(resultCode == RESULT_OK) {
                    String signedrequest = data.getStringExtra("signedrequest");
                    if(signedrequest != null) {
                        VenmoResponse response = (new VenmoLibrary()).validateVenmoPaymentResponse(signedrequest, "yUy7yw4mMVuDHWz4tnXL4DwYSPsSQQqF");
                        if(response.getSuccess().equals("1")) {
                            //Payment successful. data is pushed to parse

                            ParseObject info = new ParseObject("database");

                            info.put("job_id", id);
                            info.put("title", titleinput.getText().toString());
                            info.put("month", month);
                            info.put("year", year);
                            info.put("day", day);
                            info.put("hour", hour);
                            info.put("minute", minute);
                            info.put("allowance",Double.parseDouble(allowanceinput.getText().toString()));
                            info.put("payment", Double.parseDouble(paymentinput.getText().toString()));
                            info.put("zipcode", Integer.parseInt(zipcodeinput.getText().toString()));
                            info.put("description", descriptioninput.getText().toString());
                            info.put("user_id", ParseUser.getCurrentUser().getUsername().toString());
                            info.put("accepted_runner_id", accepted_runner_id);
                            info.put("runners", runners);

                            //job state 0 means no runner has been accepted. 1 is in progress. 2 is finished.
                            info.put("job_state", 0);
                            info.saveInBackground();
                            Toast.makeText(addJob.this, "yo bro it went thru", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(addJob.this, JobBoard.class);
                            startActivity(intent);
                        }
                    }
                    else {
                        String error_message = data.getStringExtra("error_message");
                        //An error ocurred.  Make sure to display the error_message to the user
                    }
                }
                else if(resultCode == RESULT_CANCELED) {
                    //The user cancelled the payment
                }
                break;
            }
        }
    }

    //The class that handles decimal digits for allowance and payment
    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }
}

