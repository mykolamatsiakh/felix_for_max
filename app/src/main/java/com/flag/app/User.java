package com.flag.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by Roman on 01-Apr-18.
 */

public class User implements Parcelable {

    @Retention(SOURCE)
    @StringDef({Social.FACEBOOK, Social.GOOGLE, Social.INSTAGRAM})
    public @interface Social {
        String FACEBOOK = "Facebook";
        String GOOGLE = "Google";
        String INSTAGRAM = "InstagramApp";
    }

    private final String mId;
    private final String mName;
    private final String mSurname;
    private final String mEmail;
    @Social private final String mSocial;
    private String mImgUrl;

    public User(String id, String name, String surname, String email, @Social String social, String imgUrl) {
        mId = id;
        mName = name;
        mSocial = social;
        mImgUrl = imgUrl;
        mSurname = surname;
        mEmail = email;
    }

    protected User(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mSocial = in.readString();
        mImgUrl = in.readString();
        mSurname = in.readString();
        mEmail =in.readString();
    }

    public String getSurname() {
        return mSurname;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getImgUrl() {return mImgUrl;}

    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }

    @Social
    public String getSocial() {
        return mSocial;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mName);
        parcel.writeString(mSocial);
        parcel.writeString(mImgUrl);
        parcel.writeString(mSurname);
        parcel.writeString(mEmail);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
