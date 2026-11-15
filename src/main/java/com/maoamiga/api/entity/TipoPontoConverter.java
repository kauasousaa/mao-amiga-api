package com.maoamiga.api.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TipoPontoConverter implements AttributeConverter<TipoPonto, String> {

    @Override
    public String convertToDatabaseColumn(TipoPonto tipo) {
        return tipo == null ? null : tipo.getValor();
    }

    @Override
    public TipoPonto convertToEntityAttribute(String valor) {
        return TipoPonto.fromValor(valor);
    }
}
