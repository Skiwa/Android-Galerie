package com.example.projet.model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.*;

@Entity(tableName = "event_annotation")
public class EventAnnotation {

    @PrimaryKey
    @NonNull
    private Uri picUri;
    private Uri eventUri;


    public EventAnnotation(Uri picUri, Uri eventUri) {
        this.picUri=picUri;
        this.eventUri=eventUri;
    }

    public Uri getPicUri() {
        return picUri;
    }

    public Uri getEventUri() {
        return eventUri;
    }


    @NonNull
    @Override
    public String toString() {
        return "["+picUri+","+eventUri+"]";
    }
}
