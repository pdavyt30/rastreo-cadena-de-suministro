package com.rastreocadena.rastreocadenasuministros.servicio;

import com.rastreocadena.rastreocadenasuministros.modelo.Almacenamiento;
import com.rastreocadena.rastreocadenasuministros.repositorio.AlmacenamientoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlmacenamientoServicio {

    @Autowired
    private AlmacenamientoRepositorio almacenamientoRepositorio;

    public Almacenamiento guardarAlmacenamiento(Almacenamiento almacenamiento) {
        return almacenamientoRepositorio.save(almacenamiento);
    }

    public List<Almacenamiento> listarAlmacenamientos() {
        return almacenamientoRepositorio.findAll();
    }

    public Optional<Almacenamiento> obtenerAlmacenamientoPorId(Long id) {
        return almacenamientoRepositorio.findById(id);
    }
}
