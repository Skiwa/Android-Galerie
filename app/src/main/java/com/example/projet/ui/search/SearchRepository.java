package com.example.projet.ui.search;

import android.app.Application;

import androidx.lifecycle.LiveData;
import java.util.List;
import android.net.Uri;


import com.example.projet.database.*;
import com.example.projet.model.*;

import java.util.List;

public class SearchRepository {

    private PicAnnotationDao picAnnotationDao;
    private LiveData<List<EventAnnotation>> myAllEvent;
    private LiveData<List<ContactAnnotation>> myAllContactAnnotations;
    private LiveData<Integer> countEventAnnotation;

    public SearchRepository(Application application){
        AnnotationDatabase db = AnnotationDatabase.getDatabase(application);
        picAnnotationDao = db.getPicAnnotationDao();

        myAllEvent = picAnnotationDao.loadEventAnnotations();
        //List<PicAnnotation> res = dao.loadAnnotations();
    }

    public LiveData<List<EventAnnotation>> getAllEventAnnotations() {return myAllEvent;}

    //public LiveData<Integer> checkEventAnnotationExist(Uri event){return picAnnotationDao.selectEventAnnotationExist(event);}


}
