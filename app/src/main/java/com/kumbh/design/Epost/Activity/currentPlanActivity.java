package com.kumbh.design.Epost.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.util.SessionManager;

public class currentPlanActivity extends AppCompatActivity {
    Button share_button, bt_go_premium;

    SessionManager shared_pr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_plan);
        shared_pr = new SessionManager(getApplicationContext());
        bt_go_premium = findViewById(R.id.bt_go_premium);
     String name =  shared_pr.getName();
        share_button = findViewById(R.id.share_button);
        String userId = shared_pr.getUserId();
        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "DePost");
                    String shareMessage = "\nHi, "+name+" suggest you to use this App for your regular Social Media Post Design. Download from below link\n\n";
                    shareMessage = shareMessage + "https://is.gd/FUEfj6";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });
        bt_go_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path= shared_pr.getLogoPhoto();
                Intent intent;
                if(path!=null)
                {
                    intent = new Intent(getApplicationContext(), get_premium_activity.class);
                    intent.putExtra("id", "1");
                    startActivity(intent);
                }
                else{

                    Toast.makeText(currentPlanActivity.this,"Please complete profile detail",Toast.LENGTH_LONG).show();
                    intent = new Intent(getApplicationContext(), Profile.class);
                    intent.putExtra("id", "1");
                    startActivity(intent);
                }

            }
        });
    }
}