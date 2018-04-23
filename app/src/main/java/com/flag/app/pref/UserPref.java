package com.flag.app.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.flag.app.User;


/**
 * Created by Roman on 01-Apr-18.
 */

public class UserPref {

    private static final String USER_PREF = "USER_PREF";
    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_USER_NAME = "USER_NAME";
    private static final String KEY_USER_SOCIAL = "USER_SOCIAL";
    private static final String KEY_USER_IMG_URL = "USER_IMG_URL";
    private static final String KEY_USER_SURNAME = "USER_SURNAME";
    private static final String KEY_USER_EMAIL = "USER_EMAIL";

    private static UserPref sInstance;
    private final SharedPreferences mPrefs;

    public static UserPref get(Context context) {
        if (sInstance == null) {
            synchronized (UserPref.class) {
                sInstance = new UserPref(context);
            }
        }
        return sInstance;
    }

    private UserPref(Context context) {
        mPrefs = context.getApplicationContext().getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
    }

    public void putUser(User user) {
        mPrefs.edit()
                .putString(KEY_USER_ID, user.getId())
                .putString(KEY_USER_NAME, user.getName())
                .putString(KEY_USER_SURNAME, user.getSurname())
                .putString(KEY_USER_EMAIL, user.getEmail())
                .putString(KEY_USER_SOCIAL, user.getSocial())
                .putString(KEY_USER_IMG_URL, user.getImgUrl())
                .apply();
    }

    @Nullable
    public User getUser() {
       String userId = mPrefs.getString(KEY_USER_ID, "");
       String userSurname =  mPrefs.getString(KEY_USER_SURNAME, "");
       String userEmail = mPrefs.getString(KEY_USER_EMAIL, "");
       if (userId.isEmpty()) return null;
       String userName = mPrefs.getString(KEY_USER_NAME, "");
       @User.Social String userSocial = mPrefs.getString(KEY_USER_SOCIAL, "");
       String userImg = mPrefs.getString(KEY_USER_IMG_URL, "");
       return new User(userId, userName, userSurname, userEmail, userSocial, userImg);
    }

    public void clear() {
        mPrefs.edit().clear().apply();
    }
}
