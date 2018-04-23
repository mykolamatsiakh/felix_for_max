package com.flag.app.instagramSignIn;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * Created by fdh on 14.09.15.
 */
public class InstagramSession {
    private Context mContext;
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;

    private static final String SHARED = "Instagram_Preferences";
    private static final String API_USERNAME = "username";
    private static final String API_ID = "id";
    private static final String API_NAME = "name";
    private static final String API_ACCESS_TOKEN = "access_token";
    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static final String API_URL = "https://api.instagram.com/v1";
    private static final String TAG = "InstagramAPI";

    public InstagramSession(Context context) { ;
        mSharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        mEditor = mSharedPref.edit();
    }

    /**
     * Сохранение данных
     *
     */
    public void storeAccessToken(String accessToken, String id, String userName, String name) {
       mEditor.putString(API_ID, id);
       mEditor.putString(API_ACCESS_TOKEN, accessToken);
       mEditor.putString(API_USERNAME, userName);
       mEditor.putString(API_NAME, name);
       mEditor.commit();
    }

    public void storeAccessToken(String accessToken) {
        mEditor.putString(API_ACCESS_TOKEN, accessToken);
        mEditor.commit();
    }

    /**
     * Сброс данных
     */
    public void resetAccessToken() {
        mEditor.putString(API_ID, null);
        mEditor.putString(API_ACCESS_TOKEN, null);
        mEditor.putString(API_USERNAME, null);
        mEditor.putString(API_NAME, null);
        mEditor.commit();
    }

    /**
     * Получение данных
     *
     * @return InstagramUser data
     */
    public String getUserName() {
        return mSharedPref.getString(API_USERNAME, null);
    }

    public String getId() {
        return mSharedPref.getString(API_ID, null);
    }


    public String getName() {
        return mSharedPref.getString(API_NAME, null);
    }

    public String getAccessToken() {
        return mSharedPref.getString(API_ACCESS_TOKEN, null);
    }


    /**
     * Получение токена
     *
     * @return Access token
     */


    /**
     * Проверка токена
     *
     * @return true if active and vice versa
     */

}
