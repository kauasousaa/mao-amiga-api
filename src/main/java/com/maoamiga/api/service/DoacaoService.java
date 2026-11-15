package com.maoamiga.api.service;

import com.maoamiga.api.dto.DoacaoRequestDTO;
import com.maoamiga.api.dto.DoacaoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DoacaoService {

    Page<DoacaoResponseDTO> filtrar(String filtro, Pageable pageable);

    DoacaoResponseDTO buscarPorId(UUID id);

    DoacaoResponseDTO cadastrar(DoacaoRequestDTO request);

    DoacaoResponseDTO atualizar(UUID id, DoacaoRequestDTO request);

    void excluir(UUID id);
}
