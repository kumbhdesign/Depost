package com.kumbh.design.Epost.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.droidninja.imageeditengine.utils.SessionDataManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.util.SessionManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
public class LoginPage extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener{

    private static final String TAG = LoginPage.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    ViewPager viewPager;
    FrameLayout frame;
    private MyViewPagerAdapter myViewPagerAdapter;
    static GoogleSignInClient mGoogleSignInClient;
    SignInButton signIn_btn;
    public RequestQueue requestQueue;
    String android_id;
    String countryCodeValue;
    String fullCountryName;
    ProgressDialog pDialog;
    SessionManager shared_pr;
    SessionDataManager dataManager;
    Button buttonDisplayToken;
    TextView textViewToken;
    String token ;
    String refreshedToken;
    boolean Button_Click = false;
    List<Integer> img;
    int currentIndex = 0;
    public static final int PERMISSION_REQUEST_CODE1 = 1;
    private String gmailUrl = "https://www.kumbhdesign.in/mobile-app/depost/api/gmail-login";
    double lat,lang;
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
  //  private LocationRequest mLocationRequest;
 //   private LocationSettingsRequest mLocationSettingsRequest;
//    private LocationCallback mLocationCallback;
  //  private Location mCurrentLocation;
    LocationManager locationManager;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_login_page);
        shared_pr = new SessionManager(getApplicationContext());
        dataManager=new SessionDataManager(getApplicationContext());
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
         countryCodeValue = tm.getNetworkCountryIso();

         fullCountryName = LoginPage.getCountryNameFromIso2Code(getApplicationContext(), countryCodeValue);
     //   //Log.d("c_c",fullCountryName);

        signIn_btn = (SignInButton) findViewById(R.id.sign_in_button);
        pDialog = new ProgressDialog(this);
        signIn_btn.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        frame = (FrameLayout) findViewById(R.id.frame);

        textViewToken = (TextView) findViewById(R.id.textViewToken);
        buttonDisplayToken = (Button) findViewById(R.id.buttonDisplayToken);
        requestQueue = Volley.newRequestQueue(this);

        if (shared_pr.getDeviceToken().equalsIgnoreCase("null")) {
            token = FirebaseInstanceId.getInstance().getToken();
        } else {
            token = shared_pr.getDeviceToken();
        }

        changeStatusBarColor();
        img = new ArrayList();
        img.add(R.drawable.banner_1);
        img.add(R.drawable.banner_2);
        img.add(R.drawable.banner_3);

        myViewPagerAdapter = new MyViewPagerAdapter(img);
        viewPager.setAdapter(myViewPagerAdapter);
        autoslider();

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
      //  //Log.d("midd",android_id);
        System.out.println("android_id=" + android_id);
        System.out.println("token==" + token);

     /*   if (!checkPermission()) {
            requestPermission();
        }*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
       // init();
    }

    public boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected(this);
        showSnack(isConnected);
        return isConnected;
    }
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
         refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Refreshed" , refreshedToken);
        token = refreshedToken;
        Log.d("tocken_id",token);
        shared_pr.saveDeviceToken(token);
    }
    public void showSnack(boolean isConnected) {
        String message;
        if (isConnected) {
        } else {
            message = "Check your Internet connection";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        List<Integer> list_image;
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter(List<Integer> list) {
            this.list_image = list;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.indicator_1, container, false);
            ImageView image = view.findViewById(R.id.image_banner);
            image.setImageDrawable(getResources().getDrawable(img.get(position)));
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return img.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private void login(final String name, final String id, final String type, final String country) {
        pDialog.setMessage("Logging...");
        pDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, gmailUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //{"response":[{"error":0,"message":"success"}]}
              //  //Log.d("response", response);
                pDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("response");

                    JSONObject object1 = array.getJSONObject(0);
                    String error = object1.getString("error");
                    String message = object1.getString("message");
                    JSONObject jsonObject1 = object1.getJSONObject("user_data");



                    if (error.equals("0")) {
                        shared_pr.setLogin(true);
                        shared_pr.setCountry(fullCountryName);
                        shared_pr.setUserId(jsonObject1.getString("user_id"));
                        dataManager.setUserId(jsonObject1.getString("user_id"));
                        //Log.d("login", String.valueOf(shared_pr.isLoggedIn()));
                        Toast.makeText(LoginPage.this, message, Toast.LENGTH_LONG).show();

                        Toast.makeText(LoginPage.this, "Your Login Is Successfully....", Toast.LENGTH_SHORT).show();
//                        shared_pr.setUserId(object1.getJSONObject("user_data").getString("user_id"));
                        Intent intent = new Intent(LoginPage.this, HomrPage.class);
                        startActivity(intent);
                        finish();

                    } else if (error.equals(1)) {
                        Toast.makeText(LoginPage.this, "Login Failed....", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // //Log.d(TAG, "error", error);
                pDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> param = new HashMap<>();
                param.put("device_id", android_id);
                //Log.d("p_id",android_id);
                param.put("name", name);
                param.put("login_type", type);
                //Log.d("login_type", type);
                param.put("unique_id", id);
                param.put("token", token);
                //Log.d("token", token);
                param.put("country", country);
                //Log.d("country", country);
                return param;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> param = new HashMap<>();
                param.put("X-API-KEY", "AIzaSyBzMOsX23bqB0Gnj1DedRvrRkt5ovgG6A4");
                return param;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.sign_in_button:
                Button_Click = true;
                            checkConnection();
                            signIn();


                break;
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode!=RESULT_CANCELED)
        {
            if (requestCode == RC_SIGN_IN) {

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {
                String personName = account.getDisplayName();
                shared_pr.setName(personName);
                String personEmail = account.getEmail();
                shared_pr.setAccountname(personEmail);
                Uri personPhoto = account.getPhotoUrl();

      /*          //Log.d("personName", personName);
                //Log.d("personGivenName", personGivenName);
                //Log.d("personFamilyName", personFamilyName);
                //Log.d("personEmail", personEmail);
                //Log.d("personId", personId);*/
              /*  if (personPhoto != null) {
                    //Log.d("personPhoto", String.valueOf(personPhoto));
                }*/

                shared_pr.setEmail(personEmail);
                shared_pr.setPhoto(String.valueOf(personPhoto));
                shared_pr.setName(personName);
                if (token != null) {

            //        String country = getCountryName(LoginPage.this,lat,lang);
                    shared_pr.setCountry(fullCountryName);
                    login(personName, personEmail, "gmail", fullCountryName);
                } else {
                    Toast.makeText(LoginPage.this, "Something wents to wrong,Try again leter.", Toast.LENGTH_LONG).show();
                }

            }
        } catch (ApiException e) {
/*            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Log.w(TAG, "signInResult:failed code=" + String.valueOf(e));*/
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
    public void onBackPressed() {
        finishAffinity();
    }

    public void autoslider() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentIndex == img.size()) {
                    currentIndex = 0;
                }
                viewPager.setCurrentItem(currentIndex++, true);
            }
        };

        Timer timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 500, 3000);
    }


    public static String getCountryNameFromIso2Code(Context context, String iso2Code) {
        if(context == null || TextUtils.isEmpty(iso2Code)) return "";

        String key = String.format(Locale.US, "%s%s", iso2Code.toLowerCase(Locale.US), "_country");
        int resId = context.getResources().getIdentifier(key, "string", context.getPackageName());

        return context.getString(resId);
    }

}