package com.maoamiga.api.exception;

import com.maoamiga.api.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(PontoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> pontoNaoEncontrado(PontoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> dadosInvalidos(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(erro -> campos.putIfAbsent(erro.getField(), erro.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(new ErroResponse(HttpStatus.BAD_REQUEST.value(), "Dados inválidos", campos));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> corpoInvalido(HttpMessageNotReadableException ex) {
        String mensagem = "Corpo da requisição inválido ou incompleto";
        Throwable causa = ex.getMostSpecificCause();
        if (causa instanceof IllegalArgumentException illegal && illegal.getMessage() != null) {
            mensagem = illegal.getMessage();
        }
        return ResponseEntity.badRequest()
                .body(new ErroResponse(HttpStatus.BAD_REQUEST.value(), mensagem));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> identificadorInvalido(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest()
                .body(new ErroResponse(HttpStatus.BAD_REQUEST.value(), "Identificador inválido"));
    }
}
