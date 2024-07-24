package com.rastreocadena.rastreocadenasuministros.controlador;

import com.rastreocadena.rastreocadenasuministros.modelo.Produccion;
import com.rastreocadena.rastreocadenasuministros.servicio.ProduccionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produccion")
public class ProduccionControlador {

    @Autowired
    private ProduccionServicio produccionServicio;

    @PostMapping("/guardar")
    public ResponseEntity<Produccion> guardarProduccion(@RequestBody Produccion produccion) {
        Produccion produccionGuardada = produccionServicio.guardarProduccion(produccion);
        return ResponseEntity.ok(produccionGuardada);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Produccion>> listarProducciones() {
        List<Produccion> producciones = produccionServicio.listarProducciones();
        return ResponseEntity.ok(producciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produccion> obtenerProduccion(@PathVariable Long id) {
        Optional<Produccion> produccion = produccionServicio.obtenerProduccionPorId(id);
        return produccion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
