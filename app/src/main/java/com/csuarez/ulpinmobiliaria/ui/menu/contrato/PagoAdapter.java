package com.csuarez.ulpinmobiliaria.ui.menu.contrato;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csuarez.ulpinmobiliaria.R;
import com.csuarez.ulpinmobiliaria.models.Pago;
import com.csuarez.ulpinmobiliaria.utils.FormatUtils;

import java.util.List;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.PagoViewHolder> {

    private List<Pago> pagos;

    public PagoAdapter(List<Pago> pagos) {
        this.pagos = pagos;
    }

    @NonNull
    @Override
    public PagoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pago, parent, false);
        return new PagoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PagoViewHolder holder, int position) {
        Pago pago = pagos.get(position);
        // Como la lista está invertida, contar desde el total hacia atrás
        int numeroPago = pagos.size() - position;
        holder.bind(pago, numeroPago);
    }

    @Override
    public int getItemCount() {
        return pagos != null ? pagos.size() : 0;
    }

    public void actualizarLista(List<Pago> nuevaLista) {
        this.pagos = nuevaLista;
        notifyDataSetChanged();
    }

    static class PagoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNumeroPago;
        private TextView tvFechaPago;
        private TextView tvMontoPago;
        private TextView tvDetallePago;

        public PagoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumeroPago = itemView.findViewById(R.id.tvNumeroPago);
            tvFechaPago = itemView.findViewById(R.id.tvFechaPago);
            tvMontoPago = itemView.findViewById(R.id.tvMontoPago);
            tvDetallePago = itemView.findViewById(R.id.tvDetallePago);
        }

        public void bind(Pago pago, int numero) {
            tvNumeroPago.setText("Pago #" + numero);
            tvFechaPago.setText("Fecha: " + FormatUtils.formatearFecha(pago.getFechaPago()));
            tvMontoPago.setText(FormatUtils.formatearMontoConPrefijo(pago.getMonto(), "Monto: "));
            tvDetallePago.setText("Detalle: " + (pago.getDetalle() != null ? pago.getDetalle() : "Sin detalle"));
        }
    }
}