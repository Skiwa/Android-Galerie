package com.example.projet.ui.search;
import android.app.Application;
import java.util.List;
import android.net.Uri;
import com.example.projet.database.*;
import com.example.projet.model.*;


public class SearchRepository {

    public PicAnnotationDao picAnnotationDao;
    public SearchRepository(Application application){
        AnnotationDatabase db = AnnotationDatabase.getDatabase(application);
        picAnnotationDao = db.getPicAnnotationDao();
    }

    public List<PicAnnotation> getAllAnnotation() {return picAnnotationDao.loadAnnotations();}

    /**Get all the picUri for an event**/
    public List<PicAnnotation> checkEventExist(Uri event){ return picAnnotationDao.getPicEvent(event);}

    /**Get all the PicUri containing two selected person*/
    public List<PicAnnotation> checkTwoContactExist(List<Uri> contactUri) { return picAnnotationDao.getPicMultipleContact(contactUri);}

    /**Get all the picUri for a contact*/
    public List<PicAnnotation> checkContactExist(Uri contactUri){ return picAnnotationDao.getPicUri(contactUri);}

    /**Get all the picUri for an event or a contact*/
    public List<PicAnnotation> checkContactEventExist(Uri eventUri,Uri contactUri){ return picAnnotationDao.getPicEventContact(eventUri, contactUri);}
}
