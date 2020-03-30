package com.example.annuairepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.annuairepro.model.ContactModel;
import com.example.annuairepro.service.ContactService;

public class UpdateContactActivity extends AppCompatActivity {
    private ContactService cs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);


        cs = ContactService.getInstance(this);
        final ContactModel cm = (ContactModel) getIntent().getExtras().get("updatedContact");
        Button addContact = (Button) findViewById(R.id.update_contact_btn);
        final EditText firstName = (EditText) findViewById(R.id.fn_filed);
        final EditText lastName = (EditText) findViewById(R.id.ln_filed);
        final EditText email = (EditText) findViewById(R.id.email_field);
        final EditText phone = (EditText) findViewById(R.id.num_filed);
        final EditText job = (EditText) findViewById(R.id.job_filed);

        firstName.setText(cm.getFirstName());
        lastName.setText(cm.getLastName());
        email.setText(cm.getEmail());
        phone.setText(cm.getNumber());
        job.setText(cm.getJob());

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cm.setFirstName(firstName.getText().toString());
                cm.setLastName(lastName.getText().toString());
                cm.setEmail(email.getText().toString());
                cm.setNumber(phone.getText().toString());
                cm.setJob(job.getText().toString());

                cs.updateContact(cm);
                Toast.makeText(UpdateContactActivity.this, "Contact bien Ajouté", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra("result",cm);
                        setResult(1,intent);
                        UpdateContactActivity.this.finish();
                    }
                }, 2000);
            }
        });
    }
}
