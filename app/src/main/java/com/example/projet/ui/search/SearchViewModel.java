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

    public SearchViewModel(Application application) {
        super(application);
        myRepository = new SearchRepository(application);
    }

    //Get the Pics Uri from an event or a contact or both
    public ArrayList<Uri> search(ArrayList<Uri> contact, Uri event){

        ArrayList<Uri> list = new ArrayList<Uri>();

        //Check if the Data send is only an event
        if(contact.size() == 0 && event != null)
        {
            AnnotationDatabase.databaseWriteExecutor.execute(() -> {
                    List<PicAnnotation> special = myRepository.checkEventExist(event);
                    for(int i = 0;i<special.size();i++){
                        list.add(special.get(i).getPicUri());
                    }
                });
        }
        //Check if the Data send is only a contact
        else if(contact.size() !=0 && event == null)
        {
            AnnotationDatabase.databaseWriteExecutor.execute(() -> {
                for(int i=0; i< contact.size(); i++) {
                    List<PicAnnotation> special = myRepository.checkContactExist(contact.get(i));
                    for(int j = 0;j<special.size();j++){
                        list.add(special.get(j).getPicUri());
                    }
                }
            });
        }
        //Check if the Data send is an event and a contact
        else {
            AnnotationDatabase.databaseWriteExecutor.execute(() -> {
                for(int i=0; i< contact.size(); i++) {
                    List<PicAnnotation> special = myRepository.checkContactEventExist(event,contact.get(i));
                    for(int j = 0;j<special.size();j++){
                        list.add(special.get(j).getPicUri());
                    }
                }
            });
        }

        return list;
    }
}

