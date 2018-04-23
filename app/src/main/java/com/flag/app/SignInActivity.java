package com.flag.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.flag.FelixApplication;
import com.flag.app.instagramSignIn.ApplicationData;
import com.flag.app.instagramSignIn.AuthenticationDialog;
import com.flag.app.instagramSignIn.AuthenticationListener;
import com.flag.app.instagramSignIn.InstagramApp;
import com.flag.app.instagramSignIn.InstagramDialog;
import com.flag.app.instagramSignIn.InstagramHelper;
import com.flag.app.instagramSignIn.InstagramResponse;
import com.flag.app.instagramSignIn.InstagramSession;
import com.flag.app.instagramSignIn.InstagramUser;
import com.flag.app.pref.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


public class SignInActivity extends AppCompatActivity implements
        View.OnClickListener, InstagramResponse, AuthenticationListener{
//
//    private FacebookHelper mFbHelper;
//    private GoogleSignInHelper mGAuthHelper;
    private InstagramHelper mInstagramHelper;

    private static final String EMAIL = "email";

    InstagramApp mInstagramApp;
    Button mFacebookView;
    LoginButton mFacebookButton;
    Button mInstagramButton;
    private AuthenticationDialog auth_dialog;
    CallbackManager callbackManager;
    InstagramApp.OAuthAuthenticationListener mOAuthAuthenticationListener;

    private HashMap<String, String> userInfoHashMap = new HashMap<>();
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                userInfoHashMap = mInstagramApp.getUserInfo();
                Log.e("USER", mInstagramApp.getName());
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(SignInActivity.this, "Check your network.",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });


    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ((FelixApplication) this.getApplication()).setFirstStart(1);
        Log.d("KEY HASH --->", printKeyHash(this));
        mInstagramApp = new InstagramApp(this, ApplicationData.CLIENT_ID,
                ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        mInstagramApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

                                      @Override
                                      public void onSuccess() {
                                          // tvSummary.setText("Connected as " + mApp.getUserName());
                                          mInstagramApp.fetchUserName(handler);
                                          Log.e("INFO", mInstagramApp.getName());
                                          mOAuthAuthenticationListener.onSuccess();
                                          AuthenticationDialog authenticationDialog;
                                      }

                                      @Override
                                      public void onFail(String error) {
                                          Toast.makeText(SignInActivity.this, error, Toast.LENGTH_SHORT)
                                                  .show();
                                      }
                                  });

        mInstagramButton = findViewById(R.id.instagram_login_button);
        mFacebookView = findViewById(R.id.facebook_view);
        mFacebookView.setOnClickListener(this);
        mInstagramButton.setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();
        mFacebookButton = findViewById(R.id.fb_login_button1);
        mFacebookButton.setReadPermissions(EMAIL);
        mFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final String userid = loginResult.getAccessToken().getUserId();
                final String imgUrl = "https://graph.facebook.com/" + userid + "/picture?type=large";
                Toast.makeText(getApplicationContext(), "GOOD", Toast.LENGTH_SHORT).show();
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                String userName = "Unknown";
                                String email = "myemail";
                                String userSurname = "Unknown";
                                try {
                                    //  email = response.getJSONObject().getString("email");
                                    userName = response.getJSONObject().getString("first_name");
                                    userSurname = response.getJSONObject().getString("last_name");
                                    Log.e("user fields:" , response.getJSONObject().toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("ERROR", e.getMessage());
                                }

                                User user = new User(userid, userName, userSurname, email, User.Social.FACEBOOK, imgUrl);
                                UserPref.get(SignInActivity.this).putUser(user);

                                startActivity(MarkerActivity.getStartIntent(SignInActivity.this, user));
                                finish();
                                Toast.makeText(getApplicationContext(), "Користувача "+user.getName()+
                                        " "+user.getSurname()+ "  збережено",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name, email, id");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }


            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "BAD", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "BAD1", Toast.LENGTH_SHORT).show();
                Log.d("LoginError", error.toString());
            }
        });

