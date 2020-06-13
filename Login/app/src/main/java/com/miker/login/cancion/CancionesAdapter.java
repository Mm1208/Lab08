package com.miker.login.cancion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.miker.login.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Luis Carrillo Rodriguez on 18/4/2018.
 */
public class CancionesAdapter extends RecyclerView.Adapter<CancionesAdapter.MyViewHolder> implements Filterable {

    private List<Cancion> carreraList;
    private List<Cancion> carreraListFiltered;
    private CancionesAdapter.CancionAdapterListener listener;
    private Cancion object;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre;
        public RelativeLayout viewForeground, viewBackgroundDelete, viewBackgroundEdit;

        public MyViewHolder(View view) {
            super(view);
            nombre = (TextView) view.findViewById(R.id.nombre);
            viewForeground = view.findViewById(R.id.view_foreground);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onSelected(carreraListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public CancionesAdapter(List<Cancion> carreraList, CancionesAdapter.CancionAdapterListener listener) {
        this.carreraList = carreraList;
        this.listener = listener;
        this.carreraListFiltered = carreraList;
    }

    @Override
    public CancionesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cancion_card, parent, false);

        return new CancionesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CancionesAdapter.MyViewHolder holder, final int position) {
        final Cancion cancion = carreraListFiltered.get(position);
        holder.nombre.setText(cancion.getNombre());
    }

    @Override
    public int getItemCount() {
        return carreraListFiltered.size();
    }

    public void removeItem(int position) {
        object = carreraListFiltered.remove(position);
        Iterator<Cancion> iter = carreraList.iterator();
        while (iter.hasNext()) {
            Cancion aux = iter.next();
            if (object.equals(aux))
                iter.remove();
        }
        // notify item removed
        notifyItemRemoved(position);
    }

    public void restoreItem(int position) {

        if (carreraListFiltered.size() == carreraList.size()) {
            carreraListFiltered.add(position, object);
        } else {
            carreraListFiltered.add(position, object);
            carreraList.add(object);
        }
        notifyDataSetChanged();
        // notify item added by position
        notifyItemInserted(position);
    }

    public Cancion getSwipedItem(int index) {
        if (this.carreraList.size() == this.carreraListFiltered.size()) { //not filtered yet
            return carreraList.get(index);
        } else {
            return carreraListFiltered.get(index);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (carreraList.size() == carreraListFiltered.size()) { // without filter
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(carreraList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(carreraList, i, i - 1);
                }
            }
        } else {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(carreraListFiltered, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(carreraListFiltered, i, i - 1);
                }
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    carreraListFiltered = carreraList;
                } else {
                    List<Cancion> filteredList = new ArrayList<>();
                    for (Cancion row : carreraList) {
                        // filter use two parameters
                        if (row.getNombre().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    carreraListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = carreraListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                carreraListFiltered = (ArrayList<Cancion>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface CancionAdapterListener {
        void onSelected(Cancion cancion);
    }
}

