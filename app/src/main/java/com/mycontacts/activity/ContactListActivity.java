package com.mycontacts.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mycontacts.R;
import com.mycontacts.adapter.ContactListAdapter;
import com.mycontacts.helper.ContactHelper;
import com.mycontacts.model.Contact;
import com.mycontacts.sql.DatabaseContacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ContactListActivity extends AppCompatActivity implements ContactListAdapter.IContactListAdapter {

    private ArrayList<Contact> contactArrayList;
    private LinkedHashMap<String, Contact> contactDetails;
    private ContactListAdapter contactListAdapter;
    private DatabaseContacts databaseContacts;

    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvContactList);
        contactListAdapter = new ContactListAdapter(this);
        recyclerView.setHasFixedSize(true);

        contactArrayList = new ArrayList<>();
        contactDetails = new LinkedHashMap<>();
        contactListAdapter.setList(contactArrayList);

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(contactListAdapter);

        databaseContacts = new DatabaseContacts(this);

        ArrayList<Contact> contacts = databaseContacts.getContactsData();
        if (contacts.size() > 0) {
            Collections.sort(contacts, (contact, contact2) -> contact.getName().compareTo(contact2.getName()));
            contactListAdapter.setList(contacts);
        } else {
            requestContactPermission();
        }

    }

    /**
     * Getting contacts from the device and saving to db
     */
    private void getContacts() {
        Cursor contactsCursor = ContactHelper.getContactCursor(getContentResolver());
        contactsCursor.moveToFirst();
        while (!contactsCursor.isAfterLast()) {
            contactDetails.put(contactsCursor.getString(1), new Contact(contactsCursor.getString(1),
                    contactsCursor.getString(2)));
            contactsCursor.moveToNext();
        }
        for (Map.Entry<String, Contact> entry : contactDetails.entrySet()) {
            Contact contact = entry.getValue();
            contactArrayList.add(contact);
        }
        contactListAdapter.setList(contactArrayList);
        databaseContacts.saveContacts(contactArrayList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater imf = getMenuInflater();
        imf.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addItem) {
            navigateToAddContactActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Requesting permission to access contacts from device
     */
    public void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Read Contacts permission");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable access to contacts.");
                    builder.setOnDismissListener(dialog -> requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}
                            , PERMISSIONS_REQUEST_READ_CONTACTS));
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                getContacts();
            }
        } else {
            getContacts();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts();
                } else {
                    Toast.makeText(this, "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void itemClick(Contact contact) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to..");

        final View customLayout = getLayoutInflater().inflate(R.layout.custom, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();

        TextView tvDelete = customLayout.findViewById(R.id.textDelete);
        TextView tvEdit = customLayout.findViewById(R.id.textEdit);
        tvDelete.setOnClickListener(view -> {
            dialog.dismiss();
            navigateToDeleteContactActivity(contact);
        });
        tvEdit.setOnClickListener(view -> {
            dialog.dismiss();
            navigateToUpdateContactActivity(contact);
        });

        dialog.show();
    }

    /**
     * Navigating to Add Contact Activity
     */
    private void navigateToAddContactActivity() {
        Intent intent = new Intent(ContactListActivity.this, AddContactActivity.class);
        startActivity(intent);
    }

    /**
     * Navigating to Update Contact Activity
     * @param contact - to be updated in db
     */
    private void navigateToUpdateContactActivity(Contact contact) {
        Intent intent = new Intent(ContactListActivity.this, UpdateContactActivity.class);
        intent.putExtra("PhoneNum", contact.getNumber());
        intent.putExtra("PhoneNam", contact.getName());
        intent.putExtra("Email", contact.getEmail());
        intent.putExtra("id", contact.getId());
        startActivity(intent);
    }

    /**
     * Navigating to Delete Contact Activity
     * @param contact - to be deleted from db
     */
    private void navigateToDeleteContactActivity(Contact contact) {
        Intent intent = new Intent(ContactListActivity.this, DeleteContactsActivity.class);
        intent.putExtra("PhoneNum", contact.getNumber());
        intent.putExtra("PhoneNam", contact.getName());
        intent.putExtra("id", contact.getId());
        startActivity(intent);
    }

    /**
     * Opening message sending apps
     * @param contact - to which message has to sent
     */
    @Override
    public void msgClick(Contact contact) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contact.getNumber()));
        startActivity(intent);
    }

    /**
     * Opening call option from device
     * @param contact - to which call has to made
     */
    @Override
    public void callClick(Contact contact) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.getNumber()));
        startActivity(intent);
    }

    /**
     * Opening email option from device
     * @param contact - to which call has to made
     */
    @Override
    public void emailClick(Contact contact) {
        String email = contact.getEmail();
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}

