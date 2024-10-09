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
        // Verificamos si es Abastecimiento 2
        if (abastecimiento.getTipoAbastecimiento() == 2) {
            // Usamos los campos específicos de Abastecimiento 2
            abastecimiento.setTiempoProduccionAbastecimiento(abastecimiento.getTiempoProduccionAbastecimiento2());
            abastecimiento.setCapacidadMaximaAbastecimiento(abastecimiento.getCapacidadMaximaAbastecimiento2());
            abastecimiento.setPeriodoExpedicionAbastecimiento(abastecimiento.getPeriodoExpedicionAbastecimiento2());
        }
        return abastecimientoRepositorio.save(abastecimiento);
    }


    public List<Abastecimiento> listarAbastecimientos() {
        return abastecimientoRepositorio.findAll();
    }

    public List<Abastecimiento> listarAbastecimientosPorTipo(int tipoAbastecimiento) {
        return abastecimientoRepositorio.findByTipoAbastecimiento(tipoAbastecimiento);
    }
}
