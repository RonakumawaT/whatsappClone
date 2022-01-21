package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashScreen extends AppCompatActivity {

    FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        getSupportActionBar().hide();


//        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
//                .setMinimumFetchIntervalInSeconds(0)
//                .build();
//        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
//
//        mFirebaseRemoteConfig.fetchAndActivate().addOnSuccessListener(new OnSuccessListener<Boolean>() {
//            @Override
//            public void onSuccess(Boolean aBoolean) {
//
//                String toolbarColor=mFirebaseRemoteConfig.getString("toolbarColor");
//
//                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(toolbarColor)));
//
//               // Toast.makeText(SplashScreen.this, toolbarColor, Toast.LENGTH_SHORT).show();
//
//            }
//        });


        Handler hd= new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i= new Intent(SplashScreen.this,register.class);
                startActivity(i);
                finish();
            }
        },2000);

    }
}