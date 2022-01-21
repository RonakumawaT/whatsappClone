package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.Adapters.groupMessagesAdapter;
import com.example.whatsappclone.Adapters.messagesAdapter;
import com.example.whatsappclone.databinding.ActivityChatBinding;
import com.example.whatsappclone.databinding.ActivityGrpChatBinding;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class GrpChatActivity extends AppCompatActivity {

    ActivityGrpChatBinding binding;
    groupMessagesAdapter adapter;
    ArrayList<msgModel> Messages;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String senderUID;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGrpChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        binding.name.setText("Group Chat");

        database = FirebaseDatabase.getInstance();
        Messages = new ArrayList<>();
        storage = FirebaseStorage.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading image...");


        senderUID = FirebaseAuth.getInstance().getUid();

        adapter = new groupMessagesAdapter(this, Messages);
        binding.grpChatView.setLayoutManager(new LinearLayoutManager(this));
        binding.grpChatView.setAdapter(adapter);

        binding.attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*"); //for all types
                startActivityForResult(intent, 23);

            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GrpChatActivity.this, Dashboard.class));
                finishAffinity();
            }
        });

        database.getReference().child("public")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            msgModel message = snapshot1.getValue(msgModel.class);
                            Messages.add(message);
                            if(Messages.isEmpty()){
                                Toast.makeText(GrpChatActivity.this, "Empty", Toast.LENGTH_LONG).show();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageTxt = binding.messageBox.getText().toString();
                binding.messageBox.setText("");
                Date date = new Date();

                HashMap map = new HashMap();
                map.put("message", messageTxt);
                map.put("uid", senderUID);
                map.put("timeStamp", date.getTime());

                database.getReference().child("public")
                        .push().setValue(map);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 23 && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getData() != null) {
                    Uri filePath = data.getData();
                    Calendar calendar = Calendar.getInstance();
                    StorageReference reference = storage.getReference().child("chats").child(calendar.getTimeInMillis() + "");
                    dialog.show();
                    reference.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String filePath = uri.toString();
                                        String messageTxt = binding.messageBox.getText().toString();
                                        Date date = new Date();

                                        msgModel message = new msgModel(senderUID, messageTxt);
                                        message.setTimeStamp(date.getTime());
                                        message.setMessage("photo");
                                        message.setImageUrl(filePath);
                                        binding.messageBox.setText("");

                                        database.getReference().child("public")
                                                .push().setValue(message);
                                    }
                                });
                            }
                        }
                    });
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}