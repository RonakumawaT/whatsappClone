package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class register extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    TextView Already,numberLogin;
    FirebaseAuth auth;
    Button Google,Register;
    ProgressDialog dialog;
    Button Facebook;
    FirebaseDatabase database;
    EditText userName,Email,Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName=findViewById(R.id.ETFullName);
        Email=findViewById(R.id.ETEmail);
        Password=findViewById(R.id.ETPassword);
        Already=findViewById(R.id.TVAlready);
            dialog=new ProgressDialog(this);
            dialog.setTitle("Register");
            dialog.setMessage("We are creating your Account");

        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        getSupportActionBar().hide();

        // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Google = findViewById(R.id.BtnGoogle);
        Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                signIn();
            }
        });

        Facebook=findViewById(R.id.BtnFacebook);
        Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        numberLogin=findViewById(R.id.numberLogin);
        numberLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register.this, otpActivity.class));
            }
        });

        Register=findViewById(R.id.BtnRegister);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                auth.createUserWithEmailAndPassword(Email.getText().toString().trim(), Password.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    String id=task.getResult().getUser().getUid();

                                    HashMap map=new HashMap();
                                    map.put("Name",userName.getText().toString());
                                    map.put("Email",Email.getText().toString());
                                    map.put("Password",Password.getText().toString());
                                    map.put("UID",id);
                                    database.getReference().child("Users").child(id).setValue(map);

                                    Toast.makeText(register.this, "User created Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(register.this,ActivityProfile.class));
                                    finishAffinity();

                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(register.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        Already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register.this,Login.class));
                finish();
            }
        });

    }

    int RC_SIGN_IN = 63;

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            FirebaseUser user = auth.getCurrentUser();
                            startActivity(new Intent(register.this, Dashboard.class));
                            Toast.makeText(register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                            String id=task.getResult().getUser().getUid();

                            HashMap map=new HashMap();
                            map.put("UID",id);
                            map.put("Name",user.getDisplayName());
                            map.put("ProfilePic",user.getPhotoUrl().toString());
                            map.put("Email",user.getEmail());

                            database.getReference().child("Users").child(id).setValue(map);
                        } else {
                            dialog.dismiss();
                            Toast.makeText(register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user=auth.getCurrentUser();
        if(user!=null){
            startActivity(new Intent(register.this,Dashboard.class));
            finish();
        }
    }
}