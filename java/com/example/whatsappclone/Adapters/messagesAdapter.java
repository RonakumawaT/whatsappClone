package com.example.whatsappclone.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.DeleteSampleBinding;
import com.example.whatsappclone.databinding.RecievermsgSampleBinding;
import com.example.whatsappclone.databinding.SendermsgSampleBinding;
import com.example.whatsappclone.msgModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class messagesAdapter extends RecyclerView.Adapter {

    private ActionMode mActionMode;

    Context context;
    ArrayList<msgModel> messages;
    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;
    String recId;
    String senderRoom;
    String receiverRoom;

    public messagesAdapter(Context context, ArrayList<msgModel> messages, String recId, String senderRoom, String receiverRoom) {
        this.context = context;
        this.messages = messages;
        this.recId = recId;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.sendermsg_sample, parent, false);
            return new senderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.recievermsg_sample, parent, false);
            return new receiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getUID().equals(FirebaseAuth.getInstance().getUid())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        msgModel model = messages.get(position);

        if (holder.getClass() == senderViewHolder.class) {

            senderViewHolder viewHolder = (senderViewHolder) holder;

            if (model.getMessage().equals("photo")) {
                ((senderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                ((senderViewHolder) holder).imageView.setVisibility(View.VISIBLE);
                Picasso.get().load(model.getImageUrl()).placeholder(R.drawable.placeholder).into(((senderViewHolder) holder).imageView);
            } else {
                ((senderViewHolder) holder).senderMsg.setVisibility(View.VISIBLE);
                ((senderViewHolder) holder).senderMsg.setText(model.getMessage());
                ((senderViewHolder) holder).imageView.setVisibility(View.GONE);
            }

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    View view = LayoutInflater.from(context).inflate(R.layout.delete_sample, null);
                    DeleteSampleBinding binding = DeleteSampleBinding.bind(view);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Message")
                            .setView(binding.getRoot())
                            .create();

                    binding.everyone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            model.setMessage("This message is removed.");
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(model.getMessageId()).setValue(model);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .child(model.getMessageId()).setValue(model);
                            dialog.dismiss();
                        }
                    });

                    binding.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(model.getMessageId()).setValue(null);
                            dialog.dismiss();
                        }
                    });

                    binding.cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                    return false;
                }
            });


        } else {

            receiverViewHolder viewHolder = (receiverViewHolder) holder;

            if (model.getMessage().equals("photo")) {
                ((receiverViewHolder) holder).receiverMsg.setVisibility(View.GONE);
                ((receiverViewHolder) holder).rec.setVisibility(View.VISIBLE);
                Picasso.get().load(model.getImageUrl()).placeholder(R.drawable.placeholder).into(((receiverViewHolder) holder).rec);
            } else {
                ((receiverViewHolder) holder).receiverMsg.setVisibility(View.VISIBLE);
                ((receiverViewHolder) holder).receiverMsg.setText(model.getMessage());
                ((receiverViewHolder) holder).rec.setVisibility(View.GONE);
            }

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    View view = LayoutInflater.from(context).inflate(R.layout.delete_sample, null);
                    DeleteSampleBinding binding = DeleteSampleBinding.bind(view);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Message")
                            .setView(binding.getRoot())
                            .create();

                    binding.everyone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            model.setMessage("This message is removed.");
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(model.getMessageId()).setValue(model);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .child(model.getMessageId()).setValue(model);
                            dialog.dismiss();
                        }
                    });

                    binding.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(model.getMessageId()).setValue(null);
                            dialog.dismiss();
                        }
                    });

                    binding.cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class senderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg;
        ImageView imageView;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.message);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public class receiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMsg;
        ImageView rec;

        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg = itemView.findViewById(R.id.recieverMsg);
            rec = itemView.findViewById(R.id.imageView2);
        }
    }
}
