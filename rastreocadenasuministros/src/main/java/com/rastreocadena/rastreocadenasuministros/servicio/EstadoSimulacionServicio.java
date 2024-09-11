package com.rastreocadena.rastreocadenasuministros.servicio;

import com.rastreocadena.rastreocadenasuministros.modelo.EstadoSimulacion;
import com.rastreocadena.rastreocadenasuministros.repositorio.AbastecimientoRepositorio;
import com.rastreocadena.rastreocadenasuministros.repositorio.AlmacenamientoRepositorio;
import com.rastreocadena.rastreocadenasuministros.repositorio.ProduccionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class EstadoSimulacionServicio {

    @Autowired
    private AbastecimientoRepositorio abastecimientoRepositorio;

    @Autowired
    private ProduccionRepositorio produccionRepositorio;

    @Autowired
    private AlmacenamientoRepositorio almacenamientoRepositorio;

    private EstadoSimulacion estadoSimulacion;
    private ScheduledExecutorService scheduler;

    // Bloqueos para cada etapa y transición
    private final ReentrantLock lockAbastecimiento = new ReentrantLock();
    private final ReentrantLock lockProduccion = new ReentrantLock();
    private final ReentrantLock lockAlmacenamiento = new ReentrantLock();
    private final ReentrantLock lockTransicionAbastecimientoProduccion = new ReentrantLock();
    private final ReentrantLock lockTransicionProduccionAlmacenamiento = new ReentrantLock();

    private int escalarTiempo(int tiempoUnidades) {
        return tiempoUnidades * 10000; // 1 unidad de tiempo = 10 segundos
    }

    public EstadoSimulacion iniciarSimulacion(Map<String, Object> etapas) {
        estadoSimulacion = new EstadoSimulacion(true);
        scheduler = Executors.newScheduledThreadPool(5); // Un hilo para cada etapa y transición

        Map<String, Object> abastecimientoCondiciones = (Map<String, Object>) etapas.get("abastecimiento");
        Map<String, Object> produccionCondiciones = (Map<String, Object>) etapas.get("produccion");
        Map<String, Object> almacenamientoCondiciones = (Map<String, Object>) etapas.get("almacenamiento");

        if (abastecimientoCondiciones == null || produccionCondiciones == null || almacenamientoCondiciones == null) {
            throw new IllegalArgumentException("Faltan condiciones para una o más etapas");
        }

        estadoSimulacion.setAbastecimientoCondiciones(abastecimientoCondiciones);
        estadoSimulacion.setProduccionCondiciones(produccionCondiciones);
        estadoSimulacion.setAlmacenamientoCondiciones(almacenamientoCondiciones);

        // Obtener valores ingresados por el usuario:
        Integer tiempoProduccionAbastecimiento = escalarTiempo((Integer) abastecimientoCondiciones.get("tiempoProduccionAbastecimiento"));
        Integer capacidadMaximaAbastecimiento = (Integer) abastecimientoCondiciones.get("capacidadMaximaAbastecimiento");
        Integer periodoExpedicionAbastecimiento = escalarTiempo((Integer) abastecimientoCondiciones.get("periodoExpedicionAbastecimiento"));

        Integer tiempoFabricacionProducto = escalarTiempo((Integer) produccionCondiciones.get("tiempoFabricacionProducto"));
        Integer unidadesPorProducto = (Integer) produccionCondiciones.get("unidadesPorProducto");
        Integer capacidadMaximaAbastecimientoProduccion = (Integer) produccionCondiciones.get("capacidadMaximaAbastecimientoProduccion");
        Integer capacidadMaximaProductosProduccion = (Integer) produccionCondiciones.get("capacidadMaximaProductosProduccion");
        Integer periodoExpedicionProduccion = escalarTiempo((Integer) produccionCondiciones.get("periodoExpedicionProduccion"));

        Integer capacidadMaximaAlmacenamiento = (Integer) almacenamientoCondiciones.get("capacidadMaximaProductosAlmacenamiento");
        Integer periodoCompras = escalarTiempo((Integer) almacenamientoCondiciones.get("periodoCompras"));

        // Definir tiempo de transición automáticamente según los tiempos de producción
        Integer tiempoTransicion;
        int tiempoReal;
        if (tiempoProduccionAbastecimiento == 1 || tiempoFabricacionProducto == 1) {
            tiempoTransicion = escalarTiempo((Integer) 1);
            tiempoReal = 1;
        } else {
            tiempoTransicion = escalarTiempo((Integer) 2);
            tiempoReal = 2;
        }

        //Definir variables para retrasos en el inicio de procedimientos:
        Integer delayTransicionAbastecimiento = periodoExpedicionAbastecimiento + tiempoTransicion;
        Integer delayFabricacionProducto = delayTransicionAbastecimiento + tiempoTransicion + tiempoFabricacionProducto;
        Integer delayTransicionProduccion = delayTransicionAbastecimiento + periodoExpedicionProduccion;
        Integer delayTransicionAlmacenamiento = delayTransicionProduccion + tiempoTransicion;

        if (tiempoProduccionAbastecimiento == null || periodoExpedicionAbastecimiento == null || tiempoFabricacionProducto == null
                || unidadesPorProducto == null || capacidadMaximaAbastecimientoProduccion == null || periodoExpedicionProduccion == null
                || capacidadMaximaAlmacenamiento == null || periodoCompras == null) {
            throw new IllegalArgumentException("Uno o más valores de las condiciones son nulos");
        }

        // Contador para manejar las ejecuciones de expedición e incremento en Abastecimiento
         AtomicInteger contadorExpedicionAbastecimiento = new AtomicInteger();

        // Lógica combinada para la producción y expedición en Abastecimiento
        scheduler.scheduleAtFixedRate(() -> {
            lockAbastecimiento.lock();
            try {
                // Incrementar el contador en cada ciclo
                contadorExpedicionAbastecimiento.getAndIncrement();

                // Verificar si es tiempo de expedir
                boolean esTiempoDeExpedicion = contadorExpedicionAbastecimiento.get() >= (periodoExpedicionAbastecimiento / tiempoProduccionAbastecimiento);

                // Si es el momento de expedir, hacerlo primero
                if (esTiempoDeExpedicion && estadoSimulacion.getUnidadesAbastecimientoAba() > 0) {
                    lockTransicionAbastecimientoProduccion.lock();
                    try {
                        int unidadesParaTransicion = estadoSimulacion.getUnidadesAbastecimientoAba();
                        estadoSimulacion.incrementarUnidadesDeAbastecimientoEnTransicion(unidadesParaTransicion);
                        estadoSimulacion.expedirUnidadesAbastecimiento();
                        estadoSimulacion.setAlertaProduccion("Unidades a: " + tiempoReal + " minutos de llegar");
                        estadoSimulacion.setAlertaAbastecimiento(null);

                        // Reiniciar el contador luego de la expedición
                        contadorExpedicionAbastecimiento.set(0);
                    } finally {
                        lockTransicionAbastecimientoProduccion.unlock();
                    }
                }else if (esTiempoDeExpedicion && estadoSimulacion.getUnidadesAbastecimientoAba()==0) {
                    estadoSimulacion.setAlertaAbastecimiento("No hay productos para expedir");
                }

                // Luego, intentamos incrementar si es que hay espacio disponible
                if (estadoSimulacion.getUnidadesAbastecimientoAba() < capacidadMaximaAbastecimiento) {
                    estadoSimulacion.incrementarUnidadesDeAbastecimientoEnAbastecimiento();
                    estadoSimulacion.incrementarUnidadesAbastecimientoGeneradas();
                    estadoSimulacion.setAlertaAbastecimiento(null);
                } else {
                    estadoSimulacion.setAlertaAbastecimiento("Capacidad máxima alcanzada en Abastecimiento");
                    estadoSimulacion.incrementarUnidadesAbastecimientoDesechadas();
                }
            } finally {
                lockAbastecimiento.unlock();
            }
        }, tiempoProduccionAbastecimiento, tiempoProduccionAbastecimiento, TimeUnit.MILLISECONDS);


        // Lógica para la transferencia de las unidades de abastecimiento desde Transición a Producción
        scheduler.scheduleAtFixedRate(() -> {
            lockTransicionAbastecimientoProduccion.lock();
            try {
                lockProduccion.lock();
                try {
                    int unidadesParaProduccion = estadoSimulacion.getUnidadesEnTransicion();
                    int espacioDisponibleProduccion = capacidadMaximaAbastecimientoProduccion - estadoSimulacion.getUnidadesAbastecimientoProduccion();

                    if (unidadesParaProduccion <= espacioDisponibleProduccion) {
                        estadoSimulacion.incrementarUnidadesDeAbastecimientoEnProduccion(unidadesParaProduccion);
                        estadoSimulacion.setAlertaProduccion("Unidades recibidas: " + unidadesParaProduccion);
                    } else {
                        int unidadesDesechadas = unidadesParaProduccion - espacioDisponibleProduccion;
                        estadoSimulacion.incrementarUnidadesDeAbastecimientoEnProduccion(espacioDisponibleProduccion);
                        estadoSimulacion.setAlertaProduccion("Unidades recibidas: " + espacioDisponibleProduccion + " - Unidades desechadas: " + unidadesDesechadas);
                        estadoSimulacion.incrementarUnidadesAbastecimientoDesechadas(unidadesDesechadas);
                    }
                    estadoSimulacion.expedirUnidadesTransicionAbastecimiento();
                } finally {
                    lockProduccion.unlock();
                }
            } finally {
                lockTransicionAbastecimientoProduccion.unlock();
            }
        }, delayTransicionAbastecimiento, delayTransicionAbastecimiento, TimeUnit.MILLISECONDS);

        // Contador para manejar las ejecuciones de producción y expedición
        AtomicInteger contadorExpedicionProduccion = new AtomicInteger();

        // Lógica combinada para la producción y expedición en Producción
        scheduler.scheduleAtFixedRate(() -> {
            lockProduccion.lock();
            try {
                // Incrementar contador
                contadorExpedicionProduccion.getAndIncrement();

                // Verificar si es tiempo de expedir productos
                boolean esTiempoDeExpedicion = contadorExpedicionProduccion.get() >= (periodoExpedicionProduccion / tiempoFabricacionProducto);

                // Si es el momento de expedir, hacerlo primero
                if (esTiempoDeExpedicion && estadoSimulacion.getUnidadesProductosProd() > 0) {
                    lockTransicionProduccionAlmacenamiento.lock();
                    try {
                        int unidadesParaAlmacenamiento = estadoSimulacion.getUnidadesProductosProd();
                        estadoSimulacion.expedirProductosDeProduccion();
                        estadoSimulacion.incrementarProductosEnTransicion(unidadesParaAlmacenamiento);
                        estadoSimulacion.setAlertaAlmacenamiento("Unidades a: " + tiempoReal + " minutos de llegar");

                        // Reiniciar el contador de expedición
                        contadorExpedicionProduccion.set(0);
                    } finally {
                        lockTransicionProduccionAlmacenamiento.unlock();
                    }
                } else if (esTiempoDeExpedicion && estadoSimulacion.getUnidadesProductosProd()==0) {
                    estadoSimulacion.setAlertaProduccion("No hay productos para expedir");
                }

                // Luego, realizar la producción de unidades si es posible
                boolean faltaUnidades = estadoSimulacion.getUnidadesAbastecimientoProduccion() < unidadesPorProducto;
                boolean produccionLlena = estadoSimulacion.getUnidadesProductosProd() >= capacidadMaximaProductosProduccion;

                if (faltaUnidades && produccionLlena) {
                    estadoSimulacion.setAlertaProduccion("No hay suficientes unidades de abastecimiento para producir y la capacidad de producción está llena.");
                } else if (faltaUnidades) {
                    estadoSimulacion.setAlertaProduccion("No hay suficientes unidades de abastecimiento para producir.");
                } else if (produccionLlena) {
                    estadoSimulacion.setAlertaProduccion("Capacidad máxima de producción alcanzada.");
                    estadoSimulacion.incrementarProductosDesechados();
                } else {
                    estadoSimulacion.producirProductos(unidadesPorProducto);
                    estadoSimulacion.incrementarProductosGenerados();
                    estadoSimulacion.setAlertaProduccion(null);
                }
            } finally {
                lockProduccion.unlock();
            }
        }, delayFabricacionProducto, tiempoFabricacionProducto, TimeUnit.MILLISECONDS);

        // Lógica para la transferencia de Productos desde Transición a Almacenamiento
        scheduler.scheduleAtFixedRate(() -> {
            lockTransicionProduccionAlmacenamiento.lock();
            try {
                lockAlmacenamiento.lock();
                try {
                    int unidadesParaAlmacenamiento = estadoSimulacion.getProductosEnTransicion();
                    int espacioDisponibleAlmacenamiento = capacidadMaximaAlmacenamiento - estadoSimulacion.getUnidadesProductosAlma();
                    if (unidadesParaAlmacenamiento <= espacioDisponibleAlmacenamiento) {
                        estadoSimulacion.transferirProductosAAlmacenamiento(unidadesParaAlmacenamiento);
                        estadoSimulacion.setAlertaAlmacenamiento(null);
                    } else {
                        int productosDesechadosCantidad = unidadesParaAlmacenamiento - espacioDisponibleAlmacenamiento;
                        estadoSimulacion.incrementarProductosDesechados(productosDesechadosCantidad);
                        estadoSimulacion.transferirProductosAAlmacenamiento(espacioDisponibleAlmacenamiento);
                        estadoSimulacion.setAlertaAlmacenamiento("Capacidad máxima en Almacenamiento alcanzada, productos excedentes desechados");
                    }
                    estadoSimulacion.expedirProductosTransicionProduccion();
                } finally {
                    lockAlmacenamiento.unlock();
                }
            } finally {
                lockTransicionProduccionAlmacenamiento.unlock();
            }
        }, delayTransicionAlmacenamiento, periodoExpedicionProduccion + tiempoTransicion, TimeUnit.MILLISECONDS);

        // Lógica para la compra de productos desde Almacenamiento
        scheduler.scheduleAtFixedRate(() -> {
            lockAlmacenamiento.lock();
            try {
                if (estadoSimulacion.getUnidadesProductosAlma() > 0) {
                    estadoSimulacion.venderProducto();
                    estadoSimulacion.incrementarProductosVendidos();
                    estadoSimulacion.setAlertaAlmacenamiento(null);
                } else {
                    estadoSimulacion.setAlertaAlmacenamiento("No hay productos disponibles para la compra.");
                }
            } finally {
                lockAlmacenamiento.unlock();
            }
        }, delayTransicionAlmacenamiento + periodoCompras, periodoCompras, TimeUnit.MILLISECONDS);

        return estadoSimulacion;
    }

    public EstadoSimulacion detenerSimulacion() {
        if (scheduler != null) {
            scheduler.shutdownNow();  // Detiene todas las tareas programadas
        }
        estadoSimulacion.setEnEjecucion(false);
        return estadoSimulacion;
    }

    public EstadoSimulacion obtenerEstadoSimulacion() {
        return estadoSimulacion;
    }

    public void borrarDatos() {
        abastecimientoRepositorio.deleteAll();
        produccionRepositorio.deleteAll();
        almacenamientoRepositorio.deleteAll();
        if (estadoSimulacion != null) {
            estadoSimulacion.setUnidadesAbastecimiento(0);
            estadoSimulacion.setUnidadesAbastecimientoProduccion(0);
            estadoSimulacion.setUnidadesProductosProd(0);
            estadoSimulacion.setUnidadesProductosAlma(0);
            estadoSimulacion.setAlertaAlmacenamiento(null);
            estadoSimulacion.setAlertaAbastecimiento(null);
            estadoSimulacion.setAlertaProduccion(null);

            estadoSimulacion.setUnidadesAbastecimientoGeneradas(0);
            estadoSimulacion.setProductosGenerados(0);
            estadoSimulacion.setProductosVendidos(0);
            estadoSimulacion.setUnidadesAbastecimientoDesechadas(0);
            estadoSimulacion.setProductosDesechados(0);
        }
    }
}