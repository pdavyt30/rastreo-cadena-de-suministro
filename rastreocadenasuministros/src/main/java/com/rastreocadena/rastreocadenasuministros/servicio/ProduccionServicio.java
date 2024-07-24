package com.rastreocadena.rastreocadenasuministros.servicio;

import com.rastreocadena.rastreocadenasuministros.modelo.Produccion;
import com.rastreocadena.rastreocadenasuministros.repositorio.ProduccionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduccionServicio {

    @Autowired
    private ProduccionRepositorio produccionRepositorio;

    public Produccion guardarProduccion(Produccion produccion) {
        return produccionRepositorio.save(produccion);
    }

    public List<Produccion> listarProducciones() {
        return produccionRepositorio.findAll();
    }

    public Optional<Produccion> obtenerProduccionPorId(Long id) {
        return produccionRepositorio.findById(id);
    }
}
