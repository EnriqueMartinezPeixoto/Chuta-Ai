package com.chutaai.service;

import com.chutaai.model.entity.Palpite;
import com.chutaai.model.entity.Partida;
import com.chutaai.model.entity.Usuario;
import com.chutaai.model.repository.PalpiteRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.List;

@Named
@ApplicationScoped
public class PalpiteService {

    @Inject
    private PalpiteRepository palpiteRepository;

    public void salvar(Palpite palpite) {
        // 1. Verificação de data: Não pode palpitar em jogo que já começou
        if (palpite.getPartida().getDataHora().before(new Date())) {
            throw new RuntimeException("O tempo acabou! Não é possível palpitar em jogos iniciados.");
        }

        // 2. Se a partida já tem resultado (ex: admin inseriu depois), calcula os pontos
        if (palpite.getPartida().getPlacarCasaReal() != null) {
            calcularPontuacao(palpite);
        }

        palpiteRepository.salvar(palpite);
    }

    public List<Palpite> listarPorUsuario(Usuario usuario) {
        return palpiteRepository.listarPorUsuario(usuario);
    }

    public Palpite buscarPorUsuarioEPartida(Usuario usuario, Partida partida) {
        return palpiteRepository.buscarPorUsuarioEPartida(usuario, partida);
    }

    /**
     * Lógica de Pontuação do CHUTA AÍ!
     * 5 Pontos: Placar Exato
     * 3 Pontos: Acertou vencedor/empate (mas errou placar)
     * 0 Pontos: Errou tudo
     */
    public void calcularPontuacao(Palpite palpite) {
        Partida partida = palpite.getPartida();

        if (partida.getPlacarCasaReal() == null || partida.getPlacarForaReal() == null) {
            palpite.setPontos(0);
            return;
        }

        int pCasa = palpite.getPalpiteCasa();
        int pFora = palpite.getPalpiteFora();
        int rCasa = partida.getPlacarCasaReal();
        int rFora = partida.getPlacarForaReal();

        // Regra 1: Placar em cheio!
        if (pCasa == rCasa && pFora == rFora) {
            palpite.setPontos(5);
        }
        // Regra 2: Acertou quem ganha ou se dá empate
        else if (Integer.compare(pCasa, pFora) == Integer.compare(rCasa, rFora)) {
            palpite.setPontos(3);
        }
        // Regra 3: Errou feio
        else {
            palpite.setPontos(0);
        }
    }

    public void editarPalpite(Palpite palpite) {
        // UC08: Só permite editar se o jogo não começou (Data atual < Data do jogo)
        if (palpite.getPartida().getDataHora().before(new Date())) {
            throw new RuntimeException("Jogo já iniciado. Não é possível alterar o palpite.");
        }
        palpiteRepository.atualizar(palpite);
    }

    // UC12: Para o gráfico de desempenho ou resumo
    public double calcularAproveitamento(Usuario u) {
        List<Palpite> palpites = listarPorUsuario(u);
        if (palpites.isEmpty()) return 0.0;

        long totalAcertosPlacar = palpites.stream().filter(p -> p.getPontos() == 5).count();
        return (double) totalAcertosPlacar / palpites.size() * 100;
    }
}