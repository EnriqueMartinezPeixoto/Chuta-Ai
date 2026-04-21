package com.chutaai.controller;

import com.chutaai.model.entity.Palpite;
import com.chutaai.model.entity.Partida;
import com.chutaai.service.PalpiteService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped; // <-- IMPORTANTE: NOVO IMPORT DA MÁGICA
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class PalpiteController implements Serializable {

    @Inject
    private PalpiteService palpiteService;

    @Inject
    private LoginController loginController;

    private Palpite palpiteAtual = new Palpite();
    private List<Palpite> meusPalpites;
    private Partida partidaSelecionada;

    @PostConstruct
    public void init() {
        carregarMeusPalpites();
    }

    public void carregarMeusPalpites() {
        if (loginController.getUsuarioLogado() != null) {
            this.meusPalpites = palpiteService.listarPorUsuario(loginController.getUsuarioLogado());
        }
    }

    public void irParaPalpite(Partida partida) {
        try {
            if (loginController.getUsuarioLogado() == null) {

                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
                return;
            }

            if (partida == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Partida não encontrada."));
                return;
            }

            this.partidaSelecionada = partida;

            this.palpiteAtual = new Palpite();
            this.palpiteAtual.setPartida(partida);
            this.palpiteAtual.setUsuario(loginController.getUsuarioLogado());


            FacesContext.getCurrentInstance().getExternalContext().redirect("palpite_form.xhtml");

        } catch (Exception e) {
            System.out.println("Erro ao tentar redirecionar: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public String salvarPalpite() {
        // --- INÍCIO DA TRAVA DE SEGURANÇA ANTIFRAUDE ---
        java.util.Date agora = new java.util.Date();
        if (agora.after(this.palpiteAtual.getPartida().getDataHora())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Apostas Encerradas!", "O juiz já apitou! Esta partida já começou ou foi encerrada e não aceita mais palpites."));
            return null;
        }
        // --- FIM DA TRAVA DE SEGURANÇA ---

        try {
            palpiteService.salvar(palpiteAtual);

            carregarMeusPalpites();

            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "O seu palpite foi registado."));
            return "partidas?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar", e.getMessage()));
            return null;
        }
    }

    /**
     * UC08: Permite o usuário editar um palpite ANTES do jogo começar.
     */
    public void prepararEdicao(Palpite palpiteSalvo) {
        try {
            java.util.Date agora = new java.util.Date();
            if (agora.after(palpiteSalvo.getPartida().getDataHora())) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Apostas Encerradas!", "A bola já está rolando. Você não pode mais alterar este palpite."));
                return;
            }

            // Se o jogo ainda não começou, permite a edição
            this.palpiteAtual = palpiteSalvo;
            this.partidaSelecionada = palpiteSalvo.getPartida();

            FacesContext.getCurrentInstance().getExternalContext().redirect("palpite_form.xhtml");

        } catch (Exception e) {
            System.out.println("Erro ao tentar editar palpite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Getters e Setters
    public Palpite getPalpiteAtual() { return palpiteAtual; }
    public void setPalpiteAtual(Palpite palpiteAtual) { this.palpiteAtual = palpiteAtual; }
    public List<Palpite> getMeusPalpites() { return meusPalpites; }
    public Partida getPartidaSelecionada() { return partidaSelecionada; }
    public void setPartidaSelecionada(Partida partidaSelecionada) { this.partidaSelecionada = partidaSelecionada; }
}