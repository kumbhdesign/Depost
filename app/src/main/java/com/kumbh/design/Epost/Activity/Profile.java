package com.kumbh.design.Epost.Activity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.util.SessionManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    CircleImageView profile;
    static SessionManager shared_pr;
    TextView text,txt_name,pr_mail_id;
    ImageView back;
    Typeface tf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile = (CircleImageView) findViewById(R.id.my_profilee);
        txt_name = (TextView) findViewById(R.id.txt_name);
        text = (TextView) findViewById(R.id.text);
        pr_mail_id = (TextView) findViewById(R.id.pr_mail_id);
        back = (ImageView) findViewById(R.id.back);

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
}
