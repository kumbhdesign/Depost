package com.kumbh.design.Epost.Activity;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.kumbh.design.Epost.Adapter.Festival_Adapter;
import com.kumbh.design.Epost.Adapter.Festival_list_adpter;
import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.data.constant;
import com.kumbh.design.Epost.util.SessionManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;

import static com.kumbh.design.Epost.Activity.HomrPage.signOut;
import static com.kumbh.design.Epost.util.SessionManager.PREF_ACCOUNT_NAME;

public class Festival_list_page_normal extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener,GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        ResultCallback<People.LoadPeopleResult>,EasyPermissions.PermissionCallbacks,View.OnClickListener {
    CircleImageView profile;
    List<constant> festivallist;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    static final int REQUEST_AUTHORIZATION = 1001;
    GoogleApiClient google_api_client;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    GoogleAccountCredential mCredential;
    TextView date, fest_name;
    String Title, Date;
    int day_month;
    ProgressBar pb_list;
    private int year;
    private int month;
    String id;
    ProgressDialog pb_festival;
    SessionManager shared_pr;
    public RequestQueue requestQueue;
    Intent i;
    Calendar c;
    View include;
    ImageView loveu_1,anniversary_1,birthday_1,gm_1,gn_1,congratulation_1;
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY, CalendarScopes.CALENDAR};
    AdView adView;
    AdRequest adRequest;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buidNewGoogleApiClient();
        setContentView(R.layout.activity_festival_list_page);

//        MobileAds.initialize(getApplicationContext(),"ca-app-pub-6008474329722648~3510787218");
//
//        adView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice("AC291303B2DCC0629689FF6F8ABD17E1").build();
//        adView.loadAd(adRequest);
        checkConnection();

