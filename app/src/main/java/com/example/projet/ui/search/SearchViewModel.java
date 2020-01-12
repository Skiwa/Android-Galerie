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

    public ArrayList<Uri> search(ArrayList<Uri> contact, Uri event){

        ArrayList<Uri> list = new ArrayList<Uri>();

        //Fonction de recherche Event seul;
        if(contact.size() == 0 && event != null)
        {
            Log.i("Search", "On est dans la recherche d'event seul");
            AnnotationDatabase.databaseWriteExecutor.execute(() -> {
                    List<PicAnnotation> special = myRepository.checkEventExist(event);
                    for(int i = 0;i<special.size();i++){
                        list.add(special.get(i).getPicUri());
                    }
                });
        }
        else if(contact.size() !=0 && event == null)
        {
            Log.i("Search", "On est dans la recherche de contact seul");
            AnnotationDatabase.databaseWriteExecutor.execute(() -> {
                for(int i=0; i< contact.size(); i++) {
                    List<PicAnnotation> special = myRepository.checkContactExist(contact.get(i));
                    for(int j = 0;j<special.size();j++){
                        list.add(special.get(j).getPicUri());
                    }
                    Log.i("Search", "Le special 2 : " + special );
                }
            });
        }
        else {
            AnnotationDatabase.databaseWriteExecutor.execute(() -> {
                for(int i=0; i< contact.size(); i++) {
                    List<PicAnnotation> special = myRepository.checkContactEventExist(event,contact.get(i));
                    for(int j = 0;j<special.size();j++){
                        list.add(special.get(j).getPicUri());
                    }
                    Log.i("Search", "Le special dans contact et Event: " + special );
                }
            });
        }

        return list;
    }

    public LiveData<String> getText() {
        return mText;
    }

    //private MutableLiveData<Uri> getEventUri() { return EventUri;}

    //Setter de Event Uri
    //public void setEventUri(Uri eventUri){EventUri.setValue(eventUri);}
}

