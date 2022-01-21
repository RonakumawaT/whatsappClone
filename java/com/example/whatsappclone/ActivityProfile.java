package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;

public class ActivityProfile extends AppCompatActivity {

    ImageView profile;
    Button setUp;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri filePath;
    ProgressDialog dialog;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        getSupportActionBar().hide();

        profile = findViewById(R.id.imageView);
        setUp = findViewById(R.id.BtnStet);
        name=findViewById(R.id.nameBox);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Profile setup..");

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 33);
            }
        });

        setUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                String userName =name.getText().toString();
                if (userName.isEmpty()) {
                    name.setError("Please Type a Name");
                }

                if (filePath != null) {


                    storage = FirebaseStorage.getInstance();
                    Date date = new Date();
                    long time = date.getTime();

//                   binding.imageView.setImageURI(filePath);

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
                                                Toast.makeText(ActivityProfile.this, "Profile setup Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(ActivityProfile.this, Dashboard.class));
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 33 && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getData() != null) {
                    filePath = data.getData();
                    profile.setImageURI(filePath);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}