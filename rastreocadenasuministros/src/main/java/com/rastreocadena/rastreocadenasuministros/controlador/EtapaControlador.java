package com.rastreocadena.rastreocadenasuministros.controlador;

import com.rastreocadena.rastreocadenasuministros.modelo.Etapa;
import com.rastreocadena.rastreocadenasuministros.servicio.EtapaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etapas")
public class EtapaControlador {

    @Autowired
    private EtapaServicio etapaServicio;

    @PostMapping("/guardar")
    public ResponseEntity<Etapa> guardarEtapa(@RequestBody Etapa etapa) {
        Etapa etapaGuardada = etapaServicio.guardarEtapa(etapa);
        return ResponseEntity.ok(etapaGuardada);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Etapa>> listarEtapas() {
        List<Etapa> etapas = etapaServicio.listarEtapas();
        return ResponseEntity.ok(etapas);
    }

    // Otros endpoints según sea necesario
}
