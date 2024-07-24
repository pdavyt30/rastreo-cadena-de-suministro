package com.rastreocadena.rastreocadenasuministros.controlador;

import com.rastreocadena.rastreocadenasuministros.modelo.EstadoSimulacion;
import com.rastreocadena.rastreocadenasuministros.servicio.EstadoSimulacionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/simulacion")
@CrossOrigin(origins = "http://localhost:3000")
public class EstadoSimulacionControlador {

    @Autowired
    private EstadoSimulacionServicio estadoSimulacionServicio;

    @PostMapping("/iniciar")
    public ResponseEntity<EstadoSimulacion> iniciarSimulacion(@RequestBody Map<String, Object> etapas) {
        EstadoSimulacion estadoSimulacion = estadoSimulacionServicio.iniciarSimulacion(etapas);
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
}
