package com.maoamiga.api.service;

import com.maoamiga.api.dto.PontoRequestDTO;
import com.maoamiga.api.dto.PontoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PontoService {

    List<PontoResponseDTO> listar();

    PontoResponseDTO buscarPorId(UUID id);

    PontoResponseDTO cadastrar(PontoRequestDTO request);

    PontoResponseDTO atualizar(UUID id, PontoRequestDTO request);

    void excluir(UUID id);
}
