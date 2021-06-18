package com.kumbh.design.Epost.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kumbh.design.Epost.Adapter.My_event_Adapter;
import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.data.Constant_events;
import com.kumbh.design.Epost.util.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kumbh.design.Epost.Activity.HomrPage.signOut;

public class My_Events extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener,View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{

    FloatingActionButton btn;
    SessionManager shared_pr;
    CircleImageView profile;
    List<Constant_events> myevents;
    RecyclerView recyclerView;
    String mac_id;
    public RequestQueue requestQueue;
    Intent intent;
    ProgressBar pb_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__events);

        btn = (FloatingActionButton)findViewById(R.id.floating_btn);
        btn.bringToFront();
        btn.setOnClickListener(this);
        shared_pr = new SessionManager(getApplicationContext());
        profile = (CircleImageView) findViewById(R.id.profile);
        pb_list = (ProgressBar) findViewById(R.id.pb_list);

        if(!shared_pr.getPhoto().equalsIgnoreCase("null"))
            Picasso.get().load(shared_pr.getPhoto()).into(profile);
        else
            Picasso.get().load(R.drawable.avatar).into(profile);

        profile.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_events);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myevents = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);

        mac_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        System.out.println("android_id=" + mac_id);

        checkConnection();

        list_reminder();



    }

    public void list_reminder(){
        pb_list.setVisibility(View.VISIBLE);
        //   Log.d("API","http://www.kumbhdesign.in/mobile-app/depost/api/reminder_new/list/" + mac_id);
        StringRequest request = new StringRequest(Request.Method.GET, "https://www.kumbhdesign.in/mobile-app/depost/api/reminder/list/" + mac_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("response");
                            JSONObject object1 = array.getJSONObject(0);
                            String error = object1.getString("error");

                            if (error.equals("0")){
                                JSONArray array1 = object1.getJSONArray("message");

                                for (int i = 0; i < array1.length(); i++){
                                    JSONObject object2 = array1.getJSONObject(i);

                                    Constant_events c = new Constant_events();
                                    c.setId(object2.getString("reminder_id"));
                                    Log.d("reminder_id", object2.getString("reminder_id"));
                                    c.setCategory(object2.getString("reminder_category"));
                                    Log.d("reminder_category_api", object2.getString("reminder_category"));
                                    c.setFestival_name(object2.getString("reminder_title"));
                                    c.setDate(object2.getString("reminder_date"));
                                    c.setTime(object2.getString("reminder_time"));
                                    myevents.add(c);
                                }

                                My_event_Adapter adapter = new My_event_Adapter(My_Events.this, myevents);
                                recyclerView.setAdapter(adapter);

                            }else if (error.equals("1")){
                                Toast.makeText(My_Events.this,object1.getString("message"),Toast.LENGTH_LONG).show();
                            }else if (error.equals("2")){
                                Toast.makeText(My_Events.this,object1.getString("message"),Toast.LENGTH_LONG).show();
                            }
                            pb_list.setVisibility(View.GONE);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(My_Events.this,String.valueOf(error),Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(My_Events.this,HomrPage.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.floating_btn:
                intent = new Intent(getApplicationContext(),Add_Reminder.class);
                startActivity(intent);
                finish();
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
                        signOut(My_Events.this);
                        break;

                    case R.id.profile:
                        intent = new Intent(getApplicationContext(), Profile.class);
                        startActivity(intent);
                        break;
//                    case R.id.details:
//                        Log.v("print","sdhg");
//                        Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
//                        startActivity(intent);
//                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    public void onTokenRefresh() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
