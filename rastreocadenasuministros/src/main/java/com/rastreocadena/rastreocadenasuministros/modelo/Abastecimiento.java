package com.rastreocadena.rastreocadenasuministros.modelo;

import jakarta.persistence.Entity;

@Entity
public class Abastecimiento extends Etapa {
    private int tiempoProduccion;
    private int capacidad;
    private int periodoExpedicion;

    // Getters y Setters

    public int getTiempoProduccion() {
        return tiempoProduccion;
    }

    public void setTiempoProduccion(int tiempoProduccion) {
        this.tiempoProduccion = tiempoProduccion;
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
