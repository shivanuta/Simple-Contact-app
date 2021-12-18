package com.mycontacts.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mycontacts.R;
import com.mycontacts.sql.DatabaseContacts;

public class DeleteContactsActivity extends AppCompatActivity implements OnClickListener {

    private int id;
    private DatabaseContacts databaseContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deletecontact);

        String name = getIntent().getExtras().getString("PhoneNam");
        String number = getIntent().getExtras().getString("PhoneNum");
        id = getIntent().getExtras().getInt("id");

        TextView edtDeleteContactNumber = (TextView) findViewById(R.id.edtDeleteContactNumber);
        TextView edtDeleteContactName = (TextView) findViewById(R.id.edtDeleteContactName);
        edtDeleteContactNumber.setText(number);
        edtDeleteContactName.setText(name);

        Button btnDeleteContact = (Button) findViewById(R.id.btndeleteContact);
        Button btnDeleteCancel = (Button) findViewById(R.id.btnDeleteCancel);
        btnDeleteContact.setOnClickListener(this);
        btnDeleteCancel.setOnClickListener(this);

        databaseContacts = new DatabaseContacts(this);
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, ContactListActivity.class);
        if (v.getId() == R.id.btndeleteContact) {
            databaseContacts.deleteContact(id);
            startActivity(intent);
            Toast.makeText(this, "Row/s deleted Successfully..", Toast.LENGTH_SHORT).show();
            finish();
        } else if (v.getId() == R.id.btnDeleteCancel) {
            finish();
        }
    }
}
