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
        List<Ponto> faltantes = pontosDeGoiania().stream()
                .filter(ponto -> !pontoRepository.existsByNome(ponto.getNome()))
                .toList();
        if (!faltantes.isEmpty()) {
            pontoRepository.saveAll(faltantes);
        }
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
                ponto("Ponto Shopping Flamboyant", "Avenida Jamel Cecílio, nº 3300, Goiânia - GO", "Jardim Goiás", "Seg a Sáb, 10h às 22h", TipoPonto.DISTRIBUICAO),
                ponto("Ponto Bosque dos Buritis", "Avenida Assis Chateaubriand, s/n, Goiânia - GO", "Setor Oeste", "Seg a Dom, 6h às 18h", TipoPonto.COLETA),
                ponto("Ponto Lago das Rosas", "Rua 2, s/n, Goiânia - GO", "Setor Oeste", "Seg a Sex, 8h às 17h", TipoPonto.COLETA),
                ponto("Ponto Shopping Bougainville", "Avenida T-10, nº 1300, Goiânia - GO", "Setor Bueno", "Seg a Sáb, 10h às 22h", TipoPonto.DISTRIBUICAO),
                ponto("Ponto Mutirama", "Avenida Contorno, s/n, Goiânia - GO", "Setor Central", "Ter a Dom, 9h às 17h", TipoPonto.COLETA),
                ponto("Ponto Mercado Central", "Avenida Paranaíba, s/n, Goiânia - GO", "Setor Central", "Seg a Sáb, 7h às 18h", TipoPonto.DISTRIBUICAO),
                ponto("Ponto Serra Dourada", "Avenida Fued José Sebba, s/n, Goiânia - GO", "Jardim Goiás", "Seg a Sex, 8h às 17h", TipoPonto.COLETA),
                ponto("Ponto Teatro Goiânia", "Avenida Tocantins, s/n, Goiânia - GO", "Setor Central", "Seg a Sex, 9h às 21h", TipoPonto.COLETA),
                ponto("Ponto Assembleia Legislativa", "Avenida Olinda, s/n, Goiânia - GO", "Setor Pedro Ludovico", "Seg a Sex, 8h às 18h", TipoPonto.COLETA),
                ponto("Ponto Hospital das Clínicas", "Primeira Avenida, s/n, Goiânia - GO", "Setor Leste Universitário", "Todos os dias, 24h", TipoPonto.DISTRIBUICAO),
                ponto("Ponto Praça do Sol", "Avenida T-63, s/n, Goiânia - GO", "Setor Bueno", "Seg a Sáb, 8h às 18h", TipoPonto.COLETA),
                ponto("Ponto Jardim América", "Rua C-200, nº 40, Goiânia - GO", "Jardim América", "Seg a Sex, 8h às 17h", TipoPonto.DISTRIBUICAO),
                ponto("Ponto Setor Coimbra", "Avenida Perimetral Norte, nº 500, Goiânia - GO", "Setor Coimbra", "Seg a Sex, 8h às 17h", TipoPonto.COLETA),
                ponto("Ponto Parque Cascavel", "Rua 90, s/n, Goiânia - GO", "Setor Sul", "Seg a Dom, 8h às 18h", TipoPonto.COLETA),
                ponto("Ponto Oscar Niemeyer", "Avenida Deputado Jamel Cecílio, s/n, Goiânia - GO", "Jardim Goiás", "Ter a Dom, 9h às 18h", TipoPonto.DISTRIBUICAO),
                ponto("Ponto Terminal Isidória", "Avenida L, s/n, Goiânia - GO", "Jardim Novo Mundo", "Seg a Sex, 6h às 20h", TipoPonto.COLETA),
                ponto("Ponto Cidade Jardim", "Avenida C-107, nº 200, Goiânia - GO", "Cidade Jardim", "Seg a Sex, 8h às 17h", TipoPonto.COLETA)
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
