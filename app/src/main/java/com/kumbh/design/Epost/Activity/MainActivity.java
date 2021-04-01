package com.kumbh.design.Epost.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.util.SessionManager;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    private SessionManager session;
    public static int SPLASH_TIME_OUT = 3000;
    ImageView splash;
    View v;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        session = new SessionManager(MainActivity.this);
        splash = (ImageView) findViewById(R.id.splash_img);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        splash.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("session.isLoggedIn()", String.valueOf(session.isLoggedIn()));
                if (session.isLoggedIn() == true) {
                    Intent intent = new Intent(MainActivity.this, HomrPage.class);
                    intent.putExtra("id","1");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginPage.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                            splash, ViewCompat.getTransitionName(splash));
                    startActivity(intent, options.toBundle());
                }
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
