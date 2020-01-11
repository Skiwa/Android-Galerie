package com.example.projet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet.database.AnnotationDatabase;
import com.example.projet.database.Converters;
import com.example.projet.database.PicAnnotationDao;
import com.example.projet.model.ContactAnnotation;
import com.example.projet.model.EventAnnotation;
import com.example.projet.model.PicAnnotation;
import com.example.projet.ui.annotations.AnnotationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public Uri selectedImageUri;
    public TextView selected_event;
    public BottomNavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.home, R.id.annotations, R.id.search)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);



        //Ouvre l'application avec une image
        //-Récupère l'intent
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if(intent.ACTION_SEND.equals(action) && type != null){
            //-Récupère l'uri
            Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
            this.selectedImageUri = imageUri;
            //-Navigue vers l'onglet annotations
            navView.setSelectedItemId(R.id.annotations);
        }

    }
    public Uri getSelectedImageUri(){
        return this.selectedImageUri;
    }

}
