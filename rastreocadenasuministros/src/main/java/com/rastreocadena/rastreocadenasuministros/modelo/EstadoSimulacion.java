package com.rastreocadena.rastreocadenasuministros.modelo;

import java.util.Map;

public class EstadoSimulacion {
    private boolean enEjecucion;
    private Map<String, Object> abastecimientoCondiciones;
    private Map<String, Object> abastecimiento2Condiciones;
    private Map<String, Object> produccionCondiciones;
    private Map<String, Object> almacenamientoCondiciones;

    private int unidadesAbastecimientoAba;
    private int unidadesAbastecimientoAba2;
    private int unidadesAbastecimientoProduccion;
    private int unidadesProductosProd;
    private int unidadesProductosAlma;
    private int unidadesEnTransicion;
    private int unidadesEnTransicion2;
    private int productosEnTransicion;
    private int unidadesAbastecimientoGeneradas;
    private int productosGenerados;
    private int productosVendidos;
    private int unidadesAbastecimientoDesechadas;
    private int productosDesechados;
    private String alertaAbastecimiento;
    private String alertaAbastecimiento2;
    private String alertaProduccion;
    private String alertaAlmacenamiento;

    public EstadoSimulacion() {
    }

    public EstadoSimulacion(boolean enEjecucion) {
        this.enEjecucion = enEjecucion;
    }

    // Getters y Setters
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

    public Map<String, Object> getAbastecimiento2Condiciones() {
        return abastecimiento2Condiciones;
    }

