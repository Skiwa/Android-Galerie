package com.example.projet.ui.search;

import android.app.Application;

import androidx.lifecycle.LiveData;
import java.util.List;
import android.net.Uri;


import com.example.projet.database.*;
import com.example.projet.model.*;

import java.util.List;

public class SearchRepository {

    public PicAnnotationDao picAnnotationDao;
    public LiveData<List<EventAnnotation>> myAllEvent;
    public LiveData<List<ContactAnnotation>> myAllContactAnnotations;
    public LiveData<Integer> countEventAnnotation;

    public SearchRepository(Application application){
        AnnotationDatabase db = AnnotationDatabase.getDatabase(application);
        picAnnotationDao = db.getPicAnnotationDao();

        myAllEvent = picAnnotationDao.loadEventAnnotations();
        //List<PicAnnotation> res = dao.loadAnnotations();
    }

    public LiveData<List<EventAnnotation>> getAllEventAnnotations() {return myAllEvent;}

    public LiveData<PicAnnotation> checkEventAnnotationExist(Uri event){return picAnnotationDao.getPicAnnotationContact(event);}

    public LiveData<PicAnnotation> checkContactAnnotationExist(Uri contact){return picAnnotationDao.getPicAnnotationEvent(contact);}

    public LiveData<PicAnnotation> getPicAnnotationEventContact(Uri contact, Uri event) {return picAnnotationDao.getPicAnnotationEventContact(contact, event);}
}
