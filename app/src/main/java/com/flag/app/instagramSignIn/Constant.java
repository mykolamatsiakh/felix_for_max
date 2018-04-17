package com.flag.app.instagramSignIn;

/**
 * Created by fdh on 09.09.15.
 */
public class Constant {

    public static final String CLIEND_ID = "a6726ae82dae477089f582816caeb63a";
    public static final String CLIENT_SECRET = "d05227aea8d94e018ec8f090d79231f7";

    public static final String AUTH_MARKER = "https://instagram.com/oauth/authorize/";

    public static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    public static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    public static final String CALLBACK_URL = "https://mail.ru";

    public static final String API_URL = "https://api.instagram.com/v1/";


    //https://instagram.com/oauth/authorize/?client_id=a6726ae82dae477089f582816caeb63a&redirect_uri=https://mail.ru&response_type=token

    public static final String GET_TOKEN_URL = "https://instagram.com/oauth/authorize/" +
            "?client_id=" + CLIEND_ID +
            "&redirect_uri=" + CALLBACK_URL +
            "&response_type=token";

}
