package com.maoamiga.api.service;

import com.maoamiga.api.dto.PontoRequestDTO;
import com.maoamiga.api.dto.PontoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PontoService {

    List<PontoResponseDTO> listar();

    Page<PontoResponseDTO> filtrar(String filtro, Pageable pageable);

    PontoResponseDTO buscarPorId(UUID id);

    PontoResponseDTO cadastrar(PontoRequestDTO request);

    PontoResponseDTO atualizar(UUID id, PontoRequestDTO request);

    void excluir(UUID id);
}
