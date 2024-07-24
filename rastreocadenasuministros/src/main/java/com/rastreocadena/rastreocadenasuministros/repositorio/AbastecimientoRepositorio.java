package com.rastreocadena.rastreocadenasuministros.repositorio;

import com.rastreocadena.rastreocadenasuministros.modelo.Abastecimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbastecimientoRepositorio extends JpaRepository<Abastecimiento, Long> {
}
