package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.whatsappclone.Adapters.contactsAdapter;
import com.example.whatsappclone.Adapters.userAdapter;
import com.example.whatsappclone.databinding.ActivityDashboardBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<UserModel> users;
    userAdapter adapter;
    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //remote config code.

        binding.BtnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getPermission();
                startActivity(new Intent(Dashboard.this,contactsActivity.class));
            }
        });

        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        mFirebaseRemoteConfig.fetchAndActivate().addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {

                String toolbarColor = mFirebaseRemoteConfig.getString("toolbarColor");
                String toolbarImage = mFirebaseRemoteConfig.getString("backgound");

                String background = mFirebaseRemoteConfig.getString("background");

                Glide.with(Dashboard.this).load(background).into(binding.backgound);
//
//                boolean isImageEnabled = mFirebaseRemoteConfig.getBoolean("toolbarImageEnabled");
//
////                if (isImageEnabled) {
//
//                    Glide.with(Dashboard.this).load(toolbarImage).into(new CustomTarget<Drawable>() {
//                        @Override
//                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                            getSupportActionBar().setBackgroundDrawable(resource);
//                        }
//
//                        @Override
//                        public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                        }
//                    });
////                } else {
////                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(toolbarColor)));
////                }
////                // Toast.makeText(SplashScreen.this, toolbarColor, Toast.LENGTH_SHORT).show();
            }
        });


//        binding.BtnContacts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getPermission();
//                startActivity(new Intent(Dashboard.this, contactsActivity.class));
//            }
//        });


        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String token) {

                HashMap map = new HashMap();
                map.put("token", token);

                database.getReference().child("Users")
                        .child(FirebaseAuth.getInstance().getUid())
                        .updateChildren(map);
            }
        });

        users = new ArrayList<>();
        adapter = new userAdapter(users, this);

        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView2.setAdapter(adapter);

        //this code is to don't show logined user in usersAdapter

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserModel user = snapshot1.getValue(UserModel.class);
                    if (!user.getUID().equals(FirebaseAuth.getInstance().getUid()))
                        users.add(user);
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void getPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 1);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(Dashboard.this, register.class));
                finish();
                break;
            case R.id.settings:
                break;
            case R.id.grpChat:
                startActivity(new Intent(Dashboard.this, GrpChatActivity.class));
                finish();
                break;
            case R.id.profilee:
                startActivity(new Intent(Dashboard.this, ProfileActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}