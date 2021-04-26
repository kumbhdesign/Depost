package com.kumbh.design.Epost.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.util.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    CircleImageView profile;
    static SessionManager shared_pr;
    TextView text,txt_name,pr_mail_id,company_email,com_mob,com_address,com_website,con_facebook,com_linkedin,com_insta;
    ImageView back;
    LinearLayout li_website,li_facebook,li_twitter,li_insta;
    Typeface tf;
    private UserData user;
    ImageView img1,img2,img3,img4,img5,img6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        profile = (CircleImageView) findViewById(R.id.my_profilee);
        txt_name = (TextView) findViewById(R.id.txt_name);
        text = (TextView) findViewById(R.id.text);
        pr_mail_id = (TextView) findViewById(R.id.pr_mail_id);
        back = (ImageView) findViewById(R.id.back);
        company_email=findViewById(R.id.profile_com_value);
        com_address=findViewById(R.id.profile_com_adress_value);
        com_mob=findViewById(R.id.profile_com_mobile_value);
        com_website=findViewById(R.id.profile_com_website_value);
        con_facebook=findViewById(R.id.profile_com_facebook_value);
        com_linkedin=findViewById(R.id.profile_com_twitter_value);
        com_insta=findViewById(R.id.profile_com_insta_value);
        img1=findViewById(R.id.img_edit);
        img2=findViewById(R.id.img_edit_address);
        img3=findViewById(R.id.img_edit_facebook);
        img4=findViewById(R.id.img_edit_mobile);
        img5=findViewById(R.id.img_edit_insta);
        img6=findViewById(R.id.img_edit_website);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
                startActivity(intent);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
                startActivity(intent);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
                startActivity(intent);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
                startActivity(intent);
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
                startActivity(intent);
            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
                startActivity(intent);
            }
        });

        li_website=findViewById(R.id.rel_comapny_website);
        li_facebook=findViewById(R.id.rel_comapny_facebook);
        li_twitter=findViewById(R.id.rel_comapny_twitter);
        li_insta=findViewById(R.id.rel_comapny_insta);

        tf = Typeface.createFromAsset(getAssets(), "fonts/swift_lt.ttf");
        text.setTypeface(tf,Typeface.BOLD);

        shared_pr = new SessionManager(getApplicationContext());

        if (!shared_pr.getPhoto().toString().equalsIgnoreCase("null"))
            Picasso.get().load(shared_pr.getPhoto()).into(profile);
        else
            Picasso.get().load(R.drawable.avatar).into(profile);

        Log.d("Email",shared_pr.getEmail());
        Log.d("Name",shared_pr.getName());

        txt_name.setText(shared_pr.getName());
        pr_mail_id.setText(shared_pr.getEmail());
        getData();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void getData() {
        String userId = shared_pr.getUserId();

        //  Log.d("DATA_URL","https://api.qwant.com/api/search/images?count=50&q="+festival+"+backgrounds&t=images&safesearch=1&locale=en_US&uiv=4");
//        pb_setimage.setVisibility(View.VISIBLE);
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
                        user = new UserData(userData.getString("facebook_url"), userData.getString("company_email"), userData.getString("company_logo_path"), userData.getString("website_url"), userData.getString("instagram_url"), userData.getString("linkedin_url"), userData.getString("mobile_number"), userData.getString("company_address"));
                        Log.v("userData", user.getCompanyEmail());


                    }
                    company_email.setText(user.getCompanyEmail());
                    com_address.setText(user.getAddress());
                    com_mob.setText(user.getMobileNumber());
                    if(user.getWebsiteUrl()!=null && user.getWebsiteUrl().trim().length()>0)
                    {
                        li_website.setVisibility(View.VISIBLE);
                        com_website.setText(user.getWebsiteUrl());
                    }
                    if(user.getFacebookUrl()!=null  && user.getWebsiteUrl().trim().length()>0)
                    {
                        li_facebook.setVisibility(View.VISIBLE);
                        con_facebook.setText(user.getFacebookUrl());
                    }
                    if(user.getInstagramUrl()   !=null  && user.getInstagramUrl().trim().length()>0)
                    {
                        li_twitter.setVisibility(View.VISIBLE);
                        com_linkedin.setText(user.getInstagramUrl());
                    }
                    if(user.getLinkedinUrl()   !=null  && user.getLinkedinUrl().trim().length()>0)
                    {
                        li_insta.setVisibility(View.VISIBLE);
                        com_insta.setText(user.getLinkedinUrl());
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
                        Toast.makeText(Profile.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
        requestQueue.add(request);
    }
}
