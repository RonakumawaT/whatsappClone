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

import com.example.whatsappclone.contactsModel;

import java.util.ArrayList;

public class contactsAdapter extends RecyclerView.Adapter<contactsAdapter.viewHolder> {

    Context context;
    ArrayList<contactsModel> contactList;

    public contactsAdapter(Context context, ArrayList<contactsModel> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_contacts,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        contactsModel model=contactList.get(position);
        holder.contactName.setText(model.getName());
        holder.status.setText(model.getNumber());

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView profile;
        TextView contactName, status;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.IVContact);
            contactName = itemView.findViewById(R.id.TVContactName);
            status = itemView.findViewById(R.id.TVStatus);
        }
    }
}
