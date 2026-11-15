package com.maoamiga.api.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoPonto {

    COLETA("coleta"),
    DISTRIBUICAO("distribuição");

    private final String valor;

    TipoPonto(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static TipoPonto fromValor(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        String informado = valor.trim();
        for (TipoPonto tipo : values()) {
            if (tipo.valor.equalsIgnoreCase(informado)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("tipo deve ser 'coleta' ou 'distribuição'");
    }
}
