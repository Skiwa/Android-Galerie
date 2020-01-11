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

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private SearchRepository myRepository;

    private LiveData<List<EventAnnotation>> myAllEventAnnotations;

    private List<Uri> contacts = new ArrayList<>();
    private MutableLiveData<Uri> EventUri = new MutableLiveData<>();
    private MutableLiveData<String> mText;

    public SearchViewModel(Application application) {
        super(application);
        myRepository = new SearchRepository(application);
        myAllEventAnnotations = myRepository.getAllEventAnnotations();
        mText = new MutableLiveData<>();
        mText.setValue("This is search fragment");
    }

    public void search(ArrayList<Uri> contact){
        Log.i("Search", "Event URI actuelle :"+contact.toString());
        Log.i("Search", "Taille de la liste contact "+contact);
        Log.i("Search", "GetEventUri().getValue() "+getEventUri().getValue());

        //Log.i("Search", "myRepository.getAllEventAnnotations() "+myRepository.getAllEventAnnotations());
        //Fonction de recherche Event seul;
        if(contact.size() == 0 && getEventUri().getValue() != null)
        {
            Log.i("Search", "On est dans la recherche d'event seul");
            //LiveData<Integer> test = myRepository.checkEventAnnotationExist(getEventUri().getValue());
            //Log.i("Search", "On est dans la recherche d'event seul" + test.toString());
            Log.i("Search", "test");
            //Recherche
        }
        if(contact.size() !=0 && getEventUri().getValue() == null)
        {
            Log.i("Search", "On est dans la recherche de contact seul");
        }
        if(contact.size() !=0 && getEventUri().getValue() != null)
        {
            Log.i("Search", "On est dans la recherche d'event et de contact simultanée");
        }
    }

    public LiveData<String> getText() {
        return mText;
    }

    private MutableLiveData<Uri> getEventUri() { return EventUri;}

    public LiveData<List<EventAnnotation>> getAllEventAnnotations() {return myAllEventAnnotations;}

    //Setter de Event Uri
    public void setEventUri(Uri eventUri){EventUri.setValue(eventUri);}
}

