package com.mycontacts.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mycontacts.R;
import com.mycontacts.model.Contact;
import com.mycontacts.sql.DatabaseContacts;

public class UpdateContactActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtContactName, edtContactNumber, edtEmail;
    String name, number, email;
    int id;
    DatabaseContacts databaseContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addcontact);

        edtContactName = (EditText) findViewById(R.id.edtContactName);
        edtContactNumber = (EditText) findViewById(R.id.edtContactNumber);
        edtEmail = (EditText) findViewById(R.id.edtEmail);

        Button btnAddContact = (Button) findViewById(R.id.btnAddContact);
        btnAddContact.setText("Update");
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        btnAddContact.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        name = getIntent().getExtras().getString("PhoneNam");
        number = getIntent().getExtras().getString("PhoneNum");
        email = getIntent().getExtras().getString("Email");
        id = getIntent().getExtras().getInt("id");

        edtContactName.setText(name);
        edtContactNumber.setText(number);
        edtEmail.setText(email);

        databaseContacts = new DatabaseContacts(this);
    }

    public void onClick(View v) {

        Intent intent = new Intent(this, ContactListActivity.class);
        if (v.getId() == R.id.btnAddContact) {
            if (edtContactName.getText().toString().equals("") && edtContactNumber.getText().toString().equals("")) {
                Toast.makeText(this, "Please fill name and number fields...", Toast.LENGTH_SHORT).show();
            } else if(edtContactName.getText().toString().equals(name) && edtContactNumber.getText().toString().equals(number)
                    && edtEmail.getText().toString().equals(email)) {
                Toast.makeText(this, "There are no updates to the fields", Toast.LENGTH_SHORT).show();
            } else {
                Contact contact = databaseContacts.getContact(id);
                contact.setName(edtContactName.getText().toString().trim());
                contact.setNumber(edtContactNumber.getText().toString().trim());
                contact.setEmail(edtEmail.getText().toString().trim());
                databaseContacts.updateContact(contact);
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