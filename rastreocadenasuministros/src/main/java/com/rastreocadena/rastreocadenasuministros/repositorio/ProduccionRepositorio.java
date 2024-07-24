package com.rastreocadena.rastreocadenasuministros.repositorio;

import com.rastreocadena.rastreocadenasuministros.modelo.Produccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduccionRepositorio extends JpaRepository<Produccion, Long> {
}
