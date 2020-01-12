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
    //public LiveData<List<EventAnnotation>> myEventAnnotation;
    public List<EventAnnotation> myEventAnnotation;
    public List<PicAnnotation> myEvent;
    //public LiveData<List<PicAnnotation>> myEvent;
    //public LiveData<List<ContactAnnotation>> myAllContactAnnotations;
    //public LiveData<Integer> countEventAnnotation;

    public SearchRepository(Application application){
        AnnotationDatabase db = AnnotationDatabase.getDatabase(application);
        picAnnotationDao = db.getPicAnnotationDao();
    }

    public List<PicAnnotation> getAllAnnotation() {return picAnnotationDao.loadAnnotations();}

    public List<PicAnnotation> checkEventExist(Uri event){ return picAnnotationDao.getPicEvent(event);}

    public List<PicAnnotation> checkContactExist(Uri contactUri){ return picAnnotationDao.getPicUri(contactUri);}

    public List<PicAnnotation> checkContactEventExist(Uri eventUri,Uri contactUri){ return picAnnotationDao.getPicEventContact(eventUri, contactUri);}
}