//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
////                Toast.makeText(Festival_list_page.this,"Ad errorcode:"+String.valueOf(errorCode),Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onAdOpened() {
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//            }
//
//            @Override
//            public void onAdClosed() {
//            }
//        });

        Intent i = getIntent();
        id = i.getStringExtra("id");
        profile = (CircleImageView) findViewById(R.id.profile);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        date = (TextView) findViewById(R.id.date);
        fest_name = (TextView) findViewById(R.id.main_name);
        include = findViewById(R.id.include);

        loveu_1 = (ImageView) findViewById(R.id.loveu_1);
        anniversary_1 = (ImageView) findViewById(R.id.anniversary_1);
        birthday_1 = (ImageView) findViewById(R.id.birthday_1);
        gm_1 = (ImageView) findViewById(R.id.gm_1);
        gn_1 = (ImageView) findViewById(R.id.gn_1);
        congratulation_1 = (ImageView) findViewById(R.id.congratulation_1);

        loveu_1.setOnClickListener(this);
        anniversary_1.setOnClickListener(this);
        birthday_1.setOnClickListener(this);
        gm_1.setOnClickListener(this);
        gn_1.setOnClickListener(this);
        congratulation_1.setOnClickListener(this);

        shared_pr = new SessionManager(getApplicationContext());
        Log.d("photo", shared_pr.getPhoto());
        Log.d("country", shared_pr.getCountry());
        if (!shared_pr.getPhoto().equalsIgnoreCase("null"))
            Picasso.get().load(shared_pr.getPhoto()).into(profile);
        else
            Picasso.get().load(R.drawable.avatar).into(profile);
        requestQueue = Volley.newRequestQueue(this);

        c = Calendar.getInstance();
        pb_list = (ProgressBar) findViewById(R.id.pb_list);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day_month = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.add(Calendar.DAY_OF_MONTH,day_month - 1);
        Log.d("last_day_of_month", String.valueOf(c.getTimeInMillis()));

        pb_festival = new ProgressDialog(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.festival_id);

        festivallist = new ArrayList<>();

        mCredential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        mCredential.setSelectedAccountName(shared_pr.getAccountname());
        getResultsFromApi();

        profile.setOnClickListener(this);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<Event>> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;
        List<Event> items;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential)
                    .setApplicationName("TestPlus")
                    .build();
        }

        @Override
        protected List<Event> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (IOException e) {
                mLastError = e;
                return null;
            }
        }

        private List<Event> getDataFromApi() throws IOException {
            Log.d("currentTimeMillis", String.valueOf(System.currentTimeMillis()));
            DateTime now = new DateTime(System.currentTimeMillis());
            Log.d("now", String.valueOf(now));
            DateTime now1 = new DateTime(c.getTimeInMillis());
            Log.d("now1", String.valueOf(now1));
            List<String> eventStrings = new ArrayList<String>();
            Events events = null;
            try {
                if (id.equalsIgnoreCase("1")) {
                    String con = getCountry();
                    events = mService.events().list("en."+con+"#holiday@group.v.calendar.google.com")
                            .setMaxAttendees(30)
                            .setAlwaysIncludeEmail(true)
                            .setTimeMin(now)
                            .setTimeMax(now1)
                            .setOrderBy("startTime")
                            .setSingleEvents(true)
                            .execute();

                } else if (id.equalsIgnoreCase("2")) {
                    events = mService.events().list("#contacts@group.v.calendar.google.com")
                            .setMaxAttendees(30)
                            .setAlwaysIncludeEmail(true)
                            .setTimeMin(now)
                            .setTimeMax(now1)
                            .setOrderBy("startTime")
                            .setSingleEvents(true)
                            .execute();
                }
                items = events.getItems();
            } catch (UserRecoverableAuthIOException e) {
                startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return items;
        }

        @Override
        protected void onPreExecute() {
            show_dialog("Fetching Data from google...");
        }

        @Override
        protected void onPostExecute(List<Event> output) {
            hide_dialog();
            Log.d("Output_text=", String.valueOf(output));
            if(output != null) {
                if (output.size() != 0) {
                    for (Event event : items) {
                        DateTime start = event.getStart().getDateTime();
                        if (start == null) {
                            start = event.getStart().getDate();
                        }
                        constant c = new constant();
                        c.setFestival_name(event.getSummary());
                        c.setDate(String.valueOf(start));
                        festivallist.add(c);
                    }
                    Festival_list_adpter adapter = new Festival_list_adpter(Festival_list_page_normal.this, festivallist, id);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    include.setVisibility(View.GONE);

                } else {
                    recyclerView.setVisibility(View.GONE);
                    include.setVisibility(View.VISIBLE);
                }
            }else {
                recyclerView.setVisibility(View.GONE);
                include.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onCancelled() {
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(((UserRecoverableAuthIOException) mLastError).getIntent(), Festival_list_page_normal.REQUEST_AUTHORIZATION);
                } else {

                }
            } else {
                Toast.makeText(Festival_list_page_normal.this, "Request cancelled.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getCountry() {
        String c = null;
        if(shared_pr.getCountry().equalsIgnoreCase("india")) {
            c = "indian";
        }else if(shared_pr.getCountry().equalsIgnoreCase("China")){
            c = "china";
        }else if(shared_pr.getCountry().equalsIgnoreCase("United States")){
            c = "usa";
        }else if(shared_pr.getCountry().equalsIgnoreCase("United Kingdom")){
            c = "uk";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Australia")){
            c="australian";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Austria")){
            c = "austrian";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Brazil")){
            c = "brazilian";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Canada")){
            c = "canadian";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Christian")){
            c = "christian";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Denmark")){
            c = "danish";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Netherlands")){
            c = "dutch";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Finland")){
            c = "finnish";
        }else if(shared_pr.getCountry().equalsIgnoreCase("France")){
            c = "french";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Germany")){
            c = "german";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Greece")){
            c = "greek";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Hong Kong")){
            c = "hong_kong";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Indonesia")){
            c = "indonesian";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Ireland")){
            c = "irish";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Muslim")){
            c = "islamic";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Italy")){
            c = "italian";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Japan")){
            c = "japanese";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Israel")){
            c = "jewish";
        }else if(shared_pr.getCountry().equalsIgnoreCase("'Malaysia")){
            c = "malaysia";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Mexico")){
            c = "mexican";
        }else if(shared_pr.getCountry().equalsIgnoreCase("New Zealand")){
            c = "new_zealand";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Norway")){
            c = "norwegian";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Philippines")){
            c = "philippines";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Poland")){
            c = "polish";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Portugal")){
            c = "portuguese";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Russia")){
            c = "russian";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Singapore")){
            c = "singapore";
        }else if(shared_pr.getCountry().equalsIgnoreCase("South Africa")){
            c = "sa";
        }else if(shared_pr.getCountry().equalsIgnoreCase("South Korea")){
            c = "south_korea";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Spain")){
            c = "spain";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Sweden")){
            c = "swedish";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Taiwan")){
            c = "taiwan";
        }else if(shared_pr.getCountry().equalsIgnoreCase("Vietnam")){
            c = "vietnamese";
        }
        return c;
    }

    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(this, android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
            }
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
        }
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    private void showGooglePlayServicesAvailabilityErrorDialog(int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                Festival_list_page_normal.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void buidNewGoogleApiClient() {
        google_api_client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
    }

    protected void onStart() {
        super.onStart();
        google_api_client.connect();
    }

    protected void onStop() {
        super.onStop();
        if (google_api_client.isConnected()) {
            google_api_client.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {

                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    Log.d("accountName", accountName);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
            case REQUEST_PERMISSION_GET_ACCOUNTS:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Toast.makeText(Festival_list_page_normal.this,"Permission Granted....",Toast.LENGTH_LONG).show();
        getResultsFromApi();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Festival_list_page_normal.this, HomrPage.class);
        startActivity(i);
        finish();
    }

    public void show_dialog(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    pb_festival.setMessage(msg);
                    pb_festival.show();
                }
            }
        });
    }

    public void hide_dialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    if (pb_festival != null) {
                        pb_festival.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            if (pb_festival != null) {
                pb_festival.dismiss();
                pb_festival = null;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.loveu_1:
                i = new Intent(Festival_list_page_normal.this,Set_Images.class);
                i.putExtra("festival","loveu");
                startActivity(i);
                break;
            case R.id.anniversary_1:
                i = new Intent(Festival_list_page_normal.this,Set_Images.class);
                i.putExtra("festival","anniversary");
                startActivity(i);
                break;
            case R.id.birthday_1:
                i = new Intent(Festival_list_page_normal.this,Set_Images.class);
                i.putExtra("festival","birthday");
                startActivity(i);
                break;
            case R.id.gm_1:
                i = new Intent(Festival_list_page_normal.this,Set_Images.class);
                i.putExtra("festival","goodmorning");
                startActivity(i);
                break;
            case R.id.gn_1:
                i = new Intent(Festival_list_page_normal.this,Set_Images.class);
                i.putExtra("festival","goodnight");
                startActivity(i);
                break;
            case R.id.congratulation_1:
                i = new Intent(Festival_list_page_normal.this,Set_Images.class);
                i.putExtra("festival","congratulation");
                startActivity(i);
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
                        signOut(Festival_list_page_normal.this);
                        break;

                    case R.id.profile:
                        i= new Intent(getApplicationContext(), Profile.class);
                        startActivity(i);
                        break;
                    case R.id.menycurrentplan:
                        Log.v("print","sdhg");
                        Intent intent = new Intent(getApplicationContext(), currentPlanActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    public boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected(this);
        showSnack(isConnected);
        return isConnected;
    }

    public void showSnack(boolean isConnected) {
        String message;
        if (isConnected) {

        } else {
            message = "Check your Internet connection.";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppController.getInstance().setConnectivityListener(this);

    }

    @Override
    public void onTokenRefresh() {

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}

