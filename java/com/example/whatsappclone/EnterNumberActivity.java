//package com.example.whatsappclone;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import com.example.whatsappclone.databinding.ActivityEnterNumberBinding;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.PhoneAuthOptions;
//import com.google.firebase.auth.PhoneAuthProvider;
//
//import java.util.concurrent.TimeUnit;
//
//public class EnterNumberActivity extends AppCompatActivity {
//
//    ActivityEnterNumberBinding binding;
//    FirebaseAuth auth;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityEnterNumberBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        getSupportActionBar().hide();
//        binding.phoneBox.requestFocus();
//
//        String phoneNum = binding.phoneBox.getText().toString().trim();
//
//        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (phoneNum.isEmpty()) {
//                    Toast.makeText(EnterNumberActivity.this, "Field Number can't be empty", Toast.LENGTH_SHORT).show();
//                } else if (phoneNum.length() != 10) {
//                    Toast.makeText(EnterNumberActivity.this, "Invalid Number", Toast.LENGTH_SHORT).show();
//                } else {
//                    Intent intent = new Intent(EnterNumberActivity.this, EnterOtpActivity.class);
//                    intent.putExtra("phoneNumber", phoneNum);
//                    startActivity(intent);
//                    sendVerificationCode(phoneNum);
//                }
//
//            }
//        });
//    }
//    private void sendVerificationCode(String number) {
//        // this method is used for getting
//        // OTP on user phone number.
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(auth)
//                        .setPhoneNumber(number)            // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // Activity (for callback binding)
//                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }
//}