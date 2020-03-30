package com.example.annuairepro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.annuairepro.dao.DataBaseHandler;
import com.example.annuairepro.model.ContactModel;
import com.example.annuairepro.service.ContactService;

import java.util.List;

public class AddContactActivity extends AppCompatActivity {
    private static final String TAG = AddContactActivity.class.getSimpleName();
    private ContactService cs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);

        cs = ContactService.getInstance(this);

        Button addContact = (Button) findViewById(R.id.add_contact_btn);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText firstName = (EditText) findViewById(R.id.fn_filed);
                EditText lastName = (EditText) findViewById(R.id.ln_filed);
                EditText email = (EditText) findViewById(R.id.email_field);
                EditText phone = (EditText) findViewById(R.id.num_filed);
                EditText job = (EditText) findViewById(R.id.job_filed);

                final ContactModel cm = new ContactModel();
                cm.setFirstName(firstName.getText().toString());
                cm.setLastName(lastName.getText().toString());
                cm.setEmail(email.getText().toString());
                cm.setNumber(phone.getText().toString());
                cm.setJob(job.getText().toString());

                cs.addContact(cm);
                Toast.makeText(AddContactActivity.this, "Contact bien Ajouté", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra("result",cm);
                        setResult(1,intent);
                        AddContactActivity.this.finish();
                    }
                }, 2000);
            }
        });

    }
}
