package com.example.projet.ui.annotations;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.projet.MainActivity;
import com.example.projet.R;
import com.example.projet.database.AnnotationDatabase;
import com.example.projet.database.Converters;
import com.example.projet.database.PicAnnotationDao;
import com.example.projet.model.ContactAnnotation;
import com.example.projet.model.EventAnnotation;
import com.example.projet.model.PicAnnotation;
import com.example.projet.contacts.ContactListAdapter;
import com.example.projet.contacts.RemoveContactListener;

import java.util.ArrayList;
import com.example.projet.events.ChooseEventActivity;

import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class AnnotationsFragment extends Fragment {

    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_CHOOSE_CONTACT = 2;
    private static int RESULT_CHOOSE_EVENT = 3;

    private AnnotationsViewModel annotationsViewModel;
    private ImageView selectedImagePreview;
    private TextView selectedContactsLabel;
    private TextView selectedEventLabel;
    private ArrayList<String> contactsNames = new ArrayList<>();
    private ContactListAdapter contactsListAdapter;
    private TextView selectedEvent;
    private ListView contactsListView;
    private Button buttonChooseImg;
    private Button buttonChooseContacts;
    private Button buttonChooseEvent;

    private Button buttonSave;

    private boolean hasSelectedImage = false;
    private boolean hasSelectedContacts = false;
    private boolean hasSelectedEvent = false;

    private Uri imageURI;
    private ArrayList<Uri> contacts = new ArrayList<>();
    private Uri eventURI;

    /**
     * Création de la vue du fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        annotationsViewModel = ViewModelProviders.of(this).get(AnnotationsViewModel.class);

        View root = inflater.inflate(R.layout.annotations, container, false);

        /** Selectionne les éléments*/
        selectedImagePreview = root.findViewById(R.id.selected_image_preview);
        selectedContactsLabel = root.findViewById(R.id.selected_contacts_label);
        contactsListView = root.findViewById(R.id.selected_contacts);
        selectedEvent = root.findViewById(R.id.selected_event);
        selectedEventLabel = root.findViewById(R.id.selected_event_label);
        buttonChooseImg = root.findViewById(R.id.choose_img);
        buttonChooseContacts = root.findViewById(R.id.choose_contacts);
        buttonChooseEvent = root.findViewById(R.id.choose_event);
        buttonSave = root.findViewById(R.id.save_annotation);

        /**Cache les élements qui doivent l'être*/
        selectedImagePreview.setVisibility(View.GONE);
        selectedEventLabel.setVisibility(View.GONE);
        selectedEvent.setVisibility(View.GONE);
        selectedContactsLabel.setVisibility(View.GONE);
        contactsListView.setVisibility(View.GONE);

        /**Fixe l'uri de l'image à celle récupérée si on a ouvert l'appli depuis la galerie*/
        if(((MainActivity) getActivity()).getSelectedImageUri() != null){

            /**Fait apparaitre les éléments*/
            if(!hasSelectedImage){
                hasSelectedImage = true;
                selectedImagePreview.setVisibility(View.VISIBLE);
            }
            imageURI = ((MainActivity) getActivity()).getSelectedImageUri();
            selectedImagePreview.setImageURI(((MainActivity) getActivity()).getSelectedImageUri());
        }

        /**Selectionner un event
         *
         */
        buttonChooseEvent.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                Intent intent = new Intent( getActivity(), ChooseEventActivity.class);
                startActivityForResult(intent, RESULT_CHOOSE_EVENT);
            }
        });

        /**Selectionner une image
         *
         */
        buttonChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent iPickImage = new Intent(Intent.ACTION_OPEN_DOCUMENT,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iPickImage, RESULT_LOAD_IMAGE);
            }

        });

        /**Selectionner un contact
         *
         */
        buttonChooseContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent iPickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                iPickContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(iPickContact, RESULT_CHOOSE_CONTACT);
            }
        });

        /**Enregistrer une annotation
         *
         */
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                saveAnnotation(imageURI, eventURI, contacts);
            }
        });

        /**Listing des contacts
         *
         */
        contactsListAdapter = new ContactListAdapter(new ArrayList<String>(),getActivity());
        contactsListView.setAdapter(contactsListAdapter);

        /**Suppression d'un contact
         *
         */
        contactsListAdapter.addRemoveContactListener(new RemoveContactListener() {
            @Override
            public void deleteContact(int position) {
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

        /**Selection d'une image
         *
         */
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            /**Fait apparaitre les éléments*/
            if(!hasSelectedImage){
                hasSelectedImage = true;
                selectedImagePreview.setVisibility(View.VISIBLE);
            }

            /**Fixe l'image avec l'uri récupérée*/
            imageURI = data.getData();
            selectedImagePreview.setImageURI(imageURI);
        }

        /**Selection d'un contact
         *
         */
        if (requestCode == RESULT_CHOOSE_CONTACT && resultCode == RESULT_OK && null != data) {

            /**Fait apparaitre les éléments*/
            if(!hasSelectedContacts){
                hasSelectedContacts = true;
                selectedContactsLabel.setVisibility(View.VISIBLE);
                contactsListView.setVisibility(View.VISIBLE);
            }

            /**Ajoute le contact recupéré à la liste*/
            contacts.add(data.getData());

            /**Met à jour graphiquement la liste des contacts*/
            this.updateSelectedContacts();
        }

        /**Selection d'un event
         *
         */
        if (requestCode == RESULT_CHOOSE_EVENT && resultCode == RESULT_OK && data != null && data.getData() != null) {

            /**Fait apparaitre les éléments*/
            if(!hasSelectedEvent){
                hasSelectedEvent = true;
                selectedEventLabel.setVisibility(View.VISIBLE);
                selectedEvent.setVisibility(View.VISIBLE);
            }

            eventURI = data.getData();

            /**Récupère le titre et la date formatée*/
            String eventTitle = getFieldFromUri(CalendarContract.Events.TITLE, eventURI);
            Date _eventDate = new Date(Long.parseLong(getFieldFromUri(CalendarContract.Events.DTSTART, eventURI)));
            String eventDate = _eventDate.toLocaleString().substring(0,_eventDate.toLocaleString().length()-9);

            selectedEvent.setText(eventTitle + " (" + eventDate+")");
        }
    }

    /**
     * Mise à jour des contacts selectionnés
     */
    private void updateSelectedContacts(){
        this.contactsNames.clear();
        for(int i = 0; i<this.contacts.size(); i++){
            this.contactsNames.add(getFieldFromUri(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, contacts.get(i)));
        }

        contactsListAdapter.setList(contactsNames);
        contactsListAdapter.notifyDataSetChanged();
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

    /**
     * Enregistre l'annotation en BDD
     */
    private void saveAnnotation(Uri imageURI, Uri eventURI, ArrayList<Uri> contacts){

        if(imageURI != null && eventURI != null && contacts.size() != 0){

            AnnotationDatabase.databaseWriteExecutor.execute(() -> {

                AnnotationDatabase db = AnnotationDatabase.getDatabase(getActivity().getApplication());
                PicAnnotationDao dao = db.getPicAnnotationDao();
                EventAnnotation annot = new EventAnnotation(imageURI, eventURI);
                dao.insertPictureEvent(annot);

                for( int i=0; i<contacts.size(); i++) {
                    try {
                        ContactAnnotation ca = new ContactAnnotation(
                                imageURI,
                                contacts.get(i)
                        );
                        dao.insertPictureContact(ca);

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
            ((MainActivity) this.getActivity()).navView.setSelectedItemId(R.id.home);

        }
        else{
            Toast.makeText(this.getContext(),"Veuillez selectionner une image, un évènement et au moins un contact pour enregistrer.", Toast.LENGTH_LONG).show();
        }

    }
}

