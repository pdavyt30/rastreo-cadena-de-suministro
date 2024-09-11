package com.rastreocadena.rastreocadenasuministros.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Produccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int unidadesPorProducto;
    private int capacidadMaximaAbastecimientoProduccion;
    private int capacidadMaximaProductosProduccion;
    private int tiempoFabricacionProducto;
    private int periodoExpedicionProduccion;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUnidadesPorProducto() {
        return unidadesPorProducto;
    }

    public void setUnidadesPorProducto(int unidadesPorProducto) {
        this.unidadesPorProducto = unidadesPorProducto;
    }

    public int getCapacidadMaximaAbastecimientoProduccion() {
        return capacidadMaximaAbastecimientoProduccion;
    }

    public void setCapacidadMaximaAbastecimientoProduccion(int capacidadMaximaAbastecimientoProduccion) {
        this.capacidadMaximaAbastecimientoProduccion = capacidadMaximaAbastecimientoProduccion;
    }

    public int getCapacidadMaximaProductosProduccion() {
        return capacidadMaximaProductosProduccion;
    }

    public void setCapacidadMaximaProductosProduccion(int capacidadMaximaProductosProduccion) {
        this.capacidadMaximaProductosProduccion = capacidadMaximaProductosProduccion;
    }

    public int getTiempoFabricacionProducto() {
        return tiempoFabricacionProducto;
    }

    public void setTiempoFabricacionProducto(int tiempoFabricacionProducto) {
        this.tiempoFabricacionProducto = tiempoFabricacionProducto;
    }

    public int getPeriodoExpedicionProduccion() {
        return periodoExpedicionProduccion;
    }

    public void setPeriodoExpedicionProduccion(int periodoExpedicionProduccion) {
        this.periodoExpedicionProduccion = periodoExpedicionProduccion;
    }
}
