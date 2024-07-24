package com.rastreocadena.rastreocadenasuministros.controlador;

import com.rastreocadena.rastreocadenasuministros.modelo.Almacenamiento;
import com.rastreocadena.rastreocadenasuministros.servicio.AlmacenamientoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/almacenamiento")
public class AlmacenamientoControlador {

    @Autowired
    private AlmacenamientoServicio almacenamientoServicio;

    @PostMapping("/guardar")
    public ResponseEntity<Almacenamiento> guardarAlmacenamiento(@RequestBody Almacenamiento almacenamiento) {
        Almacenamiento almacenamientoGuardado = almacenamientoServicio.guardarAlmacenamiento(almacenamiento);
        return ResponseEntity.ok(almacenamientoGuardado);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Almacenamiento>> listarAlmacenamientos() {
        List<Almacenamiento> almacenamientos = almacenamientoServicio.listarAlmacenamientos();
        return ResponseEntity.ok(almacenamientos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Almacenamiento> obtenerAlmacenamiento(@PathVariable Long id) {
        Optional<Almacenamiento> almacenamiento = almacenamientoServicio.obtenerAlmacenamientoPorId(id);
        return almacenamiento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
