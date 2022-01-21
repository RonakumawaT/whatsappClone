package com.example.whatsappclone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.ChatActivity;
import com.example.whatsappclone.R;
import com.example.whatsappclone.UserModel;
import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;

public class userAdapter extends RecyclerView.Adapter<userAdapter.viewHolder> {

    ArrayList<UserModel> list;
    Context context;

    public userAdapter(ArrayList<UserModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_show_user, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        UserModel userModel = list.get(position);
        String senderID = FirebaseAuth.getInstance().getUid();
        String senderRoom = senderID + userModel.getUID();

        holder.Name.setText(userModel.getName());

//        FirebaseDatabase.getInstance().getReference().child("Users")
//                .child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    Glide.with(context).load(snapshot.child("ProfilePic").getValue().toString()).placeholder(R.drawable.user).into(holder.profile);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        // Picasso.get().load(userModel.getProfilePic()).placeholder(R.drawable.user).into(holder.profile);

//
//       FirebaseDatabase.getInstance().getReference()
//               .child("Chats").child(senderRoom)
//               .addValueEventListener(new ValueEventListener() {
//                   @Override
//                   public void onDataChange(@NonNull DataSnapshot snapshot) {
//                       if(snapshot.exists()) {
//                           String lastMsg = snapshot.child("lastMessage").getValue(String.class);
////                           long time = snapshot.child("lastMsgTime").getValue(long.class);
//                           holder.lastMsg.setText(lastMsg);
//                       }else {
//                           holder.lastMsg.setText("Tap to Chat");
//                       }
//                   }
//                   @Override
//                   public void onCancelled(@NonNull DatabaseError error) {
//
//                   }
//               });
        FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            //long time = snapshot.child("lastMsgTime").getValue(Long.class);
                            //SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                            holder.lastMsg.setText(lastMsg);
                        } else {
                            holder.lastMsg.setText("Tap to chat");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name", userModel.getName());
                intent.putExtra("uid", userModel.getUID());
                intent.putExtra("token", userModel.getToken());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView profile;
        TextView lastMsg, Name;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.profile_image);
            lastMsg = itemView.findViewById(R.id.TVLastMsg);
            Name = itemView.findViewById(R.id.TVName);
        }
    }
}
