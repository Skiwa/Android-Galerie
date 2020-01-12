package com.example.projet.ui.search;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.projet.R;
import com.example.projet.contacts.ContactListAdapter;
import com.example.projet.contacts.RemoveContactListener;
import com.example.projet.events.ChooseEventActivity;

import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class SearchFragment extends Fragment {

    private static int RESULT_CHOOSE_CONTACT = 1;
    private static int RESULT_CHOOSE_EVENT = 2;

    private SearchViewModel searchViewModel;
    private ArrayList<Uri> contacts = new ArrayList<>();
    private ArrayList<String> contactsNames = new ArrayList<>();
    private ContactListAdapter contactsListAdapter;
    private ListView contactsListView;
    private Button buttonChooseEvent;
    private Button buttonChooseContacts;
    private Button buttonSearch;
    private Button hideSearchButton;
    private Button showSearchButton;
    private TextView selectedContactsLabel;
    private TextView selectedEventLabel;
    private TextView selectedEvent;
    private boolean hasSelectedContacts = false;
    private boolean hasSelectedEvent = false;

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
        selectedContactsLabel = root.findViewById(R.id.selected_contacts_label);
        contactsListView = root.findViewById(R.id.selected_contacts);
        buttonChooseContacts = root.findViewById(R.id.choose_contacts);
        buttonChooseEvent = root.findViewById(R.id.choose_event);
        buttonSearch = root.findViewById(R.id.search);
        selectedEvent = root.findViewById(R.id.selected_event);
        selectedEventLabel = root.findViewById(R.id.selected_event_label);
        hideSearchButton = root.findViewById(R.id.hideSearchButton);
        showSearchButton = root.findViewById(R.id.showSearchButton);

        //Cache les élements qui doivent l'être
        selectedEventLabel.setVisibility(View.GONE);
        selectedEvent.setVisibility(View.GONE);
        selectedContactsLabel.setVisibility(View.GONE);
        contactsListView.setVisibility(View.GONE);


        //Selectionner un contact
        buttonChooseContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent iPickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                iPickContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(iPickContact, RESULT_CHOOSE_CONTACT);
            }
        });

        //Séléctionner un event
        buttonChooseEvent.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                Intent intent = new Intent( getActivity(), ChooseEventActivity.class);
                startActivityForResult(intent, RESULT_CHOOSE_EVENT);
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                searchViewModel.search(contacts);
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


        //Retractation du volet de recherche
        hideSearchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                ConstraintLayout searchPanel = root.findViewById(R.id.searchPanel);
                searchPanel.setVisibility(View.GONE);

                showSearchButton.setVisibility(View.VISIBLE);

            }
        });

        //Affichage du volet de recherche
        showSearchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                ConstraintLayout searchPanel = root.findViewById(R.id.searchPanel);
                searchPanel.setVisibility(View.VISIBLE);

                showSearchButton.setVisibility(View.GONE);

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

        //Selection d'un contact
        if (requestCode == RESULT_CHOOSE_CONTACT && resultCode == RESULT_OK && null != data) {

            //Fait apparaitre les éléments
            if(!hasSelectedContacts){
                hasSelectedContacts = true;
                selectedContactsLabel.setVisibility(View.VISIBLE);
                contactsListView.setVisibility(View.VISIBLE);
            }

            //Ajoute le contact recupéré à la liste
            contacts.add(data.getData());

            //Met à jour graphiquement la liste des contacts
            this.updateSelectedContacts();
        }

        //Selection d'un event
        if (requestCode == RESULT_CHOOSE_EVENT && resultCode == RESULT_OK && data != null && data.getData() != null) {

            //Fait apparaitre les éléments
            if(!hasSelectedEvent){
                hasSelectedEvent = true;
                selectedEventLabel.setVisibility(View.VISIBLE);
                selectedEvent.setVisibility(View.VISIBLE);
            }

            Uri uriEvent = data.getData();

            //Récupère le titre et la date formatée
            String eventTitle = getFieldFromUri(CalendarContract.Events.TITLE, uriEvent);
            Date _eventDate = new Date(Long.parseLong(getFieldFromUri(CalendarContract.Events.DTSTART, uriEvent)));
            String eventDate = _eventDate.toLocaleString().substring(0,_eventDate.toLocaleString().length()-9);
            searchViewModel.setEventUri(uriEvent);

            selectedEvent.setText(eventTitle + " (" + eventDate+")");
        }


    }

    /**
     * Récupère un champs voulu pour une URI
     * @param field
     * @param eventUri
     * @return
     */
    private String getFieldFromUri(String field, Uri eventUri){
        String[] projection = new String[]{field};
        Cursor cursor = getActivity().getContentResolver().query(eventUri, projection,null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int numberIndex = cursor.getColumnIndex(field);
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
            this.contactsNames.add(getFieldFromUri(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, contacts.get(i)));
        }

        //-Met à jour la liste
        contactsListAdapter.setList(contactsNames);
        contactsListAdapter.notifyDataSetChanged();
    }

}
