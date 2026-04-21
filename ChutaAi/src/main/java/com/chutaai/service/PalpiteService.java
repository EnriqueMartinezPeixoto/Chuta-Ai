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

    @Inject
    private UsuarioService usuarioService; // Injetado para podermos atualizar o Ranking do usuário

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

    // ========================================================================
    // MÉTODOS NOVOS PARA PROCESSAMENTO DO FIM DE JOGO (ÁREA DO ADMIN)
    // ========================================================================

    public void processarPontosDaPartida(Partida partida) {
        // 1. Busca todos os palpites que a galera fez para esse jogo
        List<Palpite> palpitesDaPartida = palpiteRepository.buscarPorPartida(partida);

        for (Palpite p : palpitesDaPartida) {
            // Força o palpite a enxergar o placar real novo que acabou de ser salvo
            p.setPartida(partida);

            // Calcula os 5, 3 ou 0 pontos
            calcularPontuacao(p);

            // Atualiza o palpite no banco
            palpiteRepository.atualizar(p);

            // Recalcula o ranking do dono desse palpite
            recalcularPontosTotal(p.getUsuario());
        }
    }

    private void recalcularPontosTotal(Usuario usuario) {
        // Pega todos os palpites que este usuário já fez na vida
        List<Palpite> todosPalpites = listarPorUsuario(usuario);

        int total = 0;
        for (Palpite p : todosPalpites) {
            total += p.getPontos();
        }

        // Atualiza a coluna pontosTotal dele e salva no banco (Isso faz o Ranking funcionar!)
        usuario.setPontosTotal(total);
        usuarioService.atualizar(usuario);
    }
}