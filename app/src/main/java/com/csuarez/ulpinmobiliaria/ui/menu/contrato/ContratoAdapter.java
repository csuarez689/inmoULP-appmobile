package com.csuarez.ulpinmobiliaria.ui.menu.contrato;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.csuarez.ulpinmobiliaria.R;
import com.csuarez.ulpinmobiliaria.models.Inmueble;

import java.util.List;

public class ContratoAdapter extends RecyclerView.Adapter<ContratoAdapter.ContratoViewHolder> {

    private List<Inmueble> inmueblesConContrato;
    private OnContratoClickListener listener;

    public interface OnContratoClickListener {
        void onVerInquilinoClick(Inmueble inmueble);
        void onVerContratoClick(Inmueble inmueble);
    }

    public ContratoAdapter(List<Inmueble> inmueblesConContrato, OnContratoClickListener listener) {
        this.inmueblesConContrato = inmueblesConContrato;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContratoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contrato, parent, false);
        return new ContratoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContratoViewHolder holder, int position) {
        Inmueble inmueble = inmueblesConContrato.get(position);
        holder.bind(inmueble, listener);
    }

    @Override
    public int getItemCount() {
        return inmueblesConContrato != null ? inmueblesConContrato.size() : 0;
    }

    public void actualizarLista(List<Inmueble> nuevaLista) {
        this.inmueblesConContrato = nuevaLista;
        notifyDataSetChanged();
    }

    static class ContratoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivInmueble;
        private TextView tvDireccion;
        private Button btnVerInquilino;
        private Button btnVerContrato;

        public ContratoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivInmueble = itemView.findViewById(R.id.ivInmuebleInquilino);
            tvDireccion = itemView.findViewById(R.id.tvDireccionInquilino);
            btnVerInquilino = itemView.findViewById(R.id.btnVerInquilino);
            btnVerContrato = itemView.findViewById(R.id.btnVerContrato);
        }

        public void bind(Inmueble inmueble, OnContratoClickListener listener) {
            tvDireccion.setText(inmueble.getDireccion());

            // cargar imagen con glide
            if (inmueble.getImagenUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(inmueble.getImagenUrl())
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(ivInmueble);
            }

            btnVerInquilino.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onVerInquilinoClick(inmueble);
                    }
                }
            });

            btnVerContrato.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onVerContratoClick(inmueble);
                    }
                }
            });
        }
    }
}