package com.example.projet.database;

import android.net.Uri;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.projet.model.*;

@Dao
public interface PicAnnotationDao {

    //<------INSERT------>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPictureEvent(EventAnnotation a);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPictureContact(ContactAnnotation a);

    //<------DELETE------>

    @Query("DELETE FROM event_annotation")
    void deleteAll();

    @Query("DELETE FROM contact_annotation")
    void deleteAllContactAnnot();

    @Query("DELETE FROM event_annotation WHERE picUri=:picUri")
    void deletePicEventAnnotation(Uri picUri);

    @Query("DELETE FROM contact_annotation WHERE picUri=:picUri")
    void deletePicContactAnnot(Uri picUri);

    @Query("DELETE FROM contact_annotation WHERE picUri=:picUri AND contactUri=:contactUri")
    void deleteContactAnnotation(Uri picUri, Uri contactUri);

    //<------GET------>

    @Transaction
    @Query("SELECT * from event_annotation")
    List<PicAnnotation> loadAnnotations();

    @Transaction
    @Query("SELECT DISTINCT picUri FROM event_annotation WHERE eventUri=:eventUri")
    List<PicAnnotation> getPicEvent(Uri eventUri);

    @Transaction
    @Query("SELECT DISTINCT picUri FROM contact_annotation WHERE contactUri=:contactUri")
    List<PicAnnotation> getPicUri(Uri contactUri);

    @Transaction
    @Query("SELECT DISTINCT contact_annotation.picUri FROM event_annotation, contact_annotation WHERE event_annotation.eventUri=:eventUri AND contact_annotation.contactUri=:contactUri")
    List<PicAnnotation> getPicEventContact(Uri eventUri,Uri contactUri);

}
