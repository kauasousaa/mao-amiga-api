package com.maoamiga.api.controller;

import com.maoamiga.api.dto.DoacaoRequestDTO;
import com.maoamiga.api.dto.DoacaoResponseDTO;
import com.maoamiga.api.service.DoacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/doacoes")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Doações", description = "CRUD de doações")
public class DoacaoController {

    private final DoacaoService doacaoService;

    @GetMapping("/filtro")
    @Operation(summary = "Busca doações pelo tipo do item, da mais nova para a mais antiga")
    @ApiResponse(responseCode = "200", description = "Página de doações")
    public ResponseEntity<Page<DoacaoResponseDTO>> filtrar(
            @RequestParam(required = false) String filtro,
            @PageableDefault(size = 10, sort = "criadoEm", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(doacaoService.filtrar(filtro, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma doação pelo id")
    @ApiResponse(responseCode = "200", description = "Doação encontrada")
    @ApiResponse(responseCode = "404", description = "Doação não encontrada")
    public ResponseEntity<DoacaoResponseDTO> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(doacaoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastra uma doação")
    @ApiResponse(responseCode = "201", description = "Doação cadastrada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<DoacaoResponseDTO> cadastrar(@Valid @RequestBody DoacaoRequestDTO request) {
        DoacaoResponseDTO criada = doacaoService.cadastrar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criada.id())
                .toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma doação")
    @ApiResponse(responseCode = "200", description = "Doação atualizada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Doação não encontrada")
    public ResponseEntity<DoacaoResponseDTO> atualizar(@PathVariable UUID id,
                                                       @Valid @RequestBody DoacaoRequestDTO request) {
        return ResponseEntity.ok(doacaoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma doação")
    @ApiResponse(responseCode = "204", description = "Doação excluída")
    @ApiResponse(responseCode = "404", description = "Doação não encontrada")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        doacaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
