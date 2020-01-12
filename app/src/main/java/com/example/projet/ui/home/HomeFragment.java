package com.example.projet.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet.R;
import com.example.projet.database.AnnotationDatabase;
import com.example.projet.database.PicAnnotationDao;
import com.example.projet.model.PicAnnotation;
import com.example.projet.ui.HomePicturesPreviewsAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView homePicturesPreviews;
    private HomePicturesPreviewsAdapter homePicturesPreviewsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.home, container, false);

        homePicturesPreviews = root.findViewById(R.id.homePicturesPreviews);
        homePicturesPreviews.setLayoutManager(new GridLayoutManager(getContext(), 3));
        AnnotationDatabase.databaseWriteExecutor.execute(() -> {

            AnnotationDatabase db = AnnotationDatabase.getDatabase(getActivity().getApplication());
            PicAnnotationDao dao = db.getPicAnnotationDao();


            List<PicAnnotation> res = dao.loadAnnotations();
            final ArrayList<PicAnnotation> list = new ArrayList<PicAnnotation>();
            for (PicAnnotation a : res) {
                list.add(a);
            }

            homePicturesPreviewsAdapter = new HomePicturesPreviewsAdapter(list, getContext());
            homePicturesPreviews.setAdapter(homePicturesPreviewsAdapter);


        });


        return root;
    }
}
