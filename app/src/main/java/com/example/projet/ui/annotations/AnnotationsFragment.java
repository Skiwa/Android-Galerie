package com.example.projet.ui.annotations;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.projet.ChooseEventActivity;
import com.example.projet.MainActivity;
import com.example.projet.R;
import com.example.projet.database.AnnotationDatabase;
import com.example.projet.database.Converters;
import com.example.projet.database.PicAnnotationDao;
import com.example.projet.model.ContactAnnotation;
import com.example.projet.model.EventAnnotation;
import com.example.projet.model.PicAnnotation;
import com.example.projet.ui.ContactListAdapter;
import com.example.projet.ui.RemoveContactListener;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AnnotationsFragment extends Fragment {

    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_CHOOSE_CONTACT = 2;
    private static int RESULT_CHOOSE_EVENT = 3;
    private static Uri UriPic;

    private PicAnnotation PicAnnotation;
    private AnnotationsViewModel annotationsViewModel;
    private PicAnnotation mMainActivityModel;
    private ImageView selectedImagePreview;
    private ArrayList<Uri> contacts = new ArrayList<>();
    private ArrayList<String> contactsNames = new ArrayList<>();
    private ContactListAdapter contactsListAdapter;
    private Converters converters;
    private ListView contactsListView;
    private Button buttonChooseImg;
    private Button buttonChooseContacts;
    private Button buttonChooseEvent;
    public Button save_annotation;
    private TextView textSelectedEvent;
    private TextView textSelectedPic;


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
        selectedImagePreview = root.findViewById(R.id.selected_image_preview);
        contactsListView = root.findViewById(R.id.selected_contacts);
        buttonChooseImg = root.findViewById(R.id.choose_img);
        buttonChooseContacts = root.findViewById(R.id.choose_contacts);
        buttonChooseEvent = root.findViewById(R.id.choose_event);
        //PicAnnotation = new PicAnnotation();
        textSelectedEvent = root.findViewById(R.id.selected_event);
        textSelectedPic = root.findViewById(R.id.selected_pic);
        save_annotation = root.findViewById(R.id.save_annotation);

        //Fixe l'uri de l'image à celle récupérée si on a ouvert l'appli depuis la galerie
        selectedImagePreview.setImageURI(((MainActivity) getActivity()).getSelectedImageUri());

        //Séléctionner un event
        buttonChooseEvent.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                Intent intent = new Intent( getActivity(), ChooseEventActivity.class);
                startActivityForResult(intent, RESULT_CHOOSE_EVENT);
            }
        });

        save_annotation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Log.i("DataBase", " Clic sur le bouton");

                AnnotationDatabase.databaseWriteExecutor.execute(() -> {

                    AnnotationDatabase db = AnnotationDatabase.getDatabase(getActivity().getApplication());
                    PicAnnotationDao dao = db.getPicAnnotationDao();

                    //A l'Event annotation on envoie l'URI de la photo et on retrouve l'URI stocké dans un textView
                    EventAnnotation annot = new EventAnnotation(
                        UriPic,
                        Uri.withAppendedPath(CalendarContract.Events.CONTENT_URI, (String) textSelectedEvent.getText())
                    );
                    dao.insertPictureEvent(annot);
                    Log.i("DataBase", "On ajoute à la BDD : "+ annot.toString());

                    //Au Contact annotation on envoie l'URI de la photo
                    //Récupérer les éléments de **Contacts** et les enlever de son tableau
                    for( int i=0; i<contacts.size(); i++) {
                        try {
                            ContactAnnotation ca = new ContactAnnotation(
                                UriPic,
                                contacts.get(i)
                            );
                            dao.insertPictureContact(ca);
                            Log.i("DataBase", "On ajoute à la BDD : "+ ca.toString());

                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

                    }

                    List<PicAnnotation> res = dao.loadAnnotations();
                    Log.v("DataBase", "coucou");
                    for (PicAnnotation a : res) {
                        Log.i("", "Une nouvelle entrée dans la BDD");
                        Log.i("DataBase", a.toString());
                    }
                });
            }
        });


        //Selectionner une image
        buttonChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent iPickImage = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iPickImage, RESULT_LOAD_IMAGE);
            }

        });

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

        //Selection d'une image
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            //Fixe l'image avec l'uri récupérée
            selectedImagePreview.setImageURI(data.getData());
            Log.i("DEBUG","Récupération des infos de l'image: "+data.getData().getPathSegments().get(1));
            UriPic = data.getData();
            Log.i("DEBUG","Récupération des infos dans l'uri de la photo : "+UriPic);
        }

        //Selection d'un contact
        if (requestCode == RESULT_CHOOSE_CONTACT && resultCode == RESULT_OK && null != data) {
            //Ajoute le contact recupéré à la liste
            contacts.add(data.getData());
            Log.i("DEBUG","Récupération des infos de contacts lors de la sélection : "+contacts);

            //Met à jour graphiquement la liste des contacts
            this.updateSelectedContacts();
        }

        //Selection d'un event
        if (requestCode == RESULT_CHOOSE_EVENT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uriEvent = data.getData();
            String uriToString = converters.UriToString(uriEvent);
            String test = uriEvent.getPathSegments().get(1);
            textSelectedEvent.setText(test);

            //Permet de récupérerer le numéro de l'événement qui peut être utiliser dans l'activité
            Log.i("DEBUG","Récupération des infos dans l'uri"+test);
            Log.i("DEBUG","Récupération des infos dans l'uri puis converters : "+uriToString);

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
