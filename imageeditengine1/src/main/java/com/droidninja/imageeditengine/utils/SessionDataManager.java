package com.droidninja.imageeditengine.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionDataManager {

    private static String TAG = SessionDataManager.class.getSimpleName();

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String PRF_NAME = "depost";
    public static final String KEY_ID = "unique_id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAME = "name";
    public static final String KEY_ISLOGOUT = "islogout";
    public static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";
    private static final String PERSON_PHOTO = "person_photo";
    private static final String PERSON_Country = "person_country";
    private static final String USER_ID = "user_id";

    String login_type;

    public SessionDataManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PRF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public void setEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public String getCountry() {
        return pref.getString(PERSON_Country, null);
    }

    public void setCountry(String email) {
        editor.putString(PERSON_Country, email);
        editor.commit();
    }

    public String getPhoto() {
        return pref.getString(PERSON_PHOTO, null);
    }

    public void setPhoto(String email) {
        editor.putString(PERSON_PHOTO, email);
        editor.commit();
    }

    public String getAccountname() {
        return pref.getString(PREF_ACCOUNT_NAME, null);
    }

    public void setAccountname(String email) {
        editor.putString(PREF_ACCOUNT_NAME, email);
        editor.commit();
    }

    public String getId() {
        return pref.getString(KEY_ID, null);
    }

    public void setId(String id) {
        editor.putString(KEY_ID, id);
        editor.commit();
    }

    public String getName() {
        return pref.getString(KEY_NAME, null);
    }

    public void setName(String message) {
        editor.putString(KEY_NAME, message);
        editor.commit();
    }

      public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token) {
        pref = _context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken() {
        pref = _context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  pref.getString(TAG_TOKEN, "null");
    }

    public boolean isLogout() {

        return pref.getBoolean(KEY_ISLOGOUT, true);
    }

    public void setLogout(boolean logout) {
        editor.putBoolean(KEY_IS_LOGGEDIN, logout);
        editor.commit();
        editor.clear();
        editor.commit();
    }

    public void setUserId(String user_id) {
        editor.putString(USER_ID, user_id);
        Log.v("userID---->",user_id);
        editor.commit();
    }

    public String getUserId() {
        return pref.getString(USER_ID, null);
    }
}
