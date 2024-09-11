package com.rastreocadena.rastreocadenasuministros.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Almacenamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int capacidadMaximaProductosAlmacenamiento;
    private int periodoCompras;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCapacidadMaximaProductosAlmacenamiento() {
        return capacidadMaximaProductosAlmacenamiento;
    }

    public void setCapacidadMaximaProductosAlmacenamiento(int capacidadMaximaProductosAlmacenamiento) {
        this.capacidadMaximaProductosAlmacenamiento = capacidadMaximaProductosAlmacenamiento;
    }

    public int getPeriodoCompras() {
        return periodoCompras;
    }

    public void setPeriodoCompras(int periodoCompras) {
        this.periodoCompras = periodoCompras;
    }
}
