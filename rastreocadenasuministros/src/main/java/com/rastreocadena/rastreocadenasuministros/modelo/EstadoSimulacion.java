package com.rastreocadena.rastreocadenasuministros.modelo;

import java.util.Map;

public class EstadoSimulacion {
    private boolean enEjecucion;
    private Map<String, Object> abastecimientoCondiciones;
    private Map<String, Object> produccionCondiciones;
    private Map<String, Object> almacenamientoCondiciones;
    private int unidadesAbastecimiento = 0;
    private String alerta;

    public EstadoSimulacion(boolean enEjecucion) {
        this.enEjecucion = enEjecucion;
    }

    public boolean isEnEjecucion() {
        return enEjecucion;
    }

    public void setEnEjecucion(boolean enEjecucion) {
        this.enEjecucion = enEjecucion;
    }

    public Map<String, Object> getAbastecimientoCondiciones() {
        return abastecimientoCondiciones;
    }

    public void setAbastecimientoCondiciones(Map<String, Object> abastecimientoCondiciones) {
        this.abastecimientoCondiciones = abastecimientoCondiciones;
    }

    public Map<String, Object> getProduccionCondiciones() {
        return produccionCondiciones;
    }

    public void setProduccionCondiciones(Map<String, Object> produccionCondiciones) {
        this.produccionCondiciones = produccionCondiciones;
    }

    public Map<String, Object> getAlmacenamientoCondiciones() {
        return almacenamientoCondiciones;
    }

    public void setAlmacenamientoCondiciones(Map<String, Object> almacenamientoCondiciones) {
        this.almacenamientoCondiciones = almacenamientoCondiciones;
    }

    public int getUnidadesAbastecimiento() {
        return unidadesAbastecimiento;
    }

    public void incrementarUnidadesAbastecimiento() {
        this.unidadesAbastecimiento++;
    }

    public void expedirUnidadesAbastecimiento() {
        this.unidadesAbastecimiento = 0; // Resetear a 0 para simplificar la lógica inicial
    }

    public String getAlerta() {
        return alerta;
    }

    public void setAlerta(String alerta) {
        this.alerta = alerta;
    }
}
