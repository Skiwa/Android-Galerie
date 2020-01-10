package com.example.projet.database;
import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.projet.model.*;

//Pas oublier de modifier la version en cas de modification de la Db
@Database(entities={EventAnnotation.class, ContactAnnotation.class}, version=1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AnnotationDatabase extends RoomDatabase {

    //Utilise le DAO présent dans PicAnnotationDao
    public abstract PicAnnotationDao getPicAnnotationDao();

    //Singleton to prevent having multiple instances
    private static volatile AnnotationDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
        Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //Crée une database du nom annotation_database
    public static AnnotationDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AnnotationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AnnotationDatabase.class, "annotation_database")
                        .build();
                }
            }
        }
        return INSTANCE;
    }
}
