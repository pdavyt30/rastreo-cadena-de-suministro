package com.rastreocadena.rastreocadenasuministros.modelo;

import jakarta.persistence.Entity;

@Entity
public class Almacenamiento extends Etapa {
    private int capacidad;
    private int periodoCompras;

    // Getters y Setters

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getPeriodoCompras() {
        return periodoCompras;
    }

    public void setPeriodoCompras(int periodoCompras) {
        this.periodoCompras = periodoCompras;
    }
}
