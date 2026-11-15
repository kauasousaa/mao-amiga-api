package com.maoamiga.api.exception;

import java.util.UUID;

public class DoacaoNaoEncontradaException extends RuntimeException {

    public DoacaoNaoEncontradaException(UUID id) {
        super("Doação não encontrada: " + id);
    }
}
