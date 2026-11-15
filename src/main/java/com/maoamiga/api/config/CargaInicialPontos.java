package com.maoamiga.api.config;

import com.maoamiga.api.entity.Ponto;
import com.maoamiga.api.entity.TipoPonto;
import com.maoamiga.api.repository.PontoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CargaInicialPontos implements ApplicationRunner {

    private final PontoRepository pontoRepository;

    @Override
    public void run(ApplicationArguments args) {
        carregarSeVazio();
    }

    public void carregarSeVazio() {
        if (pontoRepository.count() > 0) {
            return;
        }
        pontoRepository.saveAll(pontosDeGoiania());
    }

    private List<Ponto> pontosDeGoiania() {
        return List.of(
                ponto("Instituto Mão Amiga - Sede", "Rua 3, nº 245, Goiânia - GO", "Setor Central", "Seg a Sex, 8h às 17h", TipoPonto.COLETA),
                ponto("Ponto Praça Cívica", "Praça Cívica, s/n, Goiânia - GO", "Setor Central", "Seg a Sex, 8h às 18h", TipoPonto.COLETA),
                ponto("Ponto Parque Vaca Brava", "Avenida T-10, s/n, Goiânia - GO", "Setor Bueno", "Seg a Sáb, 9h às 18h", TipoPonto.COLETA),
                ponto("Ponto Feira Hippie", "Avenida Goiás, nº 1000, Goiânia - GO", "Setor Central", "Sáb e Dom, 8h às 14h", TipoPonto.COLETA),
                ponto("Centro de Distribuição Campinas", "Avenida 24 de Outubro, nº 500, Goiânia - GO", "Campinas", "Seg a Sex, 8h às 17h", TipoPonto.DISTRIBUICAO),
                ponto("Ponto PUC Goiás", "Avenida Universitária, nº 1440, Goiânia - GO", "Setor Universitário", "Seg a Sex, 8h às 20h", TipoPonto.COLETA),
                ponto("Ponto UFG Samambaia", "Avenida Esperança, s/n, Goiânia - GO", "Campus Samambaia", "Seg a Sex, 9h às 17h", TipoPonto.COLETA),
                ponto("Ponto Parque Areião", "Avenida Ricardo Paranhos, s/n, Goiânia - GO", "Setor Pedro Ludovico", "Ter a Sáb, 8h às 16h", TipoPonto.DISTRIBUICAO),
                ponto("Ponto Terminal Praça da Bíblia", "Praça da Bíblia, s/n, Goiânia - GO", "Setor Leste Universitário", "Seg a Sex, 7h às 19h", TipoPonto.COLETA),
                ponto("Ponto Shopping Flamboyant", "Avenida Jamel Cecílio, nº 3300, Goiânia - GO", "Jardim Goiás", "Seg a Sáb, 10h às 22h", TipoPonto.DISTRIBUICAO)
        );
    }

    private Ponto ponto(String nome, String endereco, String bairro, String horario, TipoPonto tipo) {
        return Ponto.builder()
                .nome(nome)
                .endereco(endereco)
                .bairro(bairro)
                .horario(horario)
                .tipo(tipo)
                .build();
    }
}
