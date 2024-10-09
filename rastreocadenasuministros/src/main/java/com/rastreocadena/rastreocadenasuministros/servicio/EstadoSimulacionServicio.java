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
    private final ReentrantLock lockAbastecimiento2 = new ReentrantLock(); // Bloqueo adicional para abastecimiento2
    private final ReentrantLock lockProduccion = new ReentrantLock();
    private final ReentrantLock lockAlmacenamiento = new ReentrantLock();
    private final ReentrantLock lockTransicionAbastecimientoProduccion = new ReentrantLock();
    private final ReentrantLock lockTransicionProduccionAlmacenamiento = new ReentrantLock();

    private int escalarTiempo(int tiempoUnidades) {
        return tiempoUnidades * 10000; // 1 unidad de tiempo = 10 segundos
    }

    public EstadoSimulacion iniciarSimulacion(Map<String, Object> etapas, String dificultad) {
        estadoSimulacion = new EstadoSimulacion(true);
        scheduler = Executors.newScheduledThreadPool(6); // Un hilo adicional para abastecimiento2

        // Configuraciones de las etapas
        Map<String, Object> abastecimientoCondiciones = (Map<String, Object>) etapas.get("abastecimiento");
        Map<String, Object> produccionCondiciones = (Map<String, Object>) etapas.get("produccion");
        Map<String, Object> almacenamientoCondiciones = (Map<String, Object>) etapas.get("almacenamiento");
        Map<String, Object> abastecimiento2Condiciones = dificultad.equals("avanzado")
                ? (Map<String, Object>) etapas.get("abastecimiento2")
                : null;

        // Validar que no falten las configuraciones
        if (abastecimientoCondiciones == null || produccionCondiciones == null || almacenamientoCondiciones == null) {
            throw new IllegalArgumentException("Faltan condiciones para una o más etapas");
        }

        estadoSimulacion.setAbastecimientoCondiciones(abastecimientoCondiciones);
        estadoSimulacion.setProduccionCondiciones(produccionCondiciones);
        estadoSimulacion.setAlmacenamientoCondiciones(almacenamientoCondiciones);
        if (dificultad.equals("avanzado") && abastecimiento2Condiciones != null) {
            estadoSimulacion.setAbastecimiento2Condiciones(abastecimiento2Condiciones);
        }

        // Obtener valores ingresados por el usuario para ambas etapas de abastecimiento
        Integer tiempoProduccionAbastecimiento = escalarTiempo((Integer) abastecimientoCondiciones.get("tiempoProduccionAbastecimiento"));
        Integer capacidadMaximaAbastecimiento = (Integer) abastecimientoCondiciones.get("capacidadMaximaAbastecimiento");
        Integer periodoExpedicionAbastecimiento = escalarTiempo((Integer) abastecimientoCondiciones.get("periodoExpedicionAbastecimiento"));

        Integer tiempoProduccionAbastecimiento2 = dificultad.equals("avanzado")
                ? escalarTiempo((Integer) abastecimiento2Condiciones.get("tiempoProduccionAbastecimiento"))
                : null;
        Integer capacidadMaximaAbastecimiento2 = dificultad.equals("avanzado")
                ? (Integer) abastecimiento2Condiciones.get("capacidadMaximaAbastecimiento")
                : null;
        Integer periodoExpedicionAbastecimiento2 = dificultad.equals("avanzado")
                ? escalarTiempo((Integer) abastecimiento2Condiciones.get("periodoExpedicionAbastecimiento"))
                : null;

        // Obtener valores de producción y almacenamiento
        Integer tiempoFabricacionProducto = escalarTiempo((Integer) produccionCondiciones.get("tiempoFabricacionProducto"));
        Integer unidadesPorProducto = (Integer) produccionCondiciones.get("unidadesPorProducto");
        Integer capacidadMaximaAbastecimientoProduccion = (Integer) produccionCondiciones.get("capacidadMaximaAbastecimientoProduccion");
        Integer capacidadMaximaProductosProduccion = (Integer) produccionCondiciones.get("capacidadMaximaProductosProduccion");
        Integer periodoExpedicionProduccion = escalarTiempo((Integer) produccionCondiciones.get("periodoExpedicionProduccion"));
        Integer capacidadMaximaAlmacenamiento = (Integer) almacenamientoCondiciones.get("capacidadMaximaProductosAlmacenamiento");
        Integer periodoCompras = escalarTiempo((Integer) almacenamientoCondiciones.get("periodoCompras"));

        // Lógica de tiempos de transición entre etapas
        Integer tiempoTransicion = (tiempoProduccionAbastecimiento == 1 || tiempoFabricacionProducto == 1) ? escalarTiempo(1) : escalarTiempo(2);
        int tiempoReal = tiempoProduccionAbastecimiento == 1 || tiempoFabricacionProducto == 1 ? 1 : 2;

        // Definir variables de retraso en los procedimientos
        Integer delayTransicionAbastecimiento = periodoExpedicionAbastecimiento + tiempoTransicion;
        Integer delayTransicionAbastecimiento2 = dificultad.equals("avanzado")
                ? periodoExpedicionAbastecimiento2 + tiempoTransicion
                : null;
        Integer delayFabricacionProducto = delayTransicionAbastecimiento + tiempoTransicion + tiempoFabricacionProducto;
        Integer delayTransicionProduccion = delayTransicionAbastecimiento + periodoExpedicionProduccion;
        Integer delayTransicionAlmacenamiento = delayTransicionProduccion + tiempoTransicion;

        // Validar que todos los valores sean correctos
        if (tiempoProduccionAbastecimiento == null || periodoExpedicionAbastecimiento == null || tiempoFabricacionProducto == null
                || unidadesPorProducto == null || capacidadMaximaAbastecimientoProduccion == null || periodoExpedicionProduccion == null
                || capacidadMaximaAlmacenamiento == null || periodoCompras == null) {
            throw new IllegalArgumentException("Uno o más valores de las condiciones son nulos");
        }

        // Condiciones para abastecimiento 2 en caso avanzado
        if (dificultad.equals("avanzado") && (tiempoProduccionAbastecimiento2 == null || periodoExpedicionAbastecimiento2 == null)) {
            throw new IllegalArgumentException("Faltan condiciones para Abastecimiento 2 en la dificultad Avanzado.");
        }

        // Contador para manejar las ejecuciones de expedición e incremento en Abastecimiento 1
        AtomicInteger contadorExpedicionAbastecimiento = new AtomicInteger();
        AtomicInteger contadorExpedicionAbastecimiento2 = new AtomicInteger(); // Para abastecimiento2
        // Contador para manejar las ejecuciones de producción y expedición
        AtomicInteger contadorExpedicionProduccion = new AtomicInteger();


        // Lógica para la producción y expedición en Abastecimiento
        scheduler.scheduleAtFixedRate(() -> {
            lockAbastecimiento.lock();
            try {
                contadorExpedicionAbastecimiento.getAndIncrement();
                boolean esTiempoDeExpedicion = contadorExpedicionAbastecimiento.get() >= (periodoExpedicionAbastecimiento / tiempoProduccionAbastecimiento);

                if (esTiempoDeExpedicion && estadoSimulacion.getUnidadesAbastecimientoAba() > 0) {
                    lockTransicionAbastecimientoProduccion.lock();
                    try {
                        int unidadesParaTransicion = estadoSimulacion.getUnidadesAbastecimientoAba();
                        estadoSimulacion.incrementarUnidadesDeAbastecimientoEnTransicion(unidadesParaTransicion);
                        estadoSimulacion.expedirUnidadesAbastecimiento();
                        estadoSimulacion.setAlertaProduccion("Unidades de Abastecimiento 1 a: " + tiempoReal + " minutos de llegar");
                        contadorExpedicionAbastecimiento.set(0);
                    } finally {
                        lockTransicionAbastecimientoProduccion.unlock();
                    }
                }

                if (estadoSimulacion.getUnidadesAbastecimientoAba() < capacidadMaximaAbastecimiento) {
                    estadoSimulacion.incrementarUnidadesDeAbastecimientoEnAbastecimiento();
                } else {
                    estadoSimulacion.incrementarUnidadesAbastecimientoDesechadas();
                }
            } finally {
                lockAbastecimiento.unlock();
            }
        }, tiempoProduccionAbastecimiento, tiempoProduccionAbastecimiento, TimeUnit.MILLISECONDS);

        // Lógica para la producción y expedición en Abastecimiento 2 (dificultad avanzada)
        if (dificultad.equals("avanzado")) {
            scheduler.scheduleAtFixedRate(() -> {
                lockAbastecimiento2.lock();
                try {
                    contadorExpedicionAbastecimiento2.getAndIncrement();
                    boolean esTiempoDeExpedicion2 = contadorExpedicionAbastecimiento2.get() >= (periodoExpedicionAbastecimiento2 / tiempoProduccionAbastecimiento2);

                    if (esTiempoDeExpedicion2 && estadoSimulacion.getUnidadesAbastecimientoAba2() > 0) {
                        lockTransicionAbastecimientoProduccion.lock();
                        try {
                            int unidadesParaTransicion2 = estadoSimulacion.getUnidadesAbastecimientoAba2();
                            estadoSimulacion.incrementarUnidadesDeAbastecimientoEnTransicion(unidadesParaTransicion2);
                            estadoSimulacion.expedirUnidadesAbastecimiento2();
                            estadoSimulacion.setAlertaProduccion("Unidades de Abastecimiento 2 a: " + tiempoReal + " minutos de llegar");
                            contadorExpedicionAbastecimiento2.set(0);
                        } finally {
                            lockTransicionAbastecimientoProduccion.unlock();
                        }
                    }

                    if (estadoSimulacion.getUnidadesAbastecimientoAba2() < capacidadMaximaAbastecimiento2) {
                        estadoSimulacion.incrementarUnidadesDeAbastecimientoEnAbastecimiento2();
                    } else {
                        estadoSimulacion.incrementarUnidadesAbastecimientoDesechadas();
                    }
                } finally {
                    lockAbastecimiento2.unlock();
                }
            }, tiempoProduccionAbastecimiento2, tiempoProduccionAbastecimiento2, TimeUnit.MILLISECONDS);
        }
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
                    } else {
                        int unidadesDesechadas = unidadesParaProduccion - espacioDisponibleProduccion;
                        estadoSimulacion.incrementarUnidadesDeAbastecimientoEnProduccion(espacioDisponibleProduccion);
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
                } else if (esTiempoDeExpedicion && estadoSimulacion.getUnidadesProductosProd() == 0) {
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


        return estadoSimulacion;
    }

    public EstadoSimulacion detenerSimulacion() {
        if (scheduler != null) {
            scheduler.shutdownNow();
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
            // Resetear valores de Abastecimiento 1
            estadoSimulacion.setUnidadesAbastecimiento(0);

            // Resetear valores de Abastecimiento 2
            estadoSimulacion.setUnidadesAbastecimientoAba2(0);  // Abastecimiento 2

            // Resetear valores de Producción
            estadoSimulacion.setUnidadesAbastecimientoProduccion(0);
            estadoSimulacion.setUnidadesProductosProd(0);

            // Resetear valores de Almacenamiento
            estadoSimulacion.setUnidadesProductosAlma(0);

            // Resetear contadores
            estadoSimulacion.setUnidadesAbastecimientoGeneradas(0);
            estadoSimulacion.setProductosGenerados(0);
            estadoSimulacion.setProductosVendidos(0);
            estadoSimulacion.setUnidadesAbastecimientoDesechadas(0);
            estadoSimulacion.setProductosDesechados(0);

            // Resetear alertas
            estadoSimulacion.setAlertaAbastecimiento(null);
            estadoSimulacion.setAlertaProduccion(null);
            estadoSimulacion.setAlertaAlmacenamiento(null);
        }
    }

}




