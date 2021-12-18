package com.mycontacts.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mycontacts.R;
import com.mycontacts.model.Contact;

import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    private ArrayList<Contact> contactList;
    private IContactListAdapter iContactListAdapter;

    /**
     * Setting the interface
     * @param iContactListAdapter - interface
     */
    public ContactListAdapter(IContactListAdapter iContactListAdapter) {
        this.iContactListAdapter = iContactListAdapter;
    }

    /**
     * This method set the list of updated contacts in the adapter and notify the changes
     * @param contactList - list of contacts to set in adapter
     */
    public void setList(ArrayList<Contact> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.liststyle, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Contact contact = contactList.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvNumber.setText(contact.getNumber());

        holder.itemView.setOnClickListener(view -> iContactListAdapter.itemClick(contact));
        holder.imgMsg.setOnClickListener(view -> iContactListAdapter.msgClick(contact));
        holder.imgCall.setOnClickListener(view -> iContactListAdapter.callClick(contact));
        holder.imgEmail.setOnClickListener(view -> iContactListAdapter.emailClick(contact));
    }

    /**
     * @return size of contacts list
     */
    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvNumber;
        public ImageView imgMsg, imgCall, imgEmail;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.tvNameMain);
            this.tvNumber = (TextView) itemView.findViewById(R.id.tvNumberMain);
            this.imgMsg = (ImageView) itemView.findViewById(R.id.imgMsg);
            this.imgCall = (ImageView) itemView.findViewById(R.id.imgCall);
            this.imgEmail = (ImageView) itemView.findViewById(R.id.imgEmail);
        }
    }

    /**
     * click event listeners for message, call and item views
     */
    public interface IContactListAdapter {
        void itemClick(Contact contact);
        void msgClick(Contact contact);
        void callClick(Contact contact);
        void emailClick(Contact contact);
    }
}