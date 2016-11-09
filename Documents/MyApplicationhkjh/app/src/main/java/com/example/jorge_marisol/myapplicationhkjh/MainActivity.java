package com.example.jorge_marisol.myapplicationhkjh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private AdView adView;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView nombreProfileFB,emailProfileFB;
    private ProfilePictureView profileImage;// para ver la imagen del perfil

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adView = (AdView) findViewById(R.id.ad_view);
        nombreProfileFB = (TextView)findViewById(R.id.nombrePerfil);
        emailProfileFB  = (TextView)findViewById(R.id.emailPerfil);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.setReadPermissions(Arrays.asList("email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //ingreso exitoso
                Profile profile = Profile.getCurrentProfile();
                nombreProfileFB.setText( profile.getName());
                profileImage = (ProfilePictureView) findViewById(R.id.profilePicture);
                profileImage.setProfileId(profile.getId());
                Toast.makeText(getApplicationContext(), R.string.success_login, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancel() {
                //proceso cancelado
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException error) {
                //un error
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }

            AccessTokenTracker accessTokenTracker= new AccessTokenTracker()
            {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                    if (newToken == null){
                        nombreProfileFB.setText("");
                        profileImage.setProfileId(null);

                    }
                }
            };
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onPause() {
        if(adView != null){
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(adView != null){
            adView.resume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

