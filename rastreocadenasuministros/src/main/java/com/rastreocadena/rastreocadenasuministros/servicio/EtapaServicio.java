package com.rastreocadena.rastreocadenasuministros.servicio;

import com.rastreocadena.rastreocadenasuministros.modelo.Abastecimiento;
import com.rastreocadena.rastreocadenasuministros.modelo.Produccion;
import com.rastreocadena.rastreocadenasuministros.modelo.Almacenamiento;
import com.rastreocadena.rastreocadenasuministros.repositorio.AbastecimientoRepositorio;
import com.rastreocadena.rastreocadenasuministros.repositorio.ProduccionRepositorio;
import com.rastreocadena.rastreocadenasuministros.repositorio.AlmacenamientoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class EtapaServicio {

    private static final Logger logger = LoggerFactory.getLogger(EtapaServicio.class);

    @Autowired
    private AbastecimientoRepositorio abastecimientoRepositorio;

    @Autowired
    private ProduccionRepositorio produccionRepositorio;

    @Autowired
    private AlmacenamientoRepositorio almacenamientoRepositorio;

    public Abastecimiento guardarAbastecimiento(Abastecimiento abastecimiento) {
        return abastecimientoRepositorio.save(abastecimiento);
    }

    public Produccion guardarProduccion(Produccion produccion) {
        return produccionRepositorio.save(produccion);
    }

    public Almacenamiento guardarAlmacenamiento(Almacenamiento almacenamiento) {
        return almacenamientoRepositorio.save(almacenamiento);
    }

    public List<Abastecimiento> listarAbastecimientos() {
        return abastecimientoRepositorio.findAll();
    }

    public List<Produccion> listarProducciones() {
        return produccionRepositorio.findAll();
    }

    public List<Almacenamiento> listarAlmacenamientos() {
        return almacenamientoRepositorio.findAll();
    }
}
