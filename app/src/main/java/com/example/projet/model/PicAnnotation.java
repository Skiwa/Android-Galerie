package com.example.projet.model;
import android.net.Uri;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.projet.model.ContactAnnotation;

public class PicAnnotation {

    //Embedded permet de récupérer les champs d'eventAnnot
    @Embedded
    public EventAnnotation eventAnnot;
    @Relation(
        parentColumn = "picUri",
        entityColumn = "picUri",
        entity = ContactAnnotation.class,
        projection = {"contactUri"}
    )
    public List<Uri> contactUris;

    public Uri getPicUri(){ return eventAnnot.getPicUri(); }
    public Uri getEventUri(){ return eventAnnot.getEventUri(); }
    public List<Uri> getContactUris() {return contactUris; }

    @NonNull
    @Override
    public String toString() {
        String res =  eventAnnot.getPicUri()+","+eventAnnot.getEventUri()+","+contactUris+"]";
        return res;
    }
}
