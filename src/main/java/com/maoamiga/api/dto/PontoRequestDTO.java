package com.maoamiga.api.dto;

import com.maoamiga.api.entity.TipoPonto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PontoRequestDTO(
        @NotBlank(message = "nome é obrigatório")
        @Size(max = 120, message = "nome deve ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "endereço é obrigatório")
        @Size(max = 200, message = "endereço deve ter no máximo 200 caracteres")
        String endereco,

        @NotBlank(message = "bairro é obrigatório")
        @Size(max = 80, message = "bairro deve ter no máximo 80 caracteres")
        String bairro,

        @NotBlank(message = "horário é obrigatório")
        @Size(max = 80, message = "horário deve ter no máximo 80 caracteres")
        String horario,

        @NotNull(message = "tipo é obrigatório")
        TipoPonto tipo
) {

    public PontoRequestDTO {
        nome = normalizar(nome);
        endereco = normalizar(endereco);
        bairro = normalizar(bairro);
        horario = normalizar(horario);
    }

    private static String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
