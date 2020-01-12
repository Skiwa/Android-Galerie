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

    private MutableLiveData<Uri> EventUri = new MutableLiveData<>();
    private MutableLiveData<String> mText;

    public SearchViewModel(Application application) {
        super(application);
        myRepository = new SearchRepository(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is search fragment");
    }

    public void search(ArrayList<Uri> contact){

        //Fonction de recherche Event seul;
        if(contact.size() == 0 && getEventUri().getValue() != null)
        {
            AnnotationDatabase.databaseWriteExecutor.execute(() -> {
                    List<PicAnnotation> special = myRepository.checkEventExist(getEventUri().getValue());
                Log.i("Search", "Le special : " + special );
                });
        }
        if(contact.size() !=0 && getEventUri().getValue() == null)
        {
            Log.i("Search", "On est dans la recherche de contact seul");
            AnnotationDatabase.databaseWriteExecutor.execute(() -> {
                for(int i=0; i< contact.size(); i++) {
                    List<PicAnnotation> special = myRepository.checkContactExist(contact.get(i));
                    Log.i("Search", "Le special : " + special );
                }
            });
        }
        if(contact.size() !=0 && getEventUri().getValue() != null)
        {
            AnnotationDatabase.databaseWriteExecutor.execute(() -> {
                for(int i=0; i< contact.size(); i++) {
                    List<PicAnnotation> special = myRepository.checkContactEventExist(getEventUri().getValue(),contact.get(i));
                    Log.i("Search", "Le special dans contact et Event: " + special );
                }
            });
        }
    }

    public LiveData<String> getText() {
        return mText;
    }

    private MutableLiveData<Uri> getEventUri() { return EventUri;}

    //Setter de Event Uri
    public void setEventUri(Uri eventUri){EventUri.setValue(eventUri);}
}

