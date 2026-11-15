package com.maoamiga.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "ponto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ponto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 200)
    private String endereco;

    @Column(nullable = false, length = 80)
    private String bairro;

    @Column(nullable = false, length = 80)
    private String horario;

    @Convert(converter = TipoPontoConverter.class)
    @Column(nullable = false, length = 20)
    private TipoPonto tipo;
}
