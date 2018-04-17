package com.flag.app.instagramSignIn;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * Created by fdh on 14.09.15.
 */
public class InstagramSession {
    private Context mContext;
    private SharedPreferences mSharedPref;

    private static final String SHARED = "Instagram_Preferences";
    private static final String ACCESS_TOKEN = "access_token";

    public InstagramSession(Context context) {
        mContext	= context;
        mSharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    }

    /**
     * Сохранение данных
     *
     * @param user InstagramUser data
     */
    public void store(InstagramUser user) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(ACCESS_TOKEN,  user.getAccessToken());
        editor.commit();
    }

    /**
     * Сброс данных
     */
    public void reset() {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(ACCESS_TOKEN, 	"");
        editor.commit();

        CookieSyncManager.createInstance(mContext);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    /**
     * Получение данных
     *
     * @return InstagramUser data
     */
    public InstagramUser getUser() {
        if (mSharedPref.getString(ACCESS_TOKEN, "").equals("")) {
            return null;
        }
        InstagramUser user 	= new InstagramUser();
        user.setAccessToken(mSharedPref.getString(ACCESS_TOKEN, ""));
        return user;
    }

    /**
     * Получение токена
     *
     * @return Access token
     */
    public String getAccessToken() {
        return mSharedPref.getString(ACCESS_TOKEN, "");
    }

    /**
     * Проверка токена
     *
     * @return true if active and vice versa
     */
    public boolean isActive() {
        return (mSharedPref.getString(ACCESS_TOKEN, "").equals("")) ? false : true;
    }
}
