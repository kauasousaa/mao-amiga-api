package com.maoamiga.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErroResponse(
        int status,
        String erro,
        Map<String, String> campos
) {

    public ErroResponse(int status, String erro) {
        this(status, erro, Map.of());
    }
}
