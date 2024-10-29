package com.rastreocadena.rastreocadenasuministros.servicio;

import com.rastreocadena.rastreocadenasuministros.modelo.EstadoSimulacion;
import com.rastreocadena.rastreocadenasuministros.repositorio.AbastecimientoRepositorio;
import com.rastreocadena.rastreocadenasuministros.repositorio.AlmacenamientoRepositorio;
import com.rastreocadena.rastreocadenasuministros.repositorio.ProduccionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;
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

    private final ReentrantLock lockAbastecimiento2 = new ReentrantLock();
    private final ReentrantLock lockProduccion = new ReentrantLock();
    private final ReentrantLock lockAlmacenamiento = new ReentrantLock();
    private final ReentrantLock lockTransicionAbastecimientoProduccion = new ReentrantLock();

    private final ReentrantLock lockTransicionAbastecimiento2Produccion = new ReentrantLock();
    private final ReentrantLock lockTransicionProduccionAlmacenamiento = new ReentrantLock();

    private int escalarTiempo(int tiempoUnidades) {
        return tiempoUnidades * 10000; // 1 unidad de tiempo = 10 segundos
    }

    public EstadoSimulacion iniciarSimulacion(Map<String, Object> etapas, String dificultad) {

        estadoSimulacion = new EstadoSimulacion(true);
        scheduler = Executors.newScheduledThreadPool(5);

        //Se guardan las condiciones de las etapas (valores ingresados por el usuario)
        Map<String, Object> abastecimientoCondiciones = (Map<String, Object>) etapas.get("abastecimiento");
        Map<String, Object> abastecimiento2Condiciones = null;
        Map<String, Object> produccionCondiciones = (Map<String, Object>) etapas.get("produccion");
        Map<String, Object> almacenamientoCondiciones = (Map<String, Object>) etapas.get("almacenamiento");

        //Se guardan las condiciones de Abastecimiento 2 (dificultad avanzado)
        if (dificultad.equals("avanzado")) {
            abastecimiento2Condiciones = (Map<String, Object>) etapas.get("abastecimiento2");
            estadoSimulacion.setAbastecimiento2Condiciones(abastecimiento2Condiciones);
        }

        //Control para que ninguna condicion quede sin valor
        if (abastecimientoCondiciones == null || produccionCondiciones == null || almacenamientoCondiciones == null ||
                (dificultad.equals("avanzado") && abastecimiento2Condiciones == null)) {
            throw new IllegalArgumentException("Faltan condiciones para una o más etapas");
        }

        //Se carga el estadoSimulacion con los valores de las condiciones de las etapas
        estadoSimulacion.setAbastecimientoCondiciones(abastecimientoCondiciones);
        estadoSimulacion.setProduccionCondiciones(produccionCondiciones);
        estadoSimulacion.setAlmacenamientoCondiciones(almacenamientoCondiciones);

        //Se descomponen los mapas de condiciones en variables locales para el estadoSimulacion:
        int tiempoProduccionAbastecimiento = escalarTiempo((Integer) abastecimientoCondiciones.get("tiempoProduccionAbastecimiento"));
        int capacidadMaximaAbastecimiento = (Integer) abastecimientoCondiciones.get("capacidadMaximaAbastecimiento");
        int periodoExpedicionAbastecimiento = escalarTiempo((Integer) abastecimientoCondiciones.get("periodoExpedicionAbastecimiento"));

        int tiempoFabricacionProducto = escalarTiempo((Integer) produccionCondiciones.get("tiempoFabricacionProducto"));
        Integer unidadesPorProducto = (Integer) produccionCondiciones.get("unidadesPorProducto");
        Integer capacidadMaximaAbastecimientoProduccion = (Integer) produccionCondiciones.get("capacidadMaximaAbastecimientoProduccion");
        Integer capacidadMaximaProductosProduccion = (Integer) produccionCondiciones.get("capacidadMaximaProductosProduccion");
        int periodoExpedicionProduccion = escalarTiempo((Integer) produccionCondiciones.get("periodoExpedicionProduccion"));
        Integer capacidadMaximaAlmacenamiento = (Integer) almacenamientoCondiciones.get("capacidadMaximaProductosAlmacenamiento");
        int periodoCompras = escalarTiempo((Integer) almacenamientoCondiciones.get("periodoCompras"));

        // En el caso avanzado, también obtendremos los valores para el segundo abastecimiento
        Integer tiempoProduccionAbastecimiento2;
        Integer capacidadMaximaAbastecimiento2;
        Integer periodoExpedicionAbastecimiento2;
        if (dificultad.equals("avanzado")) {
            tiempoProduccionAbastecimiento2 = escalarTiempo((Integer) abastecimiento2Condiciones.get("tiempoProduccionAbastecimiento"));
            capacidadMaximaAbastecimiento2 = (Integer) abastecimiento2Condiciones.get("capacidadMaximaAbastecimiento");
            periodoExpedicionAbastecimiento2 = escalarTiempo((Integer) abastecimiento2Condiciones.get("periodoExpedicionAbastecimiento"));
        } else {
            capacidadMaximaAbastecimiento2 = null;
            tiempoProduccionAbastecimiento2 = null;
            periodoExpedicionAbastecimiento2 = null;
        }

        // Se define tiempo de transición según los tiempos de producción
        int tiempoTransicion;
        if (tiempoProduccionAbastecimiento == escalarTiempo(1) || tiempoFabricacionProducto == escalarTiempo(1) ||
                (dificultad.equals("avanzado") && tiempoProduccionAbastecimiento2 == escalarTiempo(1))) {
            tiempoTransicion = escalarTiempo(1);
        } else {
            tiempoTransicion = escalarTiempo(2);
        }

        // Se definen variables para retrasos en el inicio de procedimientos:
        int delayTransicionAbastecimiento = periodoExpedicionAbastecimiento + tiempoTransicion ;
        int delayFabricacionProducto = delayTransicionAbastecimiento + tiempoFabricacionProducto;
        int delayTransicionProduccion = delayTransicionAbastecimiento + periodoExpedicionProduccion;
        int delayTransicionAlmacenamiento = delayTransicionProduccion + tiempoTransicion;

        if (unidadesPorProducto == null || capacidadMaximaAbastecimientoProduccion == null || capacidadMaximaAlmacenamiento == null) {
            throw new IllegalArgumentException("Uno o más valores de las condiciones son nulos");
        }

        //Procesos ejecutados periodicamente para la actualizacion de los valores en la simulacion.

        // [Expedición de Unidades desde Abastecimiento] - Envía las unidades generadas a la siguiente etapa
        scheduler.scheduleAtFixedRate(() -> {
            lockAbastecimiento.lock();
            try {
                if (estadoSimulacion.getUnidadesAbastecimientoAba() > 0) {
                    lockTransicionAbastecimientoProduccion.lock();
                    try {
                        int unidadesParaTransicion = estadoSimulacion.getUnidadesAbastecimientoAba();
                        estadoSimulacion.incrementarUnidadesDeAbastecimientoEnTransicion(unidadesParaTransicion);
                        estadoSimulacion.expedirUnidadesAbastecimiento();
                        estadoSimulacion.setAlertaAbastecimiento(null);
                    } finally {
                        lockTransicionAbastecimientoProduccion.unlock();
                    }
                } else {
                    estadoSimulacion.setAlertaAbastecimiento("No hay productos para expedir");
                }
            } finally {
                lockAbastecimiento.unlock();
            }
        }, periodoExpedicionAbastecimiento, periodoExpedicionAbastecimiento, TimeUnit.MILLISECONDS);

        // [Expedición de Unidades desde Abastecimiento 2] - Envía las unidades generadas a la siguiente etapa
        if (dificultad.equals("avanzado")) {
            scheduler.scheduleAtFixedRate(() -> {
                lockAbastecimiento2.lock();
                try {
                    if (estadoSimulacion.getUnidadesAbastecimientoAba2() > 0) {
                        lockTransicionAbastecimiento2Produccion.lock();
                        try {
                            int unidadesParaTransicion2 = estadoSimulacion.getUnidadesAbastecimientoAba2();
                            estadoSimulacion.incrementarUnidadesDeAbastecimientoEnTransicion2(unidadesParaTransicion2);
                            estadoSimulacion.expedirUnidadesAbastecimiento2();
                            estadoSimulacion.setAlertaAbastecimiento2(null);
                        } finally {
                            lockTransicionAbastecimiento2Produccion.unlock();
                        }
                    } else {
                        estadoSimulacion.setAlertaAbastecimiento2("No hay productos para expedir en Abastecimiento 2");
                    }
                } finally {
                    lockAbastecimiento2.unlock();
                }
            }, periodoExpedicionAbastecimiento2, periodoExpedicionAbastecimiento2, TimeUnit.MILLISECONDS);
        }

        // [Incremento de Unidades en Abastecimiento] - Produce una unidad de Abastecimiento cada vez que se ejecuta
        scheduler.scheduleAtFixedRate(() -> {
            lockAbastecimiento.lock();
            try {
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

        // [Incremento de Unidades en Abastecimiento 2] - Produce una unidad de Abastecimiento cada vez que se ejecuta
        if (dificultad.equals("avanzado")) {
            scheduler.scheduleAtFixedRate(() -> {
                lockAbastecimiento2.lock();
                try {
                    if (estadoSimulacion.getUnidadesAbastecimientoAba2() < capacidadMaximaAbastecimiento2) {
                        estadoSimulacion.incrementarUnidadesDeAbastecimientoEnAbastecimiento2();
                        estadoSimulacion.incrementarUnidadesAbastecimientoGeneradas();
                        estadoSimulacion.setAlertaAbastecimiento2(null);
                    } else {
                        estadoSimulacion.setAlertaAbastecimiento2("Capacidad máxima alcanzada en Abastecimiento 2");
                        estadoSimulacion.incrementarUnidadesAbastecimientoDesechadas();
                    }
                } finally {
                    lockAbastecimiento2.unlock();
                }
            }, tiempoProduccionAbastecimiento2, tiempoProduccionAbastecimiento2, TimeUnit.MILLISECONDS);
        }

        // [TRANSICION 1] Lógica para la transferencia de las unidades de abastecimiento desde Transición a Producción
        scheduler.scheduleAtFixedRate(() -> {
            lockTransicionAbastecimientoProduccion.lock();
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
                lockTransicionAbastecimientoProduccion.unlock();
            }
        }, delayTransicionAbastecimiento, periodoExpedicionAbastecimiento, TimeUnit.MILLISECONDS);

        // [TRANSICION 2] Lógica para manejar la transferencia de las unidades de abastecimiento desde Transicion 2 a Producción
        if (dificultad.equals("avanzado")) {
            scheduler.scheduleAtFixedRate(() -> {
                lockTransicionAbastecimiento2Produccion.lock();
                try {
                    lockProduccion.lock();
                    try {
                        int unidadesParaProduccion = estadoSimulacion.getUnidadesEnTransicion2();
                        int espacioDisponibleProduccion = capacidadMaximaAbastecimientoProduccion - estadoSimulacion.getUnidadesAbastecimientoProduccion();

                        if (unidadesParaProduccion <= espacioDisponibleProduccion) {
                            estadoSimulacion.incrementarUnidadesDeAbastecimientoEnProduccion(unidadesParaProduccion);
                            estadoSimulacion.setAlertaProduccion("Unidades recibidas desde Abastecimiento 2: " + unidadesParaProduccion);
                        } else {
                            int unidadesDesechadas = unidadesParaProduccion - espacioDisponibleProduccion;
                            estadoSimulacion.incrementarUnidadesDeAbastecimientoEnProduccion(espacioDisponibleProduccion);
                            estadoSimulacion.setAlertaProduccion("Unidades recibidas desde Abastecimiento 2: " + espacioDisponibleProduccion + " - Unidades desechadas: " + unidadesDesechadas);
                            estadoSimulacion.incrementarUnidadesAbastecimientoDesechadas(unidadesDesechadas);
                        }
                        estadoSimulacion.expedirUnidadesTransicionAbastecimiento2();
                    } finally {
                        lockProduccion.unlock();
                    }
                } finally {
                    lockTransicionAbastecimiento2Produccion.unlock();
                }
            }, delayTransicionAbastecimiento, escalarTiempo(4), TimeUnit.MILLISECONDS);
        }

        // [EXPEDICIÓN DE PRODUCTOS] Expedir productos cada 'periodoExpedicionProduccion'
        scheduler.scheduleAtFixedRate(() -> {
            lockProduccion.lock();
            try {
                if (estadoSimulacion.getUnidadesProductosProd() > 0) {
                    lockTransicionProduccionAlmacenamiento.lock();
                    try {
                        int unidadesParaAlmacenamiento = estadoSimulacion.getUnidadesProductosProd();
                        estadoSimulacion.expedirProductosDeProduccion();
                        estadoSimulacion.incrementarProductosEnTransicion(unidadesParaAlmacenamiento);
                    } finally {
                        lockTransicionProduccionAlmacenamiento.unlock();
                    }
                } else {
                    estadoSimulacion.setAlertaProduccion("No hay productos para expedir");
                }
            } finally {
                lockProduccion.unlock();
            }
        }, delayTransicionProduccion, periodoExpedicionProduccion, TimeUnit.MILLISECONDS);

        // [INCREMENTO DE PRODUCTO]  Genera un nuevo producto cada 'tiempoFabricacionProducto'
        scheduler.scheduleAtFixedRate(() -> {
            lockProduccion.lock();
            try {
                boolean faltaUnidades = estadoSimulacion.getUnidadesAbastecimientoProduccion() < unidadesPorProducto;
                boolean produccionLlena = estadoSimulacion.getUnidadesProductosProd() >= capacidadMaximaProductosProduccion;

                if (faltaUnidades && produccionLlena) {
                    estadoSimulacion.setAlertaProduccion("No hay suficientes unidades de abastecimiento y la producción está llena.");
                } else if (faltaUnidades) {
                    estadoSimulacion.setAlertaProduccion("No hay suficientes unidades de abastecimiento.");
                } else if (produccionLlena) {
                    estadoSimulacion.setAlertaProduccion("Capacidad máxima de producción alcanzada.");
                    estadoSimulacion.incrementarProductosDesechados();
                } else {
                    estadoSimulacion.producirProductos(unidadesPorProducto);
                    estadoSimulacion.incrementarProductosGenerados();
                }
            } finally {
                lockProduccion.unlock();
            }
        }, delayFabricacionProducto, tiempoFabricacionProducto, TimeUnit.MILLISECONDS);

        // [TRANSICION PROD-ALM] Lógica para la transferencia de Productos desde Transición a Almacenamiento
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
        }, delayTransicionAlmacenamiento, periodoExpedicionProduccion, TimeUnit.MILLISECONDS);

        // [ALMACENAMIENTO] Lógica para la compra de productos desde Almacenamiento
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
            estadoSimulacion.setUnidadesAbastecimientoAba(0);
            estadoSimulacion.setUnidadesAbastecimientoAba2(0);
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


