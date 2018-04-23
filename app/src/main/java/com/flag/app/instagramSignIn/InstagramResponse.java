package com.flag.app.instagramSignIn;

import android.provider.ContactsContract;

/**
 * Created by Mykola Matsiakh on 18.04.18.
 * Copyright (c) 2017, Reynolds. All rights reserved.
 */
public interface InstagramResponse {


//    private ContactsContract.Data[] data;

//    public ContactsContract.Data[] getData() {
//        return data;
//    }

//    public void setData(ContactsContract.Data[] data) {
//        this.data = data;
//    }

    /**
     * If instagram successfully login
     *
     * @param user user data from instagram
     */
    void onInstagramSignInSuccess(InstagramUser user);

    /**
     * If error occurs while login to instagram
     *
     * @param error error message from {@link InstagramHelper}
     */
    void onInstagramSignInFail(String error);
    }