package com.maoamiga.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record DoacaoRequestDTO(
        @NotBlank(message = "tipo do item é obrigatório")
        @Size(max = 120, message = "tipo do item deve ter no máximo 120 caracteres")
        String tipoItem,

        @NotNull(message = "quantidade é obrigatória")
        @Positive(message = "quantidade deve ser maior que zero")
        Integer quantidade,

        @NotBlank(message = "ponto de destino é obrigatório")
        @Size(max = 120, message = "ponto de destino deve ter no máximo 120 caracteres")
        String pontoDestino
) {

    public DoacaoRequestDTO {
        tipoItem = normalizar(tipoItem);
        pontoDestino = normalizar(pontoDestino);
    }

    private static String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
