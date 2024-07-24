package com.rastreocadena.rastreocadenasuministros.repositorio;

import com.rastreocadena.rastreocadenasuministros.modelo.Etapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtapaRepositorio extends JpaRepository<Etapa, Long> {
}
