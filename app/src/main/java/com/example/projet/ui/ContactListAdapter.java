package com.example.projet.ui;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.projet.R;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private List<RemoveContactListener> removeContactListeners;


    /**
     * Crée un list adapter avec une liste
     * @param list
     * @param context
     */
    public ContactListAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }


    /**
     * Renvoie la taille de la liste
     * @return
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Renvoie un item à une certaine position
     * @param pos
     * @return
     */
    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    /**
     * Renvoie l'id d'un item (non concerné)
     * @param pos
     * @return
     */
    @Override
    public long getItemId(int pos) {
        return 0;
    }

    /**
     * Gère la vue
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //Crée la vue
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.selected_contact_item, null);
        }

        //Fixe les noms des contacts
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        //Gère les évènements à la suppression
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Supprime graphiquement
                list.remove(position);
                //Rafraichit la liste
                notifyDataSetChanged();
                //Indique qu'on a supprimé un contact
                notifyDeleted(position);
            }
        });
        return view;
    }

    /**
     * Ajoute les listeners de suppression de contact à la liste
     * @param listener
     */
    public void addRemoveContactListener(RemoveContactListener listener) {
        if (removeContactListeners==null) removeContactListeners=new ArrayList<>();
        removeContactListeners.add(listener);
    }

    /**
     * Indique qu'on a supprimé un contact
     * @param pos
     */
    protected void notifyDeleted(int pos) {
        for(int i = 0; i < removeContactListeners.size(); i++){
            removeContactListeners.get(i).deleteContact(pos);
        }
    }

    /**
     * Fixe une nouvelle liste
     * @param list
     */
    public void setList(ArrayList<String> list) {
        this.list = list;
    }


}