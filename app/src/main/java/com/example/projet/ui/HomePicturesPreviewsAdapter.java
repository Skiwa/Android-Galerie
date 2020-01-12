package com.example.projet.ui;

import android.app.Activity;
import android.content.Context;
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

public class HomePicturesPreviewsAdapter extends RecyclerView.Adapter<HomePicturesPreviewsAdapter.HomePicturesPreviewsViewHolder>{

    private LayoutInflater inflater;
    private Context context;
    private List<PicAnnotation> list = new ArrayList<PicAnnotation>();

    public HomePicturesPreviewsAdapter(List list,Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.list=list;
    }

    @Override
    public HomePicturesPreviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.picture_preview, parent, false);
        HomePicturesPreviewsViewHolder holder = new HomePicturesPreviewsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HomePicturesPreviewsViewHolder holder, int position) {
        holder.preview.setImageURI(list.get(position).getPicUri());
        holder.preview.requestLayout();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HomePicturesPreviewsViewHolder extends RecyclerView.ViewHolder
    {
        ImageView preview;
        TextView text;

        public HomePicturesPreviewsViewHolder(View itemView) {
            super(itemView);
            preview = itemView.findViewById(R.id.picturePreviewImage);
        }
    }

}
