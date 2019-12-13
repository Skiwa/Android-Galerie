package com.example.projet.ui.search;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.projet.R;
import com.example.projet.ui.ContactListAdapter;
import com.example.projet.ui.RemoveContactListener;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class SearchFragment extends Fragment {

    private static int RESULT_CHOOSE_CONTACT = 2;

    private SearchViewModel searchViewModel;
    private ArrayList<Uri> contacts = new ArrayList<>();
    private ArrayList<String> contactsNames = new ArrayList<>();
    private ContactListAdapter contactsListAdapter;
    private ListView contactsListView;
    private Button buttonChooseEvent;
    private Button buttonChooseContacts;
    private Button buttonSearch;

    /**
     * Création de la vue du fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        //Crée la vue
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.search, container, false);

        //Récupère les éléments
        contactsListView = root.findViewById(R.id.selected_contacts);
        buttonChooseContacts = root.findViewById(R.id.choose_contacts);
        buttonChooseEvent = root.findViewById(R.id.choose_event);
        buttonSearch = root.findViewById(R.id.search);

        //TODO: selectionner un event

        //Selectionner un contact
        buttonChooseContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent iPickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                iPickContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(iPickContact, RESULT_CHOOSE_CONTACT);
            }
        });

        //Listing des contacts
        contactsListAdapter = new ContactListAdapter(new ArrayList<String>(),getActivity());
        contactsListView.setAdapter(contactsListAdapter);

        //Evenement lors de la suppression d'un contact
        contactsListAdapter.addRemoveContactListener(new RemoveContactListener() {
            @Override
            public void deleteContact(int position) {
                //Supprime le contact de la liste
                contacts.remove(position);
            }
        });

        return root;
    }


    /**
     * Traitement des intents
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //TODO: selection d'un event

        //Selection d'un contact
        if (requestCode == RESULT_CHOOSE_CONTACT && resultCode == RESULT_OK && null != data) {
            //Ajoute le contact recupéré à la liste
            contacts.add(data.getData());

            //Met à jour graphiquement la liste des contacts
            this.updateSelectedContacts();
        }
    }

    //TODO: isoler ça dans une classe
    /**
     * Renvoie le nom d'un contact en fonction de son Uri
     * @param contactUri
     * @return
     */
    private String getContactDisplayName(Uri contactUri){
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        Cursor cursor = getActivity().getContentResolver().query(contactUri, projection,null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            return cursor.getString(numberIndex);
        }
        return "";
    }

    //TODO: isoler ça dans une classe
    /**
     * Mise à jour des contacts selectionnés
     */
    private void updateSelectedContacts(){
        //-Transforme les contacts en string
        this.contactsNames.clear();
        for(int i = 0; i<this.contacts.size(); i++){
            this.contactsNames.add(getContactDisplayName(contacts.get(i)));
        }

        //-Met à jour la liste
        contactsListAdapter.setList(contactsNames);
        contactsListAdapter.notifyDataSetChanged();
    }
}