//package com.example.whatsappclone;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//
//import com.example.whatsappclone.databinding.ActivityEnterOtpBinding;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.FirebaseException;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.PhoneAuthCredential;
//import com.google.firebase.auth.PhoneAuthOptions;
//import com.google.firebase.auth.PhoneAuthProvider;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.concurrent.TimeUnit;
//
//public class EnterOtpActivity extends AppCompatActivity {
//
//    ActivityEnterOtpBinding binding;
//    FirebaseAuth auth;
//    FirebaseDatabase database;
//    ProgressDialog dialog;
//    String mVerificationId;
//    String number;
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityEnterOtpBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        getSupportActionBar().hide();
//        auth = FirebaseAuth.getInstance();
//        database = FirebaseDatabase.getInstance();
//
//        number = getIntent().getStringExtra("phoneNumber").toString();
//
//        binding.phoneLbl.setText("Verify" + number);
//
//        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential credential) {
//
//                signInWithPhoneAuthCredential(credential);
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//
//            }
//
//            @Override
//            public void onCodeSent(@NonNull String verificationId,
//                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
//
//                mVerificationId = verificationId;
//            }
//        };
//
//    }
//
//    private void startPhoneNumberVerification(String phoneNumber) {
//        // [START start_phone_auth]
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(auth)
//                        .setPhoneNumber(phoneNumber)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // Activity (for callback binding)
//                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//        // [END start_phone_auth]
//    }
//
//    private void verifyPhoneNumberWithCode(String verificationId, String code) {
//        // [START verify_with_code]
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
//        // [END verify_with_code]
//    }
//
//    private void resendVerificationCode(String phoneNumber,
//                                        PhoneAuthProvider.ForceResendingToken token) {
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(auth)
//                        .setPhoneNumber(phoneNumber)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // Activity (for callback binding)
//                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }
//
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        auth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser user = task.getResult().getUser();
//
//
//                        } else {
//
//                        }
//                    }
//                });
//    }
//}
