package com.maoamiga.api.repository;

import com.maoamiga.api.entity.Ponto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PontoRepository extends JpaRepository<Ponto, UUID> {

    List<Ponto> findAllByOrderByNomeAsc();
}
