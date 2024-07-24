package com.rastreocadena.rastreocadenasuministros.servicio;

import com.rastreocadena.rastreocadenasuministros.modelo.Etapa;
import com.rastreocadena.rastreocadenasuministros.repositorio.EtapaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtapaServicio {

    @Autowired
    private EtapaRepositorio etapaRepositorio;

    public Etapa guardarEtapa(Etapa etapa) {
        return etapaRepositorio.save(etapa);
    }

    public List<Etapa> listarEtapas() {
        return etapaRepositorio.findAll();
    }

    // Otros métodos según sea necesario
}