//
//        //google auth initialization
//        mGAuthHelper = new GoogleSignInHelper(this, null, (GoogleAuthResponse) this);
//
//        //fb api initialization
//        mFbHelper = new FacebookHelper((FacebookResponse) this,
//                "id,name,email,gender,birthday,picture,cover",
//                this);
//
//
//        //instagram initializer
//        mInstagramHelper = new InstagramHelper(
//                getResources().getString(R.string.instagram_client_id),
//                getResources().getString(R.string.instagram_client_secret),
//                getResources().getString(R.string.instagram_callback_url), this, (InstagramResponse) this);

        //set sign in button
//        findViewById(R.id.g_login_btn).setOnClickListener(this);
//        findViewById(R.id.bt_act_login_fb).setOnClickListener(this);
//
//        findViewById(R.id.instagram_login_button).setOnClickListener(this);
    }
//
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.g_login_btn:
//                mGAuthHelper.performSignIn(this);
//                break;
               case R.id.facebook_view:
                    mFacebookButton.performClick();
                    break;
                case R.id.instagram_login_button:
                    connectOrDisconnectUser();
                    break;
            }
      }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    private void connectOrDisconnectUser() {
        if (mInstagramApp.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    SignInActivity.this);
            builder.setMessage("Disconnect from Instagram?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    mInstagramApp.resetAccessToken();
                                    // btnConnect.setVisibility(View.VISIBLE);
                                    // tvSummary.setText("Not connected");
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            mInstagramApp.authorize();
        }
    }

    @Override
    public void onInstagramSignInSuccess(InstagramUser user) {
        Intent intent = new Intent(SignInActivity.this, MarkerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onInstagramSignInFail(String error) {
        Toast.makeText(this, "ВСЬО ПЛОХО", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCodeReceived(String auth_token) {
        if (auth_token == null) {
            auth_dialog.dismiss();
        }

        Intent i = new Intent(SignInActivity.this, MarkerActivity.class);
        i.putExtra("access_token", auth_token);
        startActivity(i);
    }
//
//    @Override
//    public void onInstagramSignInFail(String error) {
//            Log.e("ERROR", error);
//    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //handle results
        callbackManager.onActivityResult(requestCode, resultCode, data);
        finish();


    }
//
//    @Override
//    public void onFbSignInFail() {
//        Toast.makeText(this, "Facebook sign in failed.", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onFbSignInSuccess() {
//        Toast.makeText(this, "Facebook sign in success", Toast.LENGTH_SHORT).show();
//
//        Макс Яблонс, [18.04.18 00:24]
//        startActivity(new Intent(SignInActivity.this, MarkerActivity.class));
//    }
//
//    @Override
//    public void onFbProfileReceived(FacebookUser facebookUser) {
//        Toast.makeText(this, "Facebook user data: name= " + facebookUser.name + " email= " + facebookUser.email, Toast.LENGTH_SHORT).show();
//
//        Log.d("Person name: ", facebookUser.name + "");
//        Log.d("Person gender: ", facebookUser.gender + "");
//        Log.d("Person email: ", facebookUser.email + "");
//        Log.d("Person image: ", facebookUser.facebookID + "");
//    }

//    @Override
//    public void onFBSignOut() {
//        Toast.makeText(this, "Facebook sign out success", Toast.LENGTH_SHORT).show();
//    }
//
//
//    @Override
//    public void onGoogleAuthSignIn(GoogleAuthUser user) {
//        startActivity(new Intent(SignInActivity.this, MarkerActivity.class));
//    }
//
//    @Override
//    public void onGoogleAuthSignInFailed() {
//        Toast.makeText(this, "Google sign in failed.", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onGoogleAuthSignOut(boolean isSuccess) {
//        Toast.makeText(this, isSuccess ? "Sign out success" : "Sign out failed", Toast.LENGTH_SHORT).show();
//    }
//

//
//    @Override
//    public void onInstagramSignInFail(String error) {
//        Toast.makeText(this, "InstagramApp sign in failed", Toast.LENGTH_SHORT).show();
   // }
}