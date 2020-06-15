package com.miker.login.Model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.miker.login.Logic.Cancion;
import com.miker.login.R;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luis Carrillo Rodriguez on 18/4/2018.
 */
public class CancionesAdapter extends RecyclerView.Adapter<CancionesAdapter.MyViewHolder> implements Filterable {

    private List<Cancion> cancionList;
    private List<Cancion> cancionListFiltered;
    private CancionesAdapter.CancionAdapterListener listener;
    private Cancion object;
    Context prueba;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre;
        public ImageView background;

        public MyViewHolder(View view) {
            super(view);
            nombre = (TextView) view.findViewById(R.id.nombre);
            background = view.findViewById(R.id.background);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onSelected(cancionListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public CancionesAdapter(List<Cancion> cancionList, CancionesAdapter.CancionAdapterListener listener, Context prueba) {
        this.cancionList = cancionList;
        this.listener = listener;
        this.cancionListFiltered = cancionList;
        this.prueba = prueba;
    }

    @Override
    public CancionesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cancion_card, parent, false);

        return new CancionesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CancionesAdapter.MyViewHolder holder, final int position) {
        final Cancion cancion = cancionListFiltered.get(position);
        holder.nombre.setText(cancion.getNombre());

        String ruta;
        ruta = "/"+ cancion.getNombre();
        try{
            FileInputStream  fileInputStream =  new FileInputStream(prueba.getFilesDir().getPath()+ ruta);
            holder.background.setImageBitmap(BitmapFactory.decodeStream(fileInputStream));
        }catch (IOException io){
            io.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return cancionListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    cancionListFiltered = cancionList;
                } else {
                    List<Cancion> filteredList = new ArrayList<>();
                    for (Cancion row : cancionList) {
                        // filter use two parameters
                        if (row.getNombre().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    cancionListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = cancionListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cancionListFiltered = (ArrayList<Cancion>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface CancionAdapterListener {
        void onSelected(Cancion cancion);
    }
}

