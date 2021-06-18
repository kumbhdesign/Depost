package com.kumbh.design.Epost.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.droidninja.imageeditengine.model.UserData;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.util.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_CALENDAR;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HomrPage extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    CircleImageView profile;
    private static final String TAG = HomrPage.class.getSimpleName();
    LinearLayout festival_ly, birthday_ly, loveu_ly, anniversary_ly, gm_ly, gn_ly, congo_ly, events_ly;
    ImageView festival_img, loveu, anniversary, birthday_img, gm, gn, congo, my_events;
    TextView festival_txt, loveu_txt, anniversary_txt, birthday_txt, gm_txt, gn_txt, congo_txt, my_events_txt;
    int year, month;
    View view;
    static SessionManager shared_pr;
    public RequestQueue requestQueue;
    Intent intent;
    private UserData user;
    Typeface tf;
    static GoogleApiClient mgoogleApiClient;
    private String festival_list_URL = " https://www.googleapis.com/calendar/v3/calendars/calendarId/events";
    public static final int PERMISSION_REQUEST_CODE = 200;
    Button bt_go_premium;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homr_page);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        shared_pr = new SessionManager(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.excludeTarget(R.id.tb_home, true);
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }
        getData();
        festival_ly = (LinearLayout) findViewById(R.id.festival_ly);
        birthday_ly = (LinearLayout) findViewById(R.id.birthday_ly);
        loveu_ly = (LinearLayout) findViewById(R.id.loveu_ly);
        anniversary_ly = (LinearLayout) findViewById(R.id.anniversary_ly);
        gm_ly = (LinearLayout) findViewById(R.id.gm_ly);
        gn_ly = (LinearLayout) findViewById(R.id.gn_ly);
        congo_ly = (LinearLayout) findViewById(R.id.congo_ly);
        events_ly = (LinearLayout) findViewById(R.id.reminder_ly);
        bt_go_premium=findViewById(R.id.bt_go_premium);

        festival_img = (ImageView) findViewById(R.id.festival_img);
        loveu = (ImageView) findViewById(R.id.loveu_img);
        anniversary = (ImageView) findViewById(R.id.imageView_anniversary);
        birthday_img = (ImageView) findViewById(R.id.birthday_img);
        gm = (ImageView) findViewById(R.id.imageView_gm);
        gn = (ImageView) findViewById(R.id.imageView_gn);
        congo = (ImageView) findViewById(R.id.congo_img);
        my_events = (ImageView) findViewById(R.id.my_events_image);

        festival_txt = (TextView) findViewById(R.id.textView_festival);
        birthday_txt = (TextView) findViewById(R.id.birthday_text);
        loveu_txt = (TextView) findViewById(R.id.loveu_text);
        anniversary_txt = (TextView) findViewById(R.id.anniversary_text);
        gm_txt = (TextView) findViewById(R.id.gm_text);
        gn_txt = (TextView) findViewById(R.id.gn_text);
        congo_txt = (TextView) findViewById(R.id.textView_congo);
        my_events_txt = (TextView) findViewById(R.id.reminder_text);
        profile = (CircleImageView) findViewById(R.id.profile);

        tf = Typeface.createFromAsset(getAssets(), "fonts/swift_lt.ttf");
        festival_txt.setTypeface(tf);
        birthday_txt.setTypeface(tf);
        loveu_txt.setTypeface(tf);
        anniversary_txt.setTypeface(tf);
        gm_txt.setTypeface(tf);
        gn_txt.setTypeface(tf);
        congo_txt.setTypeface(tf);
        my_events_txt.setTypeface(tf);

        requestQueue = Volley.newRequestQueue(this);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);

        didTapButton(view);

        festival_ly.setOnClickListener(this);
        birthday_ly.setOnClickListener(this);
        loveu_ly.setOnClickListener(this);
        anniversary_ly.setOnClickListener(this);
        gm_ly.setOnClickListener(this);
        gn_ly.setOnClickListener(this);
        congo_ly.setOnClickListener(this);
        events_ly.setOnClickListener(this);
        profile.setOnClickListener(this);
        bt_go_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                String path= shared_pr.getLogoPhoto();

                if(user!=null && user.getCompanyEmail()!=null)
                {
                    intent = new Intent(getApplicationContext(), get_premium_activity.class);
                    intent.putExtra("id", "1");
                    startActivity(intent);
                }
                else{

                    Toast.makeText(HomrPage.this,"Please complete profile detail",Toast.LENGTH_LONG).show();
                    intent = new Intent(getApplicationContext(), Profile.class);
                    intent.putExtra("id", "1");
                    startActivity(intent);
                }


            }
        });

        changeStatusBarColor();

        if (!checkPermission()) {
            requestPermission();
        }

        if (!shared_pr.getPhoto().toString().equalsIgnoreCase("null"))
            Picasso.get().load(shared_pr.getPhoto()).into(profile);
        else
            Picasso.get().load(R.drawable.avatar).into(profile);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mgoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }

    private void getData() {
        String userId = shared_pr.getUserId();

        //  Log.d("DATA_URL","https://api.qwant.com/api/search/images?count=50&q="+festival+"+backgrounds&t=images&safesearch=1&locale=en_US&uiv=4");

        StringRequest request = new StringRequest(Request.Method.GET, "https://www.kumbhdesign.in/mobile-app/depost/api/profile-update/" + userId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
//                    pb_setimage.setVisibility(View.GONE);


                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("user_data");

                    int error = jsonObject2.getInt("error");
                    if (error == 0) {

                        JSONObject userData = jsonObject2.getJSONObject("user");
                        user = new UserData(userData.getString("facebook_url"), userData.getString("company_email"), userData.getString("company_logo_path"), userData.getString("website_url"), userData.getString("instagram_url"), userData.getString("linkedin_url"), userData.getString("mobile_number"), userData.getString("company_address"), userData.getString("twitter_url"));
                        Log.v("userData", user.getCompanyEmail());


                    }




                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pb_setimage.setVisibility(View.GONE);
                        Log.d("Error", error.toString());
                        Toast.makeText(HomrPage.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(HomrPage.this);
        requestQueue.add(request);
    }

    public void didTapButton(View view) {

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        festival_img.startAnimation(myAnim);
        loveu.startAnimation(myAnim);
        anniversary.startAnimation(myAnim);
        birthday_img.startAnimation(myAnim);
        gm.startAnimation(myAnim);
        gn.startAnimation(myAnim);
        congo.startAnimation(myAnim);
        my_events.startAnimation(myAnim);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.festival_ly:
                intent = new Intent(getApplicationContext(), Festival_list_page_normal.class);
                intent.putExtra("id", "1");
                startActivity(intent);
                finish();
                break;
            case R.id.birthday_ly:
                intent = new Intent(getApplicationContext(), Set_Images.class);
                intent.putExtra("festival", "birthday");
                startActivity(intent);
                finish();
                break;
            case R.id.loveu_ly:
                intent = new Intent(getApplicationContext(), Set_Images.class);
                intent.putExtra("festival", "loveu");
                startActivity(intent);
                finish();
                break;
            case R.id.anniversary_ly:
                intent = new Intent(getApplicationContext(), Set_Images.class);
                intent.putExtra("festival", "anniversary");
                startActivity(intent);
                finish();
                break;
            case R.id.gm_ly:
                intent = new Intent(getApplicationContext(), Set_Images.class);
                intent.putExtra("festival", "goodmorning");
                startActivity(intent);
                finish();
                break;

            case R.id.gn_ly:
                intent = new Intent(getApplicationContext(), Set_Images.class);
                intent.putExtra("festival", "goodnight");
                startActivity(intent);
                finish();
                break;

            case R.id.congo_ly:
                intent = new Intent(getApplicationContext(), Set_Images.class);
                intent.putExtra("festival", "congratulation");
                startActivity(intent);
                finish();
                break;

            case R.id.reminder_ly:
                intent = new Intent(HomrPage.this, My_Events.class);
                startActivity(intent);
                finish();
                break;

            case R.id.profile:
                showFilterPopup(v);
                break;
        }
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mgoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mgoogleApiClient.connect();
        super.onStart();
    }

    private void showFilterPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuLogout:
                        signOut(HomrPage.this);
                        break;

                    case R.id.profile:
                        intent = new Intent(getApplicationContext(), Profile.class);
                        startActivity(intent);
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

    public static void signOut(Context context) {
        Auth.GoogleSignInApi.signOut(mgoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                        shared_pr.isLogout();
                        shared_pr.setLogout(false);

                        Intent intent = new Intent(context,LoginPage.class);
                        context.startActivity(intent);
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result5 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CALENDAR);
        int result6 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_CALENDAR);
        int result7 = ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);
        int result8 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int result9 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED
                && result5 == PackageManager.PERMISSION_GRANTED && result6 == PackageManager.PERMISSION_GRANTED && result7 == PackageManager.PERMISSION_GRANTED && result8 == PackageManager.PERMISSION_GRANTED && result9 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE, READ_CONTACTS,INTERNET, CAMERA, READ_CALENDAR, WRITE_CALENDAR, GET_ACCOUNTS}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean camera1Accepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted && camera1Accepted) {
                    }
                    //  Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    else {

                        //  Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE, READ_CONTACTS,INTERNET, CAMERA, READ_CALENDAR, WRITE_CALENDAR, GET_ACCOUNTS},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(HomrPage.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }


}
