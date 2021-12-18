package com.mycontacts.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mycontacts.R;
import com.mycontacts.model.Contact;
import com.mycontacts.sql.DatabaseContacts;

public class AddContactActivity extends AppCompatActivity implements OnClickListener {

    private EditText edtContactName, edtContactNumber, edtEmail;
    DatabaseContacts databaseContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addcontact);

        edtContactName = (EditText) findViewById(R.id.edtContactName);
        edtContactNumber = (EditText) findViewById(R.id.edtContactNumber);
        edtEmail = (EditText) findViewById(R.id.edtEmail);

        Button btnAddContact = (Button) findViewById(R.id.btnAddContact);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        btnAddContact.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        databaseContacts = new DatabaseContacts(this);
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, ContactListActivity.class);
        if (v.getId() == R.id.btnAddContact) {
            if (edtContactName.getText().toString().equals("") && edtContactNumber.getText().toString().equals("")) {
                Toast.makeText(this, "Please fill both fields...", Toast.LENGTH_SHORT).show();
            } else {
                databaseContacts.addContact(new Contact(edtContactName.getText().toString().trim(),
                        edtContactNumber.getText().toString().trim(), edtEmail.getText().toString().trim()));
                edtContactName.setText("");
                edtContactNumber.setText("");
                edtEmail.setText("");
                startActivity(intent);
                finish();
            }
        } else if (v.getId() == R.id.btnCancel) {
            finish();
        }
    }
}