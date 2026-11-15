package com.maoamiga.api.controller;

import com.maoamiga.api.dto.PontoRequestDTO;
import com.maoamiga.api.dto.PontoResponseDTO;
import com.maoamiga.api.service.PontoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pontos")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Pontos", description = "CRUD de pontos de coleta e distribuição")
public class PontoController {

    private final PontoService pontoService;

    @GetMapping
    @Operation(summary = "Lista os pontos")
    @ApiResponse(responseCode = "200", description = "Lista de pontos")
    public ResponseEntity<List<PontoResponseDTO>> listar() {
        return ResponseEntity.ok(pontoService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um ponto pelo id")
    @ApiResponse(responseCode = "200", description = "Ponto encontrado")
    @ApiResponse(responseCode = "404", description = "Ponto não encontrado")
    public ResponseEntity<PontoResponseDTO> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(pontoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastra um ponto")
    @ApiResponse(responseCode = "201", description = "Ponto cadastrado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<PontoResponseDTO> cadastrar(@Valid @RequestBody PontoRequestDTO request) {
        PontoResponseDTO criado = pontoService.cadastrar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.id())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um ponto")
    @ApiResponse(responseCode = "200", description = "Ponto atualizado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Ponto não encontrado")
    public ResponseEntity<PontoResponseDTO> atualizar(@PathVariable UUID id,
                                                      @Valid @RequestBody PontoRequestDTO request) {
        return ResponseEntity.ok(pontoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um ponto")
    @ApiResponse(responseCode = "204", description = "Ponto excluído")
    @ApiResponse(responseCode = "404", description = "Ponto não encontrado")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        pontoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
