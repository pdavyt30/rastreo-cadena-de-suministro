package com.rastreocadena.rastreocadenasuministros.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Abastecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int capacidadMaximaAbastecimiento;
    private int periodoExpedicionAbastecimiento;
    private int tiempoProduccionAbastecimiento;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCapacidadMaximaAbastecimiento() {
        return capacidadMaximaAbastecimiento;
    }

    public void setCapacidadMaximaAbastecimiento(int capacidadMaximaAbastecimiento) {
        this.capacidadMaximaAbastecimiento = capacidadMaximaAbastecimiento;
    }

    public int getPeriodoExpedicionAbastecimiento() {
        return periodoExpedicionAbastecimiento;
    }

    public void setPeriodoExpedicionAbastecimiento(int periodoExpedicionAbastecimiento) {
        this.periodoExpedicionAbastecimiento = periodoExpedicionAbastecimiento;
    }

    public int getTiempoProduccionAbastecimiento() {
        return tiempoProduccionAbastecimiento;
    }

    public void setTiempoProduccionAbastecimiento(int tiempoProduccionAbastecimiento) {
        this.tiempoProduccionAbastecimiento = tiempoProduccionAbastecimiento;
    }
}
