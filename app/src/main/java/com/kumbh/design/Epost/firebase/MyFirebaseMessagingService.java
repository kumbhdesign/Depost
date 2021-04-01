package com.kumbh.design.Epost.firebase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kumbh.design.Epost.Activity.Festival_list_page_premium;
import com.kumbh.design.Epost.Activity.LoginPage;
import com.kumbh.design.Epost.Activity.Set_Images;
import com.kumbh.design.Epost.util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String TAG = "Messaging_service";
    SessionManager manager;
    Intent intent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message_data_payload: " + remoteMessage.getData());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @SuppressLint("RestrictedApi")
    private void sendPushNotification(JSONObject json) {
        manager = new SessionManager(getApplicationContext());
        try {
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");
            String category = data.getString("category");

            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
            if (manager.isLoggedIn() == true) {

                if (category.equalsIgnoreCase("festival")){
                    intent = new Intent(getApplicationContext(), Festival_list_page_premium.class);
                    intent.putExtra("id","1");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mNotificationManager.showSmallNotification(title, message, intent);

                }else if (category.equalsIgnoreCase("birthday")){
                    intent = new Intent(getApplicationContext(), Set_Images.class);
                    intent.putExtra("festival","birthday");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mNotificationManager.showSmallNotification(title, message, intent);

                }else if (category.equalsIgnoreCase("Love")){
                    intent = new Intent(getApplicationContext(), Set_Images.class);
                    intent.putExtra("festival","loveu");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mNotificationManager.showSmallNotification(title, message, intent);

                }else if (category.equalsIgnoreCase("Anniversary")){
                    intent = new Intent(getApplicationContext(), Set_Images.class);
                    intent.putExtra("festival","anniversary");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mNotificationManager.showSmallNotification(title, message, intent);

                }else if (category.equalsIgnoreCase("Good Morning")){
                    intent = new Intent(getApplicationContext(), Set_Images.class);
                    intent.putExtra("festival","goodmorning");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mNotificationManager.showSmallNotification(title, message, intent);

                }else if (category.equalsIgnoreCase("Good Night")){
                    intent = new Intent(getApplicationContext(), Set_Images.class);
                    intent.putExtra("festival","goodnight");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mNotificationManager.showSmallNotification(title, message, intent);

                }else if (category.equalsIgnoreCase("Congratulations")){
                    intent = new Intent(getApplicationContext(), Set_Images.class);
                    intent.putExtra("festival","congratulation");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mNotificationManager.showSmallNotification(title, message, intent);
                }
            } else {
                intent = new Intent(getApplicationContext(), LoginPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mNotificationManager.showSmallNotification(title, message, intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
