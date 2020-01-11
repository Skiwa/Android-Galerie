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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPictureEvent(EventAnnotation a);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPictureContact(ContactAnnotation a);

    @Query("DELETE FROM event_annotation")
    void deleteAll();

    @Transaction
    @Query("SELECT * from event_annotation")
    List<PicAnnotation> loadAnnotations(); //LiveData permet d'observer le changement en direct

    @Transaction
    @Query("SELECT * FROM event_annotation")
    LiveData<List<EventAnnotation>> loadEventAnnotations();

    /*@Transaction
    @Query("SELECT * FROM event_annotation WHERE picUri=:picUri")
    LiveData<Integer> selectEventAnnotationExist(Uri picUri);*/

}
