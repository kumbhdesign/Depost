package com.kumbh.design.Epost.firebase;

import android.app.ProgressDialog;
import android.util.Log;

import com.kumbh.design.Epost.util.SessionManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    ProgressDialog pb_service;
    private static final String TAG = "MyFirebaseIIDService";
    SessionManager manager;

    @Override
        public void onTokenRefresh() {
        manager = new SessionManager(getApplicationContext());
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed_token: " + refreshedToken);
        storeToken(refreshedToken);
    }

    private void storeToken(String refreshedToken) {
        manager.saveDeviceToken(refreshedToken);
    }
}
