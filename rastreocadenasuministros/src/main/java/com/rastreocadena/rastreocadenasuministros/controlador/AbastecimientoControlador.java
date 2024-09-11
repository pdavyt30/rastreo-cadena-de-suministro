package com.rastreocadena.rastreocadenasuministros.controlador;

import com.rastreocadena.rastreocadenasuministros.modelo.Abastecimiento;
import com.rastreocadena.rastreocadenasuministros.servicio.AbastecimientoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/abastecimiento")
@CrossOrigin(origins = "http://localhost:3000")
public class AbastecimientoControlador {

    @Autowired
    private AbastecimientoServicio abastecimientoServicio;

    @PostMapping("/guardar")
    public ResponseEntity<Abastecimiento> guardarAbastecimiento(@RequestBody Abastecimiento abastecimiento) {
        Abastecimiento nuevoAbastecimiento = abastecimientoServicio.guardarAbastecimiento(abastecimiento);
        return ResponseEntity.ok(nuevoAbastecimiento);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Abastecimiento>> listarAbastecimientos() {
        List<Abastecimiento> lista = abastecimientoServicio.listarAbastecimientos();
        return ResponseEntity.ok(lista);
    }
}
