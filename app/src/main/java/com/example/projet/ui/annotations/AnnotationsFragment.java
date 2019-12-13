package com.example.projet.ui.annotations;

import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.projet.MainActivity;
import com.example.projet.R;
import com.example.projet.ui.ContactListAdapter;
import com.example.projet.ui.RemoveContactListener;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AnnotationsFragment extends Fragment {

    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_CHOOSE_CONTACT = 2;

    private AnnotationsViewModel annotationsViewModel;
    private ImageView selectedImagePreview;
    private ArrayList<Uri> contacts = new ArrayList<>();
    private ArrayList<String> contactsNames = new ArrayList<>();
    private ContactListAdapter contactsListAdapter;
    private ListView contactsListView;

    /**
     * Création de la vue du fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        //Crée la vue
        annotationsViewModel = ViewModelProviders.of(this).get(AnnotationsViewModel.class);
        View root = inflater.inflate(R.layout.annotations, container, false);

        //Récupère les élements
        selectedImagePreview = root.findViewById(R.id.selectedImagePreview);
        contactsListView = root.findViewById(R.id.selectedContacts);

        //Fixe l'uri de l'image à celle récupérée si on a ouvert l'appli depuis la galerie
        selectedImagePreview.setImageURI(((MainActivity) getActivity()).getSelectedImageUri());

        //Selectionner une image
        Button buttonChoose_img = root.findViewById(R.id.choose_img);
        buttonChoose_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent iPickImage = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iPickImage, RESULT_LOAD_IMAGE);
            }

        });

        //Selectionner un contact
        Button buttonChoose_contact = root.findViewById(R.id.choose_contact);
        buttonChoose_contact.setOnClickListener(new View.OnClickListener() {
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

        //Selection d'une image
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            //Fixe l'image avec l'uri récupérée
            selectedImagePreview.setImageURI(data.getData());
        }

        //Selection d'un contact
        if (requestCode == RESULT_CHOOSE_CONTACT && resultCode == RESULT_OK && null != data) {
            //Ajoute le contact recupéré à la liste
            contacts.add(data.getData());

            //Met à jour graphiquement la liste des contacts
            this.updateSelectedContacts();
        }
    }


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