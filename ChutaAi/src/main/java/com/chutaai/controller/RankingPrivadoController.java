package com.chutaai.controller;

import com.chutaai.model.entity.Bolao;
import com.chutaai.model.entity.Usuario;
import com.chutaai.service.BolaoService;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class RankingPrivadoController implements Serializable {

    @Inject
    private BolaoService bolaoService;

    @Inject
    private LoginController loginController;

    private Long bolaoId;
    private Bolao bolao;
    private List<Usuario> rankingParticipantes;

    public void carregarRanking() {
        try {
            bolao = bolaoService.buscarPorId(bolaoId);

            if (bolao == null) {
                redirecionarParaBoloes("Bolão não encontrado.");
                return;
            }

            Usuario eu = loginController.getUsuarioLogado();

            // A NOVA TRAVA DE SEGURANÇA: Compara pelo ID (infalível!)
            boolean temPermissao = bolao.getParticipantes().stream()
                    .anyMatch(participante -> participante.getId().equals(eu.getId()));

            if (!temPermissao) {
                redirecionarParaBoloes("Você não tem permissão para ver este bolão.");
                return;
            }

            // Copia a lista de participantes e ORDENA por pontos (do maior pro menor)
            rankingParticipantes = new ArrayList<>(bolao.getParticipantes());
            rankingParticipantes.sort((u1, u2) -> Integer.compare(u2.getPontosTotal(), u1.getPontosTotal()));

        } catch (Exception e) {
            redirecionarParaBoloes("Erro ao carregar o ranking.");
        }
    }

    private void redirecionarParaBoloes(String mensagemErro) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acesso Negado", mensagemErro));

            FacesContext.getCurrentInstance().getExternalContext().redirect("bolao.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retorna TRUE se o usuário logado for o dono do bolão (Usado para mostrar/esconder os botões na tela)
    public boolean isSouDono() {
        if (bolao == null || loginController.getUsuarioLogado() == null) return false;
        return bolao.getDono().getId().equals(loginController.getUsuarioLogado().getId());
    }

    // Ação do botão da lixeirinha na tabela
    public void expulsarParticipante(Usuario participante) {
        try {
            bolaoService.removerParticipante(bolao, participante, loginController.getUsuarioLogado());

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Removido", participante.getNome() + " foi retirado do bolão."));

            carregarRanking(); // Recarrega a tabela na hora

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
        }
    }

    // Ação do botão Vermelho Gigante de encerrar
    public void encerrarBolao() {
        try {
            bolaoService.excluirBolao(bolao, loginController.getUsuarioLogado());

            // Joga o usuário de volta pra tela de "Meus Bolões" e avisa que foi excluído
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Bolão Encerrado", "O bolão foi excluído com sucesso."));

            FacesContext.getCurrentInstance().getExternalContext().redirect("bolao.xhtml");

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
        }
    }

    // Getters e Setters
    public Long getBolaoId() { return bolaoId; }
    public void setBolaoId(Long bolaoId) { this.bolaoId = bolaoId; }
    public Bolao getBolao() { return bolao; }
    public List<Usuario> getRankingParticipantes() { return rankingParticipantes; }
}