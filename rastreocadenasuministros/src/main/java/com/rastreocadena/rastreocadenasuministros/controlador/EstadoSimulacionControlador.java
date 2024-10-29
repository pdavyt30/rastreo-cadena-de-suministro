package com.rastreocadena.rastreocadenasuministros.controlador;

import com.rastreocadena.rastreocadenasuministros.modelo.EstadoSimulacion;
import com.rastreocadena.rastreocadenasuministros.servicio.EstadoSimulacionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/simulacion")
@CrossOrigin(origins = "http://localhost:3000")
public class EstadoSimulacionControlador {

    @Autowired
    private EstadoSimulacionServicio estadoSimulacionServicio;

    @PostMapping("/iniciar")
    public ResponseEntity<EstadoSimulacion> iniciarSimulacion(@RequestBody Map<String, Object> simulacionData) {
        String dificultad = (String) simulacionData.get("dificultad");
        EstadoSimulacion estadoSimulacion = estadoSimulacionServicio.iniciarSimulacion(simulacionData, dificultad);
        return ResponseEntity.ok(estadoSimulacion);
    }

    @PostMapping("/detener")
    public ResponseEntity<EstadoSimulacion> detenerSimulacion() {
        EstadoSimulacion estadoSimulacion = estadoSimulacionServicio.detenerSimulacion();
        return ResponseEntity.ok(estadoSimulacion);
    }

    @GetMapping("/estado")
    public ResponseEntity<EstadoSimulacion> obtenerEstadoSimulacion() {
        EstadoSimulacion estadoSimulacion = estadoSimulacionServicio.obtenerEstadoSimulacion();
        return ResponseEntity.ok(estadoSimulacion);
    }

    @PostMapping("/borrar-datos")
    public ResponseEntity<Void> borrarDatos() {
        estadoSimulacionServicio.borrarDatos();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reporte")
    public ResponseEntity<InputStreamResource> generarReporte() {
        try {
            // Obtener el estado actual de la simulación
            EstadoSimulacion estadoSimulacion = estadoSimulacionServicio.obtenerEstadoSimulacion();

            // Construir el reporte con los datos de la simulación
            StringBuilder reporte = new StringBuilder();

            reporte.append("Reporte de Simulación\n");
            reporte.append("=====================================\n");
            reporte.append("Valores ingresados en los formularios de las etapas:\n\n");

            // Condiciones de Abastecimiento
            Map<String, Object> abastecimientoCondiciones = estadoSimulacion.getAbastecimientoCondiciones();
            reporte.append("Abastecimiento:\n");
            reporte.append("Tiempo de Producción en Abastecimiento: " + abastecimientoCondiciones.get("tiempoProduccionAbastecimiento") + " minutos\n");
            reporte.append("Capacidad Máxima de Abastecimiento: " + abastecimientoCondiciones.get("capacidadMaximaAbastecimiento") + "unidades\n");
            reporte.append("Periodo entre Tandas de Expedición: " + abastecimientoCondiciones.get("periodoExpedicionAbastecimiento") + " minutos\n\n");

            // Condiciones de Producción
            Map<String, Object> produccionCondiciones = estadoSimulacion.getProduccionCondiciones();
            reporte.append("Producción:\n");
            reporte.append("Unidades por Producto: " + produccionCondiciones.get("unidadesPorProducto") + "\n");
            reporte.append("Tiempo de Fabricación de Producto: " + produccionCondiciones.get("tiempoFabricacionProducto") + " minutos\n");
            reporte.append("Capacidad Máxima de Abastecimiento en Producción: " + produccionCondiciones.get("capacidadMaximaAbastecimientoProduccion") + "unidades\n");
            reporte.append("Capacidad Máxima de Productos en Producción: " + produccionCondiciones.get("capacidadMaximaProductosProduccion") + "productos\n");
            reporte.append("Periodo entre Expediciones en Producción: " + produccionCondiciones.get("periodoExpedicionProduccion") + " minutos\n\n");

            // Condiciones de Almacenamiento
            Map<String, Object> almacenamientoCondiciones = estadoSimulacion.getAlmacenamientoCondiciones();
            reporte.append("Almacenamiento:\n");
            reporte.append("Capacidad Máxima de Productos en Almacenamiento: " + almacenamientoCondiciones.get("capacidadMaximaProductosAlmacenamiento") + "productos\n");
            reporte.append("Periodo de Compras: " + almacenamientoCondiciones.get("periodoCompras") + " minutos\n\n");

            reporte.append("=====================================\n");
            reporte.append("Información generada durante la simulación:\n\n");

            // Información generada durante la simulación
            reporte.append("Unidades de Abastecimiento generadas: " + estadoSimulacion.getUnidadesAbastecimientoGeneradas() + "\n");
            reporte.append("Productos generados: " + estadoSimulacion.getProductosGenerados() + "\n");
            reporte.append("Productos vendidos: " + estadoSimulacion.getProductosVendidos() + "\n");
            reporte.append("Unidades de Abastecimiento desechadas: " + estadoSimulacion.getUnidadesAbastecimientoDesechadas() + "\n");
            reporte.append("Productos desechados: " + estadoSimulacion.getProductosDesechados() + "\n\n");

            // Información sobre las unidades y productos en las etapas
            reporte.append("Unidades de Abastecimiento en Abastecimiento: " + estadoSimulacion.getUnidadesAbastecimientoAba() + "\n");
            reporte.append("Unidades en Transición de Abastecimiento a Producción: " + estadoSimulacion.getUnidadesEnTransicion() + "\n");
            reporte.append("Unidades de Abastecimiento en Producción: " + estadoSimulacion.getUnidadesAbastecimientoProduccion() + "\n");
            reporte.append("Productos en Producción: " + estadoSimulacion.getUnidadesProductosProd() + "\n");
            reporte.append("Productos en Transición de Producción a Almacenamiento: " + estadoSimulacion.getProductosEnTransicion() + "\n");
            reporte.append("Productos en Almacenamiento: " + estadoSimulacion.getUnidadesProductosAlma() + "\n");

            // Convertir el reporte a un archivo descargable
            ByteArrayInputStream bis = new ByteArrayInputStream(reporte.toString().getBytes(StandardCharsets.UTF_8));
            InputStreamResource resource = new InputStreamResource(bis);

            // Configurar la respuesta HTTP para forzar la descarga del archivo
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_simulacion.txt")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
