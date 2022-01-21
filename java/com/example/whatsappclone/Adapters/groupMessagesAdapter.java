package com.example.whatsappclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;
import com.example.whatsappclone.UserModel;
import com.example.whatsappclone.msgModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class groupMessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<msgModel> Messages;
    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    public groupMessagesAdapter(Context context, ArrayList<msgModel> Messages) {
        this.context = context;
        this.Messages = Messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.group_sender_item, parent, false);
            return new senderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.group_receive_item, parent, false);
            return new receiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (Messages.get(position).getUID().equals(FirebaseAuth.getInstance().getUid())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        msgModel model = Messages.get(position);
        if (holder.getClass() == senderViewHolder.class) {
            if (model.getMessage().equals("photo")) {
                ((senderViewHolder) holder).imageView.setVisibility(View.VISIBLE);
                ((senderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                Picasso.get().load(model.getImageUrl()).placeholder(R.drawable.placeholder).into(((senderViewHolder) holder).imageView);
            } else {
                ((senderViewHolder) holder).imageView.setVisibility(View.GONE);
                ((senderViewHolder) holder).senderMsg.setVisibility(View.VISIBLE);
            }
            ((senderViewHolder) holder).senderMsg.setText(model.getMessage());

        } else {
            if (model.getMessage().equals("photo")) {
                ((receiverViewHolder) holder).receiverMsg.setVisibility(View.GONE);
                ((receiverViewHolder) holder).rec.setVisibility(View.VISIBLE);
                Picasso.get().load(model.getImageUrl()).placeholder(R.drawable.placeholder).into(((receiverViewHolder) holder).rec);
            }

            FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(model.getUID())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                UserModel model = snapshot.getValue(UserModel.class);
                                ((receiverViewHolder) holder).receiverName.setText("@"+model.getName());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            ((receiverViewHolder) holder).receiverMsg.setText(model.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        return Messages.size();
    }

    public class senderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg;
        ImageView imageView;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.messageGrp);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    public class receiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMsg, receiverName;
        ImageView rec;

        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg = itemView.findViewById(R.id.messageRec);
            rec = itemView.findViewById(R.id.imageRec);
            receiverName = itemView.findViewById(R.id.nameRec);

        }
    }
}
