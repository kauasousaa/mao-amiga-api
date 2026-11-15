package com.maoamiga.api.controller;

import com.maoamiga.api.repository.DoacaoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
class DoacaoControllerTest {

    private static final String DOACOES = "/api/v1/doacoes";
    private static final String FILTRO = DOACOES + "/filtro";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DoacaoRepository doacaoRepository;

    @Test
    void cadastraBuscaAtualizaEExcluiSemAlterarCriadoEm() throws Exception {
        MvcResult criado = mockMvc.perform(post(DOACOES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo("Roupa", 4, "Instituto Mão Amiga - Sede")))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.tipoItem").value("Roupa"))
                .andExpect(jsonPath("$.quantidade").value(4))
                .andExpect(jsonPath("$.pontoDestino").value("Instituto Mão Amiga - Sede"))
                .andExpect(jsonPath("$.criadoEm").exists())
                .andReturn();

        String corpo = criado.getResponse().getContentAsString();
        String id = extrair(corpo, "id");
        String criadoEm = extrair(corpo, "criadoEm");

        mockMvc.perform(get(DOACOES + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.criadoEm").value(criadoEm));

        mockMvc.perform(put(DOACOES + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo("Alimento", 2, "Ponto Parque Vaca Brava")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.tipoItem").value("Alimento"))
                .andExpect(jsonPath("$.quantidade").value(2))
                .andExpect(jsonPath("$.pontoDestino").value("Ponto Parque Vaca Brava"))
                .andExpect(jsonPath("$.criadoEm").value(criadoEm));

        mockMvc.perform(delete(DOACOES + "/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(DOACOES + "/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void idInexistenteNoGetENoPutResponde404() throws Exception {
        String id = UUID.randomUUID().toString();

        mockMvc.perform(get(DOACOES + "/" + id))
                .andExpect(status().isNotFound());

        mockMvc.perform(put(DOACOES + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo("Roupa", 1, "Instituto Mão Amiga - Sede")))
                .andExpect(status().isNotFound());

        assertEquals(0, doacaoRepository.count());
    }

    @Test
    void quantidadeZeroNegativaOuTipoVazioNaoGrava() throws Exception {
        long antes = doacaoRepository.count();

        mockMvc.perform(post(DOACOES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo("Roupa", 0, "Instituto Mão Amiga - Sede")))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(DOACOES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo("Roupa", -3, "Instituto Mão Amiga - Sede")))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(DOACOES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo("   ", 2, "Instituto Mão Amiga - Sede")))
                .andExpect(status().isBadRequest());

        assertEquals(antes, doacaoRepository.count());
    }

    @Test
    void filtraPorTipoDeItemDaMaisNovaParaAMaisAntiga() throws Exception {
        String idAntiga = idCriado(corpo("Roupa", 1, "Instituto Mão Amiga - Sede"));
        String idNova = idCriado(corpo("Cesta básica", 2, "Ponto Parque Vaca Brava"));

        doacaoRepository.findById(UUID.fromString(idAntiga)).orElseThrow()
                .setCriadoEm(LocalDateTime.of(2026, 1, 1, 8, 0));
        doacaoRepository.findById(UUID.fromString(idNova)).orElseThrow()
                .setCriadoEm(LocalDateTime.of(2026, 11, 15, 8, 0));
        doacaoRepository.flush();

        mockMvc.perform(get(FILTRO).param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(idNova))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.number").value(0));

        mockMvc.perform(get(FILTRO).param("filtro", "ROUPA").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(idAntiga))
                .andExpect(jsonPath("$.content[0].tipoItem").value("Roupa"));
    }

    private String idCriado(String json) throws Exception {
        MvcResult resultado = mockMvc.perform(post(DOACOES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();
        return extrair(resultado.getResponse().getContentAsString(), "id");
    }

    private static String corpo(String tipoItem, int quantidade, String pontoDestino) {
        return """
                {
                  "tipoItem": "%s",
                  "quantidade": %d,
                  "pontoDestino": "%s"
                }
                """.formatted(tipoItem, quantidade, pontoDestino);
    }

    private static String extrair(String json, String campo) {
        String chave = "\"" + campo + "\":\"";
        int inicio = json.indexOf(chave);
        if (inicio < 0) {
            throw new IllegalStateException("Campo ausente: " + campo);
        }
        inicio += chave.length();
        return json.substring(inicio, json.indexOf('"', inicio));
    }
}
