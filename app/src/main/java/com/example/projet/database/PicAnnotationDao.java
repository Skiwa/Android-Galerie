package com.example.projet.database;

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

}
