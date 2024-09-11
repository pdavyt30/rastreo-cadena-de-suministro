package com.rastreocadena.rastreocadenasuministros.modelo;

import java.util.Map;

public class EstadoSimulacion {
    private boolean enEjecucion;
    private Map<String, Object> abastecimientoCondiciones;
    private Map<String, Object> produccionCondiciones;
    private Map<String, Object> almacenamientoCondiciones;
    private int unidadesAbastecimientoAba = 0;
    private int unidadesAbastecimientoProd = 0;
    private int unidadesProductosProd = 0;
    private int unidadesProductosAlma = 0;
    private int unidadesEnTransicion;
    private int productosEnTransicion;
    private String alertaAbastecimiento;
    private String alertaProduccion;
    private String alertaAlmacenamiento;
    private int unidadesAbastecimientoGeneradas;
    private int productosGenerados;

    private int productosVendidos;
    private int unidadesAbastecimientoDesechadas;
    private int productosDesechados;


    // Constructor por defecto
    public EstadoSimulacion() {
    }

    // Constructor con parámetro
    public EstadoSimulacion(boolean enEjecucion) {
        this.enEjecucion = enEjecucion;
    }

    // Getters y Setters

    public void setUnidadesAbastecimientoAba(int unidadesAbastecimientoAba) {
        this.unidadesAbastecimientoAba = unidadesAbastecimientoAba;
    }

