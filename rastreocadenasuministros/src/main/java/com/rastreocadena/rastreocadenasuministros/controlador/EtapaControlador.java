package com.rastreocadena.rastreocadenasuministros.controlador;

import com.rastreocadena.rastreocadenasuministros.modelo.Abastecimiento;
import com.rastreocadena.rastreocadenasuministros.modelo.Produccion;
import com.rastreocadena.rastreocadenasuministros.modelo.Almacenamiento;
import com.rastreocadena.rastreocadenasuministros.servicio.EtapaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etapas")
@CrossOrigin(origins = "http://localhost:3000")
public class EtapaControlador {

    @Autowired
    private EtapaServicio etapaServicio;

    @PostMapping("/abastecimiento/guardar")
    public ResponseEntity<Abastecimiento> guardarAbastecimiento(@RequestBody Abastecimiento abastecimiento) {
        Abastecimiento nuevoAbastecimiento = etapaServicio.guardarAbastecimiento(abastecimiento);
        return ResponseEntity.ok(nuevoAbastecimiento);
    }

    @PostMapping("/produccion/guardar")
    public ResponseEntity<Produccion> guardarProduccion(@RequestBody Produccion produccion) {
        Produccion nuevaProduccion = etapaServicio.guardarProduccion(produccion);
        return ResponseEntity.ok(nuevaProduccion);
    }

    @PostMapping("/almacenamiento/guardar")
    public ResponseEntity<Almacenamiento> guardarAlmacenamiento(@RequestBody Almacenamiento almacenamiento) {
        Almacenamiento nuevoAlmacenamiento = etapaServicio.guardarAlmacenamiento(almacenamiento);
        return ResponseEntity.ok(nuevoAlmacenamiento);
    }

    @GetMapping("/abastecimiento/listar")
    public ResponseEntity<List<Abastecimiento>> listarAbastecimientos() {
        List<Abastecimiento> lista = etapaServicio.listarAbastecimientos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/produccion/listar")
    public ResponseEntity<List<Produccion>> listarProducciones() {
        List<Produccion> lista = etapaServicio.listarProducciones();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/almacenamiento/listar")
    public ResponseEntity<List<Almacenamiento>> listarAlmacenamientos() {
        List<Almacenamiento> lista = etapaServicio.listarAlmacenamientos();
        return ResponseEntity.ok(lista);
    }
}
