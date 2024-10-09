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

    // Campos para Abastecimiento 2
    private int capacidadMaximaAbastecimiento2;
    private int periodoExpedicionAbastecimiento2;
    private int tiempoProduccionAbastecimiento2;

    private int tipoAbastecimiento; // 1 para Abastecimiento 1 y 2 para Abastecimiento 2

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

    // Getters y Setters para Abastecimiento 2
    public int getCapacidadMaximaAbastecimiento2() {
        return capacidadMaximaAbastecimiento2;
    }

    public void setCapacidadMaximaAbastecimiento2(int capacidadMaximaAbastecimiento2) {
        this.capacidadMaximaAbastecimiento2 = capacidadMaximaAbastecimiento2;
    }

    public int getPeriodoExpedicionAbastecimiento2() {
        return periodoExpedicionAbastecimiento2;
    }

    public void setPeriodoExpedicionAbastecimiento2(int periodoExpedicionAbastecimiento2) {
        this.periodoExpedicionAbastecimiento2 = periodoExpedicionAbastecimiento2;
    }

    public int getTiempoProduccionAbastecimiento2() {
        return tiempoProduccionAbastecimiento2;
    }

    public void setTiempoProduccionAbastecimiento2(int tiempoProduccionAbastecimiento2) {
        this.tiempoProduccionAbastecimiento2 = tiempoProduccionAbastecimiento2;
    }

    public int getTipoAbastecimiento() {
        return tipoAbastecimiento;
    }

    public void setTipoAbastecimiento(int tipoAbastecimiento) {
        this.tipoAbastecimiento = tipoAbastecimiento;
    }
}
