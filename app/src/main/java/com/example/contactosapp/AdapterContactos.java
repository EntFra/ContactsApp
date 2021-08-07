package com.example.contactosapp;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactosapp.utilidades.Utilidades;

import java.util.ArrayList;
import java.util.List;

public class AdapterContactos extends RecyclerView.Adapter<AdapterContactos.ViewHolderContactos> implements Filterable {

    ArrayList<Contacto> listaContactos = new ArrayList<>();
    //Lista para el searchview
    ArrayList<Contacto> listaSearch;
    private OnContactoListener mOnContactoListener;
    private final Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contacto> listaFiltrada = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                listaFiltrada.addAll(listaSearch);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Contacto item : listaSearch) {
                    if (item.getNombre().toLowerCase().contains(filterPattern)) {
                        listaFiltrada.add(item);
                    }
                }
            }

            FilterResults resultado = new FilterResults();
            resultado.values = listaFiltrada;

            return resultado;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listaContactos.clear();
            listaContactos.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public AdapterContactos(ArrayList<Contacto> listaContactos, OnContactoListener onContactoListener) {
        this.listaContactos = listaContactos;
        this.mOnContactoListener = onContactoListener;
        listaSearch = new ArrayList<>(listaContactos);
    }

    @NonNull
    @Override
    public ViewHolderContactos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_contacto, null, false);
        return new ViewHolderContactos(view, mOnContactoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderContactos holder, int position) {
        holder.nombre.setText(listaContactos.get(position).getNombre());
        holder.telefono.setText(listaContactos.get(position).getTelefono());

        byte[] imagen = listaContactos.get(position).getFoto();
        Bitmap bitmap = Utilidades.getImage(imagen);

        holder.foto.setImageBitmap(bitmap);


    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    //Maneja el click sobre un objeto
    public interface OnContactoListener {
        void onContactoClick(int position);

    }

    public class ViewHolderContactos extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nombre, telefono;
        ImageView foto;
        OnContactoListener onContactoListener;

        public ViewHolderContactos(@NonNull View itemView, OnContactoListener onContactoListener) {
            super(itemView);
            foto = itemView.findViewById(R.id.imagenCard);
            nombre = itemView.findViewById(R.id.txtNombre);
            telefono = itemView.findViewById(R.id.txtTelefono);
            this.onContactoListener = onContactoListener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onContactoListener.onContactoClick((getAdapterPosition()));
        }
    }
}
