package com.csuarez.ulpinmobiliaria.ui.menu.inmueble;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.csuarez.ulpinmobiliaria.R;
import com.csuarez.ulpinmobiliaria.models.Inmueble;
import com.csuarez.ulpinmobiliaria.network.ApiClient;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class InmueblesAdapter extends RecyclerView.Adapter<InmueblesAdapter.InmuebleViewHolder> {

    private List<Inmueble> listaInmuebles;
    private LayoutInflater layoutInflater;
    private Context context;

    public InmueblesAdapter(List<Inmueble> listaInmuebles, LayoutInflater layoutInflater, Context context) {
        this.listaInmuebles = listaInmuebles;
        this.layoutInflater = layoutInflater;
        this.context = context;
    }

    @NonNull
    @Override
    public InmuebleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_inmueble, parent, false);
        return new InmuebleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InmuebleViewHolder holder, int position) {
        Inmueble inmueble = listaInmuebles.get(position);

        holder.tvDireccion.setText(inmueble.getDireccion());
        holder.tvTipoUso.setText(inmueble.getTipo() + " - " + inmueble.getUso());
        holder.tvDetalles.setText(inmueble.getAmbientes() + " amb. - " + inmueble.getSuperficie() + " m²");

        NumberFormat formatoPrecio = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        holder.tvPrecio.setText(formatoPrecio.format(inmueble.getValor()));

        // Cargar imagen con Glide
        if (inmueble.getImagen() != null && !inmueble.getImagen().isEmpty()) {
            String imageUrl = inmueble.getImagen().replace("\\", "/");
            String fullUrl = ApiClient.BASE_URL + imageUrl;

            Glide.with(context)
                    .load(fullUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.ivInmueble);
        } else {
            holder.ivInmueble.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return listaInmuebles != null ? listaInmuebles.size() : 0;
    }

    static class InmuebleViewHolder extends RecyclerView.ViewHolder {
        ImageView ivInmueble;
        TextView tvDireccion;
        TextView tvTipoUso;
        TextView tvDetalles;
        TextView tvPrecio;

        public InmuebleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivInmueble = itemView.findViewById(R.id.ivInmueble);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvTipoUso = itemView.findViewById(R.id.tvTipoUso);
            tvDetalles = itemView.findViewById(R.id.tvDetalles);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
        }
    }
}