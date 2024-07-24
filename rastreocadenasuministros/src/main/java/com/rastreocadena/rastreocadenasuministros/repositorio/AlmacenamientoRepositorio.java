package com.rastreocadena.rastreocadenasuministros.repositorio;

import com.rastreocadena.rastreocadenasuministros.modelo.Almacenamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlmacenamientoRepositorio extends JpaRepository<Almacenamiento, Long> {
}
