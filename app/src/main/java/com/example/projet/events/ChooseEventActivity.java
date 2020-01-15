package com.example.projet.events;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.OnItemActivatedListener;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet.R;

public class ChooseEventActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener, OnItemActivatedListener<Long> {

    private final static int MY_PERMISSIONS_REQUEST_READ_CALENDARS=0;
    private EventListAdapter adapter;

    private boolean readCalendarAuthorized=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_event);

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);

        RecyclerView recyclerView = findViewById(R.id.listEvent);

        //Creation d'un adapteur et liaison avec le recyclerview_item
        adapter = new EventListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SelectionTracker tracker = new SelectionTracker.Builder<>(
            "my-selection-id",
            recyclerView,
            new StableIdKeyProvider(recyclerView),
            new EventListAdapter.EventDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage())
            .withSelectionPredicate(SelectionPredicates.<Long>createSelectSingleAnything())
            .withOnItemActivatedListener(this)
            .build();


    }

    @Override
    public boolean onItemActivated(@NonNull ItemDetailsLookup.ItemDetails<Long> item, @NonNull MotionEvent e) {

        Uri selectedEvent = Uri.withAppendedPath(CalendarContract.Events.CONTENT_URI, item.getSelectionKey().toString());
        getIntent().setData(selectedEvent);
        this.setResult(Activity.RESULT_OK, getIntent());
        this.finish();
        return true;
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        checkCalendarReadPermission();
        if (readCalendarAuthorized) {
            adapter.setDate(year, month, dayOfMonth);
        }
    }

    /**
     * Check the Calender Permission
     */
    public void checkCalendarReadPermission() {
        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_CALENDAR)
            != PackageManager.PERMISSION_GRANTED) {

            // La permission est refusé
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CALENDAR)) {
            } else {
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR},
                    MY_PERMISSIONS_REQUEST_READ_CALENDARS);
            }
        } else {
            // La permission à déjà été accordé
            readCalendarAuthorized=true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CALENDARS: {
                readCalendarAuthorized= grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                return;
            }
        }
    }
}
