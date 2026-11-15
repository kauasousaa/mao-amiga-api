package com.maoamiga.api.controller;

import com.maoamiga.api.config.CargaInicialPontos;
import com.maoamiga.api.repository.PontoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PontoControllerTest {

    private static final String PONTOS = "/api/v1/pontos";

    private static final String CORPO_VALIDO = """
            {
              "nome": "Ponto Setor Oeste",
              "endereco": "Rua 10, nº 20, Goiânia - GO",
              "bairro": "Setor Oeste",
              "horario": "Seg a Sex, 8h às 17h",
              "tipo": "coleta"
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PontoRepository pontoRepository;

    @Autowired
    private CargaInicialPontos cargaInicialPontos;

    @Test
    void listaPontosIniciaisDeGoiania() throws Exception {
        mockMvc.perform(get(PONTOS).header("Origin", "http://localhost:19006"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*"))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(8))))
                .andExpect(jsonPath("$[0].nome").exists())
                .andExpect(jsonPath("$[0].endereco", containsString("Goiânia")))
                .andExpect(jsonPath("$[?(@.bairro == 'Setor Bueno')]").exists())
                .andExpect(jsonPath("$[?(@.tipo == 'coleta')]").exists())
                .andExpect(jsonPath("$[?(@.tipo == 'distribuição')]").exists());
    }

    @Test
    void naoDuplicaCargaInicial() {
        long antes = pontoRepository.count();

        cargaInicialPontos.carregarSeVazio();

        org.junit.jupiter.api.Assertions.assertEquals(antes, pontoRepository.count());
        org.junit.jupiter.api.Assertions.assertTrue(antes >= 25);
    }

    @Test
    void cadastraBuscaAtualizaEExclui() throws Exception {
        String resposta = mockMvc.perform(post(PONTOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CORPO_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Ponto Setor Oeste"))
                .andExpect(jsonPath("$.tipo").value("coleta"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = resposta.replaceAll("(?s).*\"id\"\\s*:\\s*\"([^\"]+)\".*", "$1");

        mockMvc.perform(get(PONTOS + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bairro").value("Setor Oeste"));

        String atualizado = """
                {
                  "nome": "Ponto Setor Oeste Atualizado",
                  "endereco": "Rua 10, nº 20, Goiânia - GO",
                  "bairro": "Setor Oeste",
                  "horario": "Seg a Sex, 9h às 18h",
                  "tipo": "distribuição"
                }
                """;

        mockMvc.perform(put(PONTOS + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Ponto Setor Oeste Atualizado"))
                .andExpect(jsonPath("$.tipo").value("distribuição"));

        mockMvc.perform(delete(PONTOS + "/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(PONTOS + "/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void idInexistenteResponde404() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get(PONTOS + "/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        mockMvc.perform(put(PONTOS + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CORPO_VALIDO))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void campoObrigatorioVazioResponde400ENaoGrava() throws Exception {
        long antes = pontoRepository.count();
        String invalido = """
                {
                  "nome": "   ",
                  "endereco": "",
                  "bairro": "",
                  "horario": "",
                  "tipo": "coleta"
                }
                """;

        mockMvc.perform(post(PONTOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.campos.nome").exists())
                .andExpect(jsonPath("$.campos.endereco").exists());

        org.junit.jupiter.api.Assertions.assertEquals(antes, pontoRepository.count());
    }

    @Test
    void edicaoComCampoVazioResponde400ENaoAltera() throws Exception {
        String criado = mockMvc.perform(post(PONTOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CORPO_VALIDO))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String id = criado.replaceAll("(?s).*\"id\"\\s*:\\s*\"([^\"]+)\".*", "$1");

        String invalido = """
                {
                  "nome": "",
                  "endereco": "Rua 10, nº 20, Goiânia - GO",
                  "bairro": "Setor Oeste",
                  "horario": "Seg a Sex, 8h às 17h",
                  "tipo": "coleta"
                }
                """;

        mockMvc.perform(put(PONTOS + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalido))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(PONTOS + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ponto Setor Oeste"));
    }

    @Test
    void tipoInvalidoResponde400() throws Exception {
        long antes = pontoRepository.count();
        String invalido = """
                {
                  "nome": "Ponto inválido",
                  "endereco": "Rua 1, Goiânia - GO",
                  "bairro": "Setor Central",
                  "horario": "Seg a Sex, 8h às 17h",
                  "tipo": "outro"
                }
                """;

        mockMvc.perform(post(PONTOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("tipo deve ser 'coleta' ou 'distribuição'"));

        org.junit.jupiter.api.Assertions.assertEquals(antes, pontoRepository.count());
    }

    @Test
    void filtraPontosComPaginacao() throws Exception {
        mockMvc.perform(get(PONTOS + "/filtro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(10)))
                .andExpect(jsonPath("$.content[0].nome").value("Centro de Distribuição Campinas"))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.totalElements", greaterThanOrEqualTo(25)));

        mockMvc.perform(get(PONTOS + "/filtro").param("sort", "bairro,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].bairro").value("Campinas"))
                .andExpect(jsonPath("$.content[1].bairro").value("Campus Samambaia"));

        mockMvc.perform(get(PONTOS + "/filtro").param("filtro", "BuEnO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].nome").value("Ponto Parque Vaca Brava"))
                .andExpect(jsonPath("$.content[0].bairro").value("Setor Bueno"))
                .andExpect(jsonPath("$.content[1].bairro").value("Setor Bueno"))
                .andExpect(jsonPath("$.content[2].bairro").value("Setor Bueno"));

        mockMvc.perform(get(PONTOS + "/filtro").param("filtro", "vaca"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].nome").value("Ponto Parque Vaca Brava"));

        mockMvc.perform(get(PONTOS + "/filtro").param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(10)))
                .andExpect(jsonPath("$.page.number").value(1))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.content[0].nome").value(not("Centro de Distribuição Campinas")));
    }
}
