package com.example.ybl.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ybl.model.User;
import com.google.gson.Gson;

public class SessionManager {
    private static final String PREF_NAME = "YBL_SESSION";
    private static final String KEY_USER = "user";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public static final String ACTION_LOGOUT = "com.example.ybl.ACTION_LOGOUT";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Gson gson;

    private static volatile SessionManager instance;

    private SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        gson = new Gson();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    public void createLoginSession(User user, String token) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER, userJson);
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public User getUserDetails() {
        String userJson = pref.getString(KEY_USER, null);
        if (userJson != null) {
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }

    public String getToken() {
        return pref.getString(KEY_TOKEN, null);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void updateUser(User user) {
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER, userJson);
        editor.apply();
    }

    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
}
