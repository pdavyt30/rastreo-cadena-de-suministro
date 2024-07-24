package com.rastreocadena.rastreocadenasuministros.modelo;

import jakarta.persistence.Entity;

@Entity
public class Produccion extends Etapa {
    private int unidadesPorProducto;
    private int capacidad;
    private int periodoExpedicion;

    // Getters y Setters

    public int getUnidadesPorProducto() {
        return unidadesPorProducto;
    }

    public void setUnidadesPorProducto(int unidadesPorProducto) {
        this.unidadesPorProducto = unidadesPorProducto;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getPeriodoExpedicion() {
        return periodoExpedicion;
    }

    public void setPeriodoExpedicion(int periodoExpedicion) {
        this.periodoExpedicion = periodoExpedicion;
    }
}
