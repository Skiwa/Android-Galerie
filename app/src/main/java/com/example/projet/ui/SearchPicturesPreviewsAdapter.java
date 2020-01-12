package com.example.projet.ui;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.projet.R;
import com.example.projet.model.PicAnnotation;

import java.util.ArrayList;
import java.util.List;

public class SearchPicturesPreviewsAdapter extends RecyclerView.Adapter<SearchPicturesPreviewsAdapter.SearchPicturesPreviewsViewHolder>{

    private LayoutInflater inflater;
    private Context context;
    private List<Uri> list = new ArrayList<Uri>();

    public SearchPicturesPreviewsAdapter(List list, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.list=list;
    }

    @Override
    public SearchPicturesPreviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.picture_preview, parent, false);
        SearchPicturesPreviewsViewHolder holder = new SearchPicturesPreviewsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchPicturesPreviewsViewHolder holder, int position) {
        holder.preview.setImageURI(list.get(position));
        holder.preview.requestLayout();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SearchPicturesPreviewsViewHolder extends RecyclerView.ViewHolder
    {
        ImageView preview;
        TextView text;

        public SearchPicturesPreviewsViewHolder(View itemView) {
            super(itemView);
            preview = itemView.findViewById(R.id.picturePreviewImage);
        }
    }

}
