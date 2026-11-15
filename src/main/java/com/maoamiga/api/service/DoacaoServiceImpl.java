package com.maoamiga.api.service;

import com.maoamiga.api.dto.DoacaoRequestDTO;
import com.maoamiga.api.dto.DoacaoResponseDTO;
import com.maoamiga.api.entity.Doacao;
import com.maoamiga.api.exception.DoacaoNaoEncontradaException;
import com.maoamiga.api.repository.DoacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoacaoServiceImpl implements DoacaoService {

    private final DoacaoRepository doacaoRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<DoacaoResponseDTO> filtrar(String filtro, Pageable pageable) {
        String termo = filtro == null ? "" : filtro.trim();
        Page<Doacao> pagina = termo.isEmpty()
                ? doacaoRepository.findAll(pageable)
                : doacaoRepository.buscarPorTipoItem(termo, pageable);
        return pagina.map(DoacaoResponseDTO::from);
    }

    @Override
    @Transactional(readOnly = true)
    public DoacaoResponseDTO buscarPorId(UUID id) {
        return DoacaoResponseDTO.from(buscarEntidade(id));
    }

    @Override
    @Transactional
    public DoacaoResponseDTO cadastrar(DoacaoRequestDTO request) {
        Doacao doacao = Doacao.builder()
                .tipoItem(request.tipoItem())
                .quantidade(request.quantidade())
                .pontoDestino(request.pontoDestino())
                .criadoEm(LocalDateTime.now())
                .build();
        return DoacaoResponseDTO.from(doacaoRepository.save(doacao));
    }

    @Override
    @Transactional
    public DoacaoResponseDTO atualizar(UUID id, DoacaoRequestDTO request) {
        Doacao doacao = buscarEntidade(id);
        doacao.setTipoItem(request.tipoItem());
        doacao.setQuantidade(request.quantidade());
        doacao.setPontoDestino(request.pontoDestino());
        return DoacaoResponseDTO.from(doacaoRepository.save(doacao));
    }

    @Override
    @Transactional
    public void excluir(UUID id) {
        Doacao doacao = buscarEntidade(id);
        doacaoRepository.delete(doacao);
    }

    private Doacao buscarEntidade(UUID id) {
        return doacaoRepository.findById(id)
                .orElseThrow(() -> new DoacaoNaoEncontradaException(id));
    }
}
