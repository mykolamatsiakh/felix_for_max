package com.flag.app.instagramSignIn;

/**
 * Created by Mykola Matsiakh on 19.04.18.
 * Copyright (c) 2017, Reynolds. All rights reserved.
 */

    public interface AuthenticationListener {

        void onCodeReceived(String auth_token);

    }
