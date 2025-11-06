package com.csuarez.ulpinmobiliaria.models;

import java.io.Serializable;

public class Pago implements Serializable {
    private int idPago;
    private int idContrato;
    private String fechaPago;
    private String detalle;
    private double monto;
    private boolean estado;
    private Contrato contrato;

    public Pago() {}

    public Pago(int idPago, int idContrato, String fechaPago, String detalle,
                double monto, boolean estado, Contrato contrato) {
        this.idPago = idPago;
        this.idContrato = idContrato;
        this.fechaPago = fechaPago;
        this.detalle = detalle;
        this.monto = monto;
        this.estado = estado;
        this.contrato = contrato;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
}