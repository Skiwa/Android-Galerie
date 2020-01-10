package com.example.projet.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.projet.R;
import com.example.projet.database.AnnotationDatabase;
import com.example.projet.database.PicAnnotationDao;
import com.example.projet.model.PicAnnotation;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ListView ListViewDB;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.home, container, false);

        ListViewDB = root.findViewById(R.id.ListViewDB);

        AnnotationDatabase.databaseWriteExecutor.execute(() -> {

            AnnotationDatabase db = AnnotationDatabase.getDatabase(getActivity().getApplication());
            PicAnnotationDao dao = db.getPicAnnotationDao();


            List<PicAnnotation> res = dao.loadAnnotations();
            final ArrayList<String> list = new ArrayList<String>();
            for (PicAnnotation a : res) {
                list.add(a.toString());
            }

            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity().getApplication(),
                android.R.layout.simple_list_item_1,
                list);
            ListViewDB.setAdapter(adapter);

        });


        return root;
    }
}
