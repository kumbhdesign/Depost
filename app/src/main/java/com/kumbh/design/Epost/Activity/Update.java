package com.kumbh.design.Epost.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.util.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kumbh.design.Epost.Activity.HomrPage.signOut;

public class Update extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener{

    String[] festival = {"Birthday", "Love", "Good Morning", "Good Night","Anniversary", "Congratulations"};
    Spinner spinner;
    EditText event_title;
    Button save;
    SessionManager shared_pr;
    CircleImageView profile;
    TextView datepicker, timepicker;
    TextView date_txt, time_txt;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Calendar c;
    AdView adView;
    AdRequest adRequest;
    String android_id,reminder_id,reminder_category,reminder_title,reminder_date,reminder_time;
    public RequestQueue requestQueue;
    Intent i;
    InterstitialAd mInterstitialAd;


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        MobileAds.initialize(getApplicationContext(),"ca-app-pub-3875860493017976~4023292410");

        adView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().addTestDevice("AC291303B2DCC0629689FF6F8ABD17E1").build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
              //  Toast.makeText(Update.this,"Ad errorcode:"+String.valueOf(errorCode),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdClosed() {
            }
        });

        mInterstitialAd = new InterstitialAd(Update.this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId("ca-app-pub-6008474329722648/5562235488");

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
//                Toast.makeText(Add_Reminder.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();

                if(mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("Reason", String.valueOf(i));
//                Toast.makeText(Add_Reminder.this, "onAdFailedToLoad()", Toast.LENGTH_SHORT).show();
            }
        });

        mInterstitialAd.loadAd(adRequest);

        event_title = (EditText)findViewById(R.id.event_title);
        spinner = (Spinner) findViewById(R.id.category_spinner);
        save = (Button) findViewById(R.id.save);

        shared_pr = new SessionManager(getApplicationContext());
        profile = (CircleImageView) findViewById(R.id.profile);

        c = Calendar.getInstance();

        datepicker = (TextView) findViewById(R.id.datepicker);
        timepicker = (TextView) findViewById(R.id.timepicker);

        date_txt = (TextView) findViewById(R.id.date_txt);
        time_txt = (TextView) findViewById(R.id.time_txt);

        datepicker.setOnClickListener(this);
        timepicker.setOnClickListener(this);

        i = getIntent();
        reminder_id = i.getStringExtra("reminder_id");
        reminder_category = i.getStringExtra("reminder_category");

        reminder_title = i.getStringExtra("reminder_title");
        event_title.setText(reminder_title);
        reminder_date = i.getStringExtra("reminder_date");
        datepicker.setText(reminder_date);
        reminder_time = i.getStringExtra("reminder_time");
        timepicker.setText(convertTo12Hour(reminder_time));

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        System.out.println("android_id=" + android_id);

        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.custom_spinner,festival);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        for(int i = 0;i<festival.length;i++){
            Log.d("festival[i]",festival[i]);
            Log.d("reminder_category",reminder_category);
            if(reminder_category.equalsIgnoreCase(festival[i])){
                Log.d("selectedposition", String.valueOf(i));
                spinner.setSelection(i);
            }
        }

        if (!shared_pr.getPhoto().equalsIgnoreCase("null"))
            Picasso.get().load(shared_pr.getPhoto()).into(profile);
        else
            Picasso.get().load(R.drawable.avatar).into(profile);

        profile.setOnClickListener(this);

        save.setOnClickListener(this);

        requestQueue = Volley.newRequestQueue(this);

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.datepicker:
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datepicker.setText(parseDateToddMMyyyy(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            case R.id.timepicker:
                c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                timepicker.setText(getTime(hourOfDay,minute));
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            case R.id.save:
                Log.d("update_vallue", festival[spinner.getSelectedItemPosition()]);
                update(reminder_id,android_id, festival[spinner.getSelectedItemPosition()],event_title.getText().toString(),datepicker.getText().toString(),timepicker.getText().toString());
                break;
            case R.id.profile :
                showFilterPopup(v);
                break;
        }
    }

    private void showFilterPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuLogout:
                        signOut(Update.this);
                        break;

                    case R.id.profile:
                        i = new Intent(getApplicationContext(), Profile.class);
                        startActivity(i);
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    public void update(String reminder_id, String android_id, String reminder_category, String reminder_title, String reminder_date, String reminder_time) {
        StringRequest request = new StringRequest(Request.Method.POST,
                "https://www.kumbhdesign.in/mobile-app/depost/api/reminder/update",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("response");
                            JSONObject object1 = array.getJSONObject(0);
                            String error = object1.getString("error");

                            if (error.equals("0")){
                                Toast.makeText(Update.this,object1.getString("message"),Toast.LENGTH_LONG).show();

                                i = new Intent(Update.this, My_Events.class);
                                startActivity(i);
                                finish();

                            }else if (error.equals("1")){
                                Toast.makeText(Update.this,object1.getString("message"),Toast.LENGTH_LONG).show();
                            }else if (error.equals("2")){
                                Toast.makeText(Update.this,object1.getString("message"),Toast.LENGTH_LONG).show();
                            }else if (error.equals("3")){
                                Toast.makeText(Update.this,object1.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Update.this,String.valueOf(error),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("reminder_id",reminder_id);
                Log.d("reminder_id",reminder_id);
                param.put("mac_id",android_id);
                Log.d("mac_id",android_id);
                param.put("reminder_category", reminder_category);
                Log.d("reminder_category", reminder_category);
                param.put("reminder_title", reminder_title);
                Log.d("reminder_title", reminder_title);
                param.put("reminder_date", reminder_date);
                Log.d("reminder_date", reminder_date);
                param.put("reminder_time", convertTo24Hour(reminder_time));
                Log.d("reminder_time", convertTo24Hour(reminder_time));
                return param;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        i = new Intent(Update.this,My_Events.class);
        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this, festival[position], Toast.LENGTH_SHORT).show();

        String item = parent.getItemAtPosition(position).toString();

        if (item.equalsIgnoreCase("Festival")){
//            festival_spinner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "d-M-yyyy";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date).toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str.trim();
    }

    public static String convertTo24Hour(String Time) {
        DateFormat f1 = new SimpleDateFormat("hh:mm a"); //11:00 pm
        Date d = null;
        try {
            d = f1.parse(Time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("HH:mm");
        String x = f2.format(d); // "23:00"

        return x;
    }

    public static String convertTo12Hour(String Time) {
        DateFormat f1 = new SimpleDateFormat("HH:mm"); //11:00 pm
        Date d = null;
        try {
            d = f1.parse(Time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("hh:mm a");
        String x = f2.format(d); // "23:00"

        return x;
    }

    private String getTime(int hr,int min) {
        Time tme = new Time(hr,min,0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(tme);
    }
}
