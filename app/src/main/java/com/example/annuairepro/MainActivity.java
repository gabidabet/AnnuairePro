package com.example.annuairepro;

import android.content.Intent;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.example.annuairepro.adapter.ContactAdapter;
import com.example.annuairepro.model.ContactModel;
import com.example.annuairepro.service.ContactService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ContactService cs;
    private ListView listView;
    private FloatingActionButton addButton;
    private List<ContactModel> listContact;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolabr = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolabr);
        addButton = (FloatingActionButton) findViewById(R.id.add_contact_button);
        cs = ContactService.getInstance(this.getApplicationContext());

        listContact = cs.getContactsFromDB();
        ContactAdapter ca = new ContactAdapter(this,listContact);
        listView = (ListView) findViewById(R.id.contactsList);
        listView.setAdapter(ca);
        //set events :
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddContact();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                openPopup(position,(ListView)parent);
                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ContactAdapter ca = (ContactAdapter) listView.getAdapter();
                ca.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
    @Override
     public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.search){
            Toast.makeText(getApplicationContext(),"search clicked",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    private void openPopup(final int position, final ListView listView){
        Button updateContact;
        Button deleteContact;
        LayoutInflater inflater = getLayoutInflater();
        final PopupWindow popupWindow = new PopupWindow(MainActivity.this);
        popupWindow.setFocusable(true);
        popupWindow.setContentView(inflater.inflate(R.layout.activity_udcontact_pop_up,null));
       /* Intent intent = new Intent(this, popupWindow.getClass());
        //popupWindow.setWidth(popupWindow.getContentView().getWidth());*/
        //set up buttons
        updateContact = (Button) popupWindow.getContentView().findViewById(R.id.update_button);
        deleteContact = (Button) popupWindow.getContentView().findViewById(R.id.delete_button);

        updateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUpdateActivity(listContact.get(position));
                popupWindow.dismiss();
            }
        });
        deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cs.deleteContact(listContact.get(position).getId());
                Log.d(TAG, "onClick:  " + listContact.get(position).getFirstName());
                listContact.remove(position);

                ContactAdapter ca = (ContactAdapter) listView.getAdapter();
                ca.getList().remove(position);
                ca.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.CENTER, 0, 0);

    }
    private void callAddContact(){
        Intent intent = new Intent(this, AddContactActivity.class);
        startActivityForResult(intent, 1);
    }
    private void callUpdateActivity(ContactModel cm){
        Intent intent = new Intent(this, UpdateContactActivity.class);
        intent.putExtra("updatedContact",cm);
        startActivityForResult(intent, 2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1){
            ContactAdapter ca = (ContactAdapter) listView.getAdapter();
            Log.d(TAG, "onActivityResult: CalledHELLO");
            ContactModel cm = (ContactModel) data.getExtras().get("result");
            if(cm != null){
                listContact.add(cm);
                ca.getList().add(cm);
                ca.notifyDataSetChanged();
            }
        }else if(requestCode == 2){
            ContactAdapter ca = (ContactAdapter) listView.getAdapter();
            ca.notifyDataSetChanged();
        }
    }
}
