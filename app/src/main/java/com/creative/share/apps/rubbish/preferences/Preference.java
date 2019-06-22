package com.creative.share.apps.rubbish.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.tags.Tags;
import com.google.gson.Gson;

public class Preference {

    private static Preference instance= null;
    private Preference() {
    }

    public static synchronized Preference newInstance()
    {
        if (instance==null)
        {
            instance = new Preference();
        }
        return instance;
    }

    public void create_update_user_data(Context context, UserModel userModel)
    {
        Gson gson = new Gson();
        String user_data = gson.toJson(userModel);

        SharedPreferences preferences = context.getSharedPreferences("user_pref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_data",user_data);
        editor.apply();
        create_update_session(context,Tags.SESSION_LOGIN);
    }

    public UserModel getUserData(Context context)
    {
        Gson gson = new Gson();

        SharedPreferences preferences = context.getSharedPreferences("user_pref",Context.MODE_PRIVATE);
        String user_data = preferences.getString("user_data","");
        UserModel userModel = gson.fromJson(user_data,UserModel.class);
        return userModel;

    }

    public void create_update_session(Context context,String session)
    {
        SharedPreferences preferences = context.getSharedPreferences("session_pref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("session",session);
        editor.apply();

    }

    public String getSession(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("session_pref",Context.MODE_PRIVATE);
        String session = preferences.getString("session", Tags.SESSION_LOGOUT);
        return session;
    }



}