    public void setUnidadesAbastecimientoProd(int unidadesAbastecimientoProd) {
        this.unidadesAbastecimientoProd = unidadesAbastecimientoProd;
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

    public int getUnidadesAbastecimientoAba() {
        return unidadesAbastecimientoAba;
    }

    public void setUnidadesAbastecimiento(int unidadesAbastecimientoAba) {
        this.unidadesAbastecimientoAba = unidadesAbastecimientoAba;
    }

    public int getUnidadesAbastecimientoProduccion() {
        return unidadesAbastecimientoProd;
    }

    public void setUnidadesAbastecimientoProduccion(int unidadesAbastecimientoProd) {
        this.unidadesAbastecimientoProd = unidadesAbastecimientoProd;
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

    public String getAlertaAbastecimiento() {
        return alertaAbastecimiento;
    }

    public String getAlertaProduccion() {
        return alertaProduccion;
    }

    public String getAlertaAlmacenamiento() {
        return alertaAlmacenamiento;
    }

    public void setAlertaAlmacenamiento(String alertaAlmacenamiento) {
        this.alertaAlmacenamiento = alertaAlmacenamiento;
    }

    public void setAlertaProduccion(String alertaProduccion) {
        this.alertaProduccion = alertaProduccion;
    }

    public void setAlertaAbastecimiento(String alertaAbastecimiento) {
        this.alertaAbastecimiento = alertaAbastecimiento;
    }

    public int getUnidadesEnTransicion() {
        return unidadesEnTransicion;
    }

    public void setUnidadesEnTransicion(int unidadesEnTransicion) {
        this.unidadesEnTransicion = unidadesEnTransicion;
    }

    public int getProductosEnTransicion() {
        return productosEnTransicion;
    }

    public void setProductosEnTransicion(int productosEnTransicion) {
        this.productosEnTransicion = productosEnTransicion;
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
        this.unidadesAbastecimientoDesechadas+= cantidad;
    }

    public int getUnidadesAbastecimientoDesechadas() {
        return this.unidadesAbastecimientoDesechadas;
    }

    public void incrementarProductosDesechados() {
        this.productosDesechados++;
    }

    public void incrementarProductosDesechados(int cantidad) {
        this.productosDesechados+= cantidad;
    }

    public int getProductosDesechados() {
        return this.productosDesechados;
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


    // Métodos adicionales

    //Abastecimiento

    /**
     * Incrementa (+1) la cantidad de unidades de abastecimiento en la etapa de abastecimiento.
     */
    public void incrementarUnidadesDeAbastecimientoEnAbastecimiento() {
        this.unidadesAbastecimientoAba++;
    }

    /**
     * Este método se utiliza para vaciar el contador de unidades de abastecimiento en la etapa de abastecimiento
     * una vez que las unidades han sido enviadas a la siguiente etapa (Produccion).
     */
    public void expedirUnidadesAbastecimiento() {
        this.unidadesAbastecimientoAba = 0;
    }

    // Transicion de Abastecimiento a Produccion

    /**
     * Incrementa la cantidad de unidades de abastecimiento en la transicion.
     *
     * @param cantidad la cantidad de unidades de abastecimiento (provenientes de la etapa de abastecimiento)
     * que se sumarán a la transicion.
     */
    public void incrementarUnidadesDeAbastecimientoEnTransicion(int cantidad) {
        this.unidadesEnTransicion += cantidad;
    }

    /**
     * Este método se utiliza para vaciar el contador de unidades de abastecimiento en la transicion de abastecimiento a produccion
     * una vez que las unidades han sido enviadas a la siguiente etapa (Produccion).
     */
    public void expedirUnidadesTransicionAbastecimiento() {
        this.unidadesEnTransicion = 0;
    }

    // Produccion:

    /**
     * Incrementa la cantidad de unidades de abastecimiento en la etapa de producción.
     *
     * @param cantidad la cantidad de unidades de abastecimiento (provenientes de la etapa de abastecimiento)
     * que se sumarán a las unidades actuales en producción.
     */
    public void incrementarUnidadesDeAbastecimientoEnProduccion(int cantidad) {
        this.unidadesAbastecimientoProd += cantidad;
    }

    /**
     * Produce una unidad de producto en la etapa de producción, utilizando la cantidad especificada de unidades de abastecimiento.
     *
     * @param cantidad la cantidad de unidades de abastecimiento necesarias para producir una unidad de producto.
     *                 Si hay suficientes unidades de abastecimiento disponibles, estas se reducen en la cantidad especificada
     *                 y se incrementa en una unidad el número de productos producidos.
     */
    public void producirProductos(int cantidad) {
        if (this.unidadesAbastecimientoProd >= cantidad) {
            this.unidadesAbastecimientoProd -= cantidad;
            this.unidadesProductosProd += 1;
        }
    }

    /**
     * Expide todas las unidades de productos en la etapa de producción, estableciendo su cantidad a cero.
     * Este método se utiliza para vaciar el contador de unidades de productos una vez que han sido enviados
     * a la siguiente etapa (Almacenamiento).
     */
    public void expedirProductosDeProduccion() {
        this.unidadesProductosProd = 0;
    }

    // Transicion de Produccion a Almacenamiento

    /**
     * Incrementa la cantidad de Productos en la transicion.
     *
     * @param cantidad la cantidad de Productos (provenientes Produccion)
     * que se sumarán a la transicion.
     */
    public void incrementarProductosEnTransicion(int cantidad) {
        this.productosEnTransicion += cantidad;
    }

    /**
     * Este método se utiliza para vaciar el contador de unidades de abastecimiento en la transicion de abastecimiento a produccion
     * una vez que las unidades han sido enviadas a la siguiente etapa (Produccion).
     */
    public void expedirProductosTransicionProduccion() {
        this.productosEnTransicion = 0;
    }

    //Almacenamiento

    /**
     * Transfiere una cantidad especificada de productos a la etapa de almacenamiento.
     *
     * @param cantidad la cantidad de productos que serán transferidos al almacenamiento.
     *                 Esta cantidad se suma a las unidades actuales en el almacenamiento.
     */
    public void transferirProductosAAlmacenamiento(int cantidad) {
        this.unidadesProductosAlma += cantidad;
    }

    /**
     * Reduce (-1) la cantidad de productos en almacenamiento simulando una venta.
     */
    public void venderProducto() {
        if (this.unidadesProductosAlma > 0) {
            this.unidadesProductosAlma--;
        }
    }
}
