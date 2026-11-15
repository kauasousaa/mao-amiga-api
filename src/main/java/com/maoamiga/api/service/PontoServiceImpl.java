package com.maoamiga.api.service;

import com.maoamiga.api.dto.PontoRequestDTO;
import com.maoamiga.api.dto.PontoResponseDTO;
import com.maoamiga.api.entity.Ponto;
import com.maoamiga.api.exception.PontoNaoEncontradoException;
import com.maoamiga.api.repository.PontoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PontoServiceImpl implements PontoService {

    private final PontoRepository pontoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PontoResponseDTO> listar() {
        return pontoRepository.findAllByOrderByNomeAsc().stream()
                .map(PontoResponseDTO::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PontoResponseDTO> filtrar(String filtro, Pageable pageable) {
        String termo = filtro == null ? "" : filtro.trim();
        Page<Ponto> pagina = termo.isEmpty()
                ? pontoRepository.findAll(pageable)
                : pontoRepository.buscarPorNomeOuBairro(termo, pageable);
        return pagina.map(PontoResponseDTO::from);
    }

    @Override
    @Transactional(readOnly = true)
    public PontoResponseDTO buscarPorId(UUID id) {
        return PontoResponseDTO.from(buscarEntidade(id));
    }

    @Override
    @Transactional
    public PontoResponseDTO cadastrar(PontoRequestDTO request) {
        Ponto ponto = Ponto.builder()
                .nome(request.nome())
                .endereco(request.endereco())
                .bairro(request.bairro())
                .horario(request.horario())
                .tipo(request.tipo())
                .build();
        return PontoResponseDTO.from(pontoRepository.save(ponto));
    }

    @Override
    @Transactional
    public PontoResponseDTO atualizar(UUID id, PontoRequestDTO request) {
        Ponto ponto = buscarEntidade(id);
        ponto.setNome(request.nome());
        ponto.setEndereco(request.endereco());
        ponto.setBairro(request.bairro());
        ponto.setHorario(request.horario());
        ponto.setTipo(request.tipo());
        return PontoResponseDTO.from(pontoRepository.save(ponto));
    }

    @Override
    @Transactional
    public void excluir(UUID id) {
        Ponto ponto = buscarEntidade(id);
        pontoRepository.delete(ponto);
    }

    private Ponto buscarEntidade(UUID id) {
        return pontoRepository.findById(id)
                .orElseThrow(() -> new PontoNaoEncontradoException(id));
    }
}