    public void setAbastecimiento2Condiciones(Map<String, Object> abastecimiento2Condiciones) {
        this.abastecimiento2Condiciones = abastecimiento2Condiciones;
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

    public int getUnidadesAbastecimientoAba() {
        return unidadesAbastecimientoAba;
    }

    public void setUnidadesAbastecimientoAba(int unidadesAbastecimientoAba) {
        this.unidadesAbastecimientoAba = unidadesAbastecimientoAba;
    }

    public int getUnidadesAbastecimientoAba2() {
        return unidadesAbastecimientoAba2;
    }

    public void setUnidadesAbastecimientoAba2(int unidadesAbastecimientoAba2) {
        this.unidadesAbastecimientoAba2 = unidadesAbastecimientoAba2;
    }

    public int getUnidadesAbastecimientoProduccion() {
        return unidadesAbastecimientoProduccion;
    }

    public void setUnidadesAbastecimientoProduccion(int unidadesAbastecimientoProd) {
        this.unidadesAbastecimientoProduccion = unidadesAbastecimientoProd;
    }

    public int getUnidadesProductosProd() {
        return unidadesProductosProd;
    }

    public void setUnidadesProductosProd(int unidadesProductosProd) {
        this.unidadesProductosProd = unidadesProductosProd;
    }

    public int getUnidadesProductosAlma() {
        return unidadesProductosAlma;
    }

    public void setUnidadesProductosAlma(int unidadesProductosAlma) {
        this.unidadesProductosAlma = unidadesProductosAlma;
    }

    public int getUnidadesEnTransicion() {
        return unidadesEnTransicion;
    }

    public void setUnidadesEnTransicion(int unidadesEnTransicion) {
        this.unidadesEnTransicion = unidadesEnTransicion;
    }

    public int getUnidadesEnTransicion2() {
        return unidadesEnTransicion2;
    }

    public void setUnidadesEnTransicion2(int unidadesEnTransicion2) {
        this.unidadesEnTransicion2 = unidadesEnTransicion2;
    }

    public int getProductosEnTransicion() {
        return productosEnTransicion;
    }

    public void setProductosEnTransicion(int productosEnTransicion) {
        this.productosEnTransicion = productosEnTransicion;
    }

    public String getAlertaAbastecimiento() {
        return alertaAbastecimiento;
    }

    public void setAlertaAbastecimiento(String alertaAbastecimiento) {
        this.alertaAbastecimiento = alertaAbastecimiento;
    }

    public String getAlertaAbastecimiento2() {
        return alertaAbastecimiento2;
    }

    public void setAlertaAbastecimiento2(String alertaAbastecimiento2) {
        this.alertaAbastecimiento2 = alertaAbastecimiento2;
    }

    public String getAlertaProduccion() {
        return alertaProduccion;
    }

    public void setAlertaProduccion(String alertaProduccion) {
        this.alertaProduccion = alertaProduccion;
    }

    public String getAlertaAlmacenamiento() {
        return alertaAlmacenamiento;
    }

    public void setAlertaAlmacenamiento(String alertaAlmacenamiento) {
        this.alertaAlmacenamiento = alertaAlmacenamiento;
    }

    public void incrementarUnidadesAbastecimientoGeneradas() {
        this.unidadesAbastecimientoGeneradas++;
    }

    public int getUnidadesAbastecimientoGeneradas() {
        return this.unidadesAbastecimientoGeneradas;
    }

    public void incrementarProductosGenerados() {
        this.productosGenerados++;
    }

    public int getProductosGenerados() {
        return this.productosGenerados;
    }

    public void incrementarProductosVendidos() {
        this.productosVendidos++;
    }

    public int getProductosVendidos() {
        return this.productosVendidos;
    }

    public void incrementarUnidadesAbastecimientoDesechadas() {
        this.unidadesAbastecimientoDesechadas++;
    }

    public void incrementarUnidadesAbastecimientoDesechadas(int cantidad) {
        this.unidadesAbastecimientoDesechadas += cantidad;
    }

    public int getUnidadesAbastecimientoDesechadas() {
        return this.unidadesAbastecimientoDesechadas;
    }

    public void incrementarProductosDesechados() {
        this.productosDesechados++;
    }

    public void incrementarProductosDesechados(int cantidad) {
        this.productosDesechados += cantidad;
    }

    public int getProductosDesechados() {
        return this.productosDesechados;
    }

    // Abastecimiento
    public void incrementarUnidadesDeAbastecimientoEnAbastecimiento() {
        this.unidadesAbastecimientoAba++;
    }

    public void expedirUnidadesAbastecimiento() {
        this.unidadesAbastecimientoAba = 0;
    }

    public void incrementarUnidadesDeAbastecimientoEnAbastecimiento2() {
        this.unidadesAbastecimientoAba2++;
    }

    public void expedirUnidadesAbastecimiento2() {
        this.unidadesAbastecimientoAba2 = 0;
    }

    // Transición de Abastecimiento a Producción
    public void incrementarUnidadesDeAbastecimientoEnTransicion(int cantidad) {
        this.unidadesEnTransicion += cantidad;
    }

    public void expedirUnidadesTransicionAbastecimiento() {
        this.unidadesEnTransicion = 0;
    }

    public void incrementarUnidadesDeAbastecimientoEnTransicion2(int cantidad) {
        this.unidadesEnTransicion2 += cantidad;
    }

    public void expedirUnidadesTransicionAbastecimiento2() {
        this.unidadesEnTransicion2 = 0;
    }

    public void incrementarUnidadesDeAbastecimientoEnProduccion(int cantidad) {
        this.unidadesAbastecimientoProduccion += cantidad;
    }

    public void producirProductos(int cantidad) {
        if (this.unidadesAbastecimientoProduccion >= cantidad) {
            this.unidadesAbastecimientoProduccion -= cantidad;
            this.unidadesProductosProd += 1;
        }
    }

    public void expedirProductosDeProduccion() {
        this.unidadesProductosProd = 0;
    }

    public void incrementarProductosEnTransicion(int cantidad) {
        this.productosEnTransicion += cantidad;
    }

    public void expedirProductosTransicionProduccion() {
        this.productosEnTransicion = 0;
    }
    public void transferirProductosAAlmacenamiento(int cantidad) {
        this.unidadesProductosAlma += cantidad;
    }

    public void venderProducto() {
        if (this.unidadesProductosAlma > 0) {
            this.unidadesProductosAlma--;
        }
    }

    public void setUnidadesAbastecimientoGeneradas(int unidadesAbastecimientoGeneradas) {
        this.unidadesAbastecimientoGeneradas = unidadesAbastecimientoGeneradas;
    }

    public void setProductosGenerados(int productosGenerados) {
        this.productosGenerados = productosGenerados;
    }

    public void setProductosVendidos(int productosVendidos) {
        this.productosVendidos = productosVendidos;
    }

    public void setUnidadesAbastecimientoDesechadas(int unidadesAbastecimientoDesechadas) {
        this.unidadesAbastecimientoDesechadas = unidadesAbastecimientoDesechadas;
    }

    public void setProductosDesechados(int productosDesechados) {
        this.productosDesechados = productosDesechados;
    }

}
