package com.example.projet.ui.annotations;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.projet.MainActivity;
import com.example.projet.R;
import com.example.projet.ui.ContactListAdapter;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AnnotationsFragment extends Fragment {

    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_CHOOSE_CONTACT = 2;

    private AnnotationsViewModel annotationsViewModel;
    private ImageView selectedImagePreview;
    private ArrayList<Uri> contacts = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        annotationsViewModel = ViewModelProviders.of(this).get(AnnotationsViewModel.class);
        View root = inflater.inflate(R.layout.annotations, container, false);

        selectedImagePreview = root.findViewById(R.id.selectedImagePreview);

        //Fixe l'uri de l'image à celle récupérée si on a ouvert l'appli depuis la gallerie
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
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Selection d'une image depuis l'intent
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImagePreview.setImageURI(data.getData());
        }

        //Selection d'un contact depuis l'intent
        if (requestCode == RESULT_CHOOSE_CONTACT && resultCode == RESULT_OK && null != data) {
            contacts.add(data.getData());

            //Met à jour la liste des contacts
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


    private void updateSelectedContacts(){
        //Mise à jour de la liste des contacts selectionnés



        //-Transforme les contacts en string
        ArrayList<String> contactsNames = new ArrayList<>();
        for(int i = 0; i<this.contacts.size(); i++){
            contactsNames.add(getContactDisplayName(contacts.get(i)));
        }

        //-Crée l'adapter
        ContactListAdapter adapter = new ContactListAdapter(contactsNames, getActivity());

        //-Assigne la vue et l'adapter
        ListView lView = (ListView)getActivity().findViewById(R.id.selectedContacts);
        lView.setAdapter(adapter);
    }
}