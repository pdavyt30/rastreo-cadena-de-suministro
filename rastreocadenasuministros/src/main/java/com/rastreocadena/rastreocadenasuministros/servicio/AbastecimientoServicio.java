package com.rastreocadena.rastreocadenasuministros.servicio;

import com.rastreocadena.rastreocadenasuministros.modelo.Abastecimiento;
import com.rastreocadena.rastreocadenasuministros.repositorio.AbastecimientoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbastecimientoServicio {

    @Autowired
    private AbastecimientoRepositorio abastecimientoRepositorio;

    public Abastecimiento guardarAbastecimiento(Abastecimiento abastecimiento) {
        return abastecimientoRepositorio.save(abastecimiento);
    }

    public List<Abastecimiento> listarAbastecimientos(Integer tipoAbastecimiento) {
        if (tipoAbastecimiento != null) {
            // Filtrar por tipo de abastecimiento
            return abastecimientoRepositorio.findByTipoAbastecimiento(tipoAbastecimiento);
        } else {
            // Listar todos los abastecimientos
            return abastecimientoRepositorio.findAll();
        }
    }
}
