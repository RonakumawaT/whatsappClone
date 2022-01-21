package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.example.whatsappclone.Adapters.contactsAdapter;
import com.example.whatsappclone.databinding.ActivityContactsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class contactsActivity extends AppCompatActivity {

    ActivityContactsBinding binding;
    ArrayList<contactsModel> contactList, userList;
    contactsAdapter adapter;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        binding.RVContacts.setLayoutManager(new LinearLayoutManager(this));
        contactList = new ArrayList<>();
        userList = new ArrayList<>();
        adapter = new contactsAdapter(this, userList);
        binding.RVContacts.setAdapter(adapter);

        getPermission();

        getContacts();

    }

    private void getContacts() {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            contactsModel model = new contactsModel(name, phone);
            contactList.add(model);
            adapter.notifyDataSetChanged();
            getUsersDetails(model);
        }
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 1);
        }
    }

    public void getUsersDetails(contactsModel model) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
        Query query = reference.orderByChild("PhoneNumber").equalTo(model.getNumber());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String phone = "";
                    String name = "";
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        if (childSnapshot.child("PhoneNumber").getValue() != null) {
                            phone = childSnapshot.child("PhoneNumber").getValue().toString();
                            if (childSnapshot.child("Name").getValue() != null)
                                name = childSnapshot.child("Name").getValue().toString();

                            contactsModel model1 = new contactsModel(name, phone);
                            userList.add(model1);
                            adapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}