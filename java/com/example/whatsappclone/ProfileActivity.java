package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    FirebaseStorage storage;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ProgressDialog dialog;
    Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating Profile");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        database.getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.nameBox.setText(snapshot.child("Name").getValue().toString());
                    Glide.with(getApplicationContext()).load(snapshot.child("ProfilePic").getValue().toString()).placeholder(R.drawable.placeholder).into(binding.imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, Dashboard.class));
                finishAffinity();
            }
        });

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 33);
            }
        });

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                String userName = binding.nameBox.getText().toString();
                if (userName.isEmpty()) {
                    binding.nameBox.setError("Please Type a Name");
                }

                if (filePath != null) {

                    storage = FirebaseStorage.getInstance();
                    Date date = new Date();
                    long time = date.getTime();

                   binding.imageView.setImageURI(filePath);

                    StorageReference reference = storage.getReference().child("Profiles").child(time + "");
                    dialog.show();
                    reference.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filesPath = uri.toString();
                                        HashMap map = new HashMap();
                                        map.put("ProfilePic", filesPath);
                                        map.put("Name", userName);
                                        database.getReference().child("Users")
                                                .child(FirebaseAuth.getInstance().getUid())
                                                .updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {

                                                Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(ProfileActivity.this, Dashboard.class));
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                } else {
                    HashMap map = new HashMap();
                    map.put("Name", userName);
                    database.getReference().child("Users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ProfileActivity.this, Dashboard.class));
                            finish();
                        }
                    });
                }
            }
        });

//        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            String userName = snapshot.child("Name").getValue(String.class);
//                            binding.nameBox.setText(userName);
////                            Glide.with(getApplicationContext()).load(model.getProfilePic())
////                                    .placeholder(R.drawable.user)
////                                    .into(binding.imageView);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 33 && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getData() != null) {
                    filePath = data.getData();
                    //binding.imageView.setImageURI(filePath);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();


//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String userId = user.getUid();
//
//        database.getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    binding.nameBox.setText(snapshot.child("Name").getValue().toString());
//                    Glide.with(getApplicationContext()).load(snapshot.child("ProfilePic").getValue().toString()).placeholder(R.drawable.placeholder).into(binding.imageView);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }
}