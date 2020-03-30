package com.example.annuairepro.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.annuairepro.AddContactActivity;
import com.example.annuairepro.R;
import com.example.annuairepro.model.ContactModel;
import com.example.annuairepro.service.ContactService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.Inflater;

public class ContactAdapter extends BaseAdapter implements Filterable {
    private static final String TAG = ContactAdapter.class.getSimpleName();
    private List<ContactModel> contacts;
    private List<ContactModel> copyContacts;
    private Activity context;
    private ContactService cs;
    private Filter cutumFilter;


    @Override
    public int getCount() {
        return copyContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return copyContacts.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public ContactAdapter(Activity context, final List<ContactModel> contacts) {
        this.contacts = contacts;
        copyContacts = new ArrayList<>(contacts);
        this.context = context;
        cs = ContactService.getInstance(context);
        cutumFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<ContactModel> filtredList =   new ArrayList<>();
                if(constraint == null || constraint.length() == 0){
                    filtredList.addAll(contacts);

                }else{
                    String pattern = constraint.toString().toLowerCase().trim();
                    for(ContactModel cm : contacts){
                        if((cm.getFirstName() + " " + cm.getLastName()).toLowerCase().contains(pattern)){
                            filtredList.add(cm);
                        }
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filtredList;
                return  results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                copyContacts = (ArrayList<ContactModel>) results.values;
                notifyDataSetChanged();
            }
        };

    }
    @Override
    public View getView(final int position, View view, ViewGroup parent){
        LayoutInflater inflater=context.getLayoutInflater();
        View contactRow = inflater.inflate(R.layout.contact_view,null,true);

        final ContactModel cm =  (ContactModel) getItem(position);
        LinearLayout contactDetaills = (LinearLayout) contactRow.findViewById(R.id.contactDetails);
        TextView contactFN = (TextView) contactRow.findViewById(R.id.contactFN);
        TextView contactLN = (TextView) contactRow.findViewById(R.id.contactLN);
        TextView contactJ = (TextView) contactRow.findViewById(R.id.contactJ);
        TextView contactE = (TextView) contactRow.findViewById(R.id.contactE);
        TextView contactN = (TextView) contactRow.findViewById(R.id.contactN);
        ImageView callIcon = (ImageView) contactRow.findViewById(R.id.callIcon);


        Log.d(TAG," " + position);
        contactFN.setText(cm.getFirstName());
        contactLN.setText(cm.getLastName());
        contactJ.setText(cm.getJob());
        contactE.setText(cm.getEmail());
        contactN.setText(cm.getNumber());


        // events

        //Event on click sur call open call activity to make a call
        callIcon.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
               /* LinearLayout contactView = (LinearLayout) v.getParent();
                LinearLayout details = (LinearLayout) contactView.getChildAt(0);
                TextView number = (TextView)  details.getChildAt(3);*/
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + cm.getNumber()));
                context.startActivity(intent);
            }
        });

        //Event clickng on detaills show options to delete or update a contact
        return contactRow;
    }

    private void openAddContactActivity(int position){

        Intent intent = new Intent(context, AddContactActivity.class);
        intent.putExtra("contact",(ContactModel) getItem(position)) ;
        context.startActivity(intent);
    }
    public List<ContactModel> getList(){
        return copyContacts;
    }
    @Override
    public Filter getFilter() {
        return cutumFilter;
    }
}
