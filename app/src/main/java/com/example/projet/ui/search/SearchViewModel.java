package com.example.projet.ui.search;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projet.database.AnnotationDatabase;
import com.example.projet.database.PicAnnotationDao;
import com.example.projet.model.EventAnnotation;
import com.example.projet.model.PicAnnotation;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private SearchRepository myRepository;

    private List<EventAnnotation> myAllEventAnnotations;

    private List<Uri> contacts = new ArrayList<>();
    private MutableLiveData<Uri> EventUri = new MutableLiveData<>();
    private MutableLiveData<String> mText;

    public SearchViewModel(Application application) {
        super(application);
        myRepository = new SearchRepository(application);
        //myAllEventAnnotations = myRepository.getAllEventAnnotations();
        mText = new MutableLiveData<>();
        mText.setValue("This is search fragment");
    }

    public void search(ArrayList<Uri> contact){
        Log.i("Search", "Taille de la liste contact "+contact);
        Log.i("Search", "GetEventUri().getValue() "+getEventUri().getValue());

        //Fonction de recherche Event seul;
        if(contact.size() == 0 && getEventUri().getValue() != null)
        {
            //List<PicAnnotation> test = myRepository.checkEventExist(getEventUri().getValue());
            AnnotationDatabase.databaseWriteExecutor.execute(() -> {
                    List<PicAnnotation> test = myRepository.getAllAnnotation();
                Log.i("Search", "On est dans la recherche d'event seul : " + test );
                });
            //Log.i("Search", "On est dans la recherche d'event seul : " + test );
            //Recherche
        }
        if(contact.size() !=0 && getEventUri().getValue() == null)
        {
            Log.i("Search", "On est dans la recherche de contact seul");
            for(int i=0; i< contact.size(); i++) {
                //LiveData<PicAnnotation> test = myRepository.checkContactAnnotationExist(contact.get(i));
                //Log.i("Search", "les photos dans lesquel "+ contact.get(i) + " apparait sont : " + test);
            }
        }
        if(contact.size() !=0 && getEventUri().getValue() != null)
        {
            for(int i=0; i< contact.size(); i++) {
                //LiveData<PicAnnotation> test = myRepository.getPicAnnotationEventContact(contact.get(i), getEventUri().getValue());
                //Log.i("Search", "Contact et événement : " + test);
            }
        }
    }

    public LiveData<String> getText() {
        return mText;
    }

    private MutableLiveData<Uri> getEventUri() { return EventUri;}

    public List<EventAnnotation> getAllEventAnnotations() {return myAllEventAnnotations;}

    //Setter de Event Uri
    public void setEventUri(Uri eventUri){EventUri.setValue(eventUri);}
}

