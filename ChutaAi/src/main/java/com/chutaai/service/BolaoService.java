package com.chutaai.service;

import com.chutaai.model.entity.Bolao;
import com.chutaai.model.entity.Usuario;
import com.chutaai.model.repository.BolaoRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.UUID;

@Named
@ApplicationScoped
public class BolaoService {

    @Inject
    private BolaoRepository bolaoRepository;

    // Função que cria o bolão, gera o código e já coloca o dono como 1º participante
    public void criarBolao(Bolao bolao, Usuario dono) {
        bolao.setDono(dono);

        // Gera um código aleatório de 6 letras/números (Ex: 8F3A2B)
        String codigoGerado = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        bolao.setCodigoAcesso(codigoGerado);

        // O dono entra automaticamente no próprio bolão
        bolao.getParticipantes().add(dono);

        bolaoRepository.salvar(bolao);
    }

    // Função para um usuário entrar em um bolão usando o código
    public void entrarNoBolao(String codigo, Usuario usuario) {
        Bolao bolao = bolaoRepository.buscarPorCodigo(codigo.toUpperCase().trim());

        if (bolao == null) {
            throw new RuntimeException("Código inválido! Nenhum bolão encontrado.");
        }

        // Verifica se o usuário já está no bolão para não adicionar duplicado
        if (bolao.getParticipantes().contains(usuario)) {
            throw new RuntimeException("Você já está participando deste bolão!");
        }

        bolao.getParticipantes().add(usuario);
        bolaoRepository.salvar(bolao);
    }

    public List<Bolao> listarPorUsuario(Usuario usuario) {
        return bolaoRepository.listarPorUsuario(usuario);
    }

    public Bolao buscarPorId(Long id) {
        return bolaoRepository.buscarPorId(id);
    }
    public void removerParticipante(Bolao bolao, Usuario participanteARemover, Usuario donoRequisitante) {
        // 1ª Trava: Confere se quem está pedindo a expulsão é o dono do bolão
        if (!bolao.getDono().getId().equals(donoRequisitante.getId())) {
            throw new RuntimeException("Apenas o criador do bolão pode remover participantes.");
        }
        // 2ª Trava: O dono não pode expulsar a si mesmo por engano
        if (bolao.getDono().getId().equals(participanteARemover.getId())) {
            throw new RuntimeException("Você não pode remover a si mesmo do próprio bolão.");
        }

        // Remove o usuário da lista e salva no banco
        bolao.getParticipantes().removeIf(u -> u.getId().equals(participanteARemover.getId()));
        bolaoRepository.atualizar(bolao);
    }

    public void excluirBolao(Bolao bolao, Usuario donoRequisitante) {
        if (!bolao.getDono().getId().equals(donoRequisitante.getId())) {
            throw new RuntimeException("Apenas o criador pode encerrar este bolão.");
        }
        bolaoRepository.excluir(bolao.getId());
    }
}