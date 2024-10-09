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
        // Si es Abastecimiento 2
        if (abastecimiento.getTipoAbastecimiento() == 2) {
            // Usamos los campos de Abastecimiento 2
            abastecimiento.setTiempoProduccionAbastecimiento(abastecimiento.getTiempoProduccionAbastecimiento2());
            abastecimiento.setCapacidadMaximaAbastecimiento(abastecimiento.getCapacidadMaximaAbastecimiento2());
            abastecimiento.setPeriodoExpedicionAbastecimiento(abastecimiento.getPeriodoExpedicionAbastecimiento2());
        }
        // Guardar el abastecimiento utilizando el mismo servicio
        Abastecimiento nuevoAbastecimiento = abastecimientoServicio.guardarAbastecimiento(abastecimiento);
        return ResponseEntity.ok(nuevoAbastecimiento);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Abastecimiento>> listarAbastecimientos(@RequestParam(value = "tipo", defaultValue = "1") int tipo) {
        List<Abastecimiento> lista = abastecimientoServicio.listarAbastecimientosPorTipo(tipo);
        return ResponseEntity.ok(lista);
    }
}
