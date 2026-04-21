package com.chutaai.controller;

import com.chutaai.model.entity.Bolao;
import com.chutaai.service.BolaoService;

import javax.annotation.PostConstruct;
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
public class BolaoController implements Serializable {

    @Inject
    private BolaoService bolaoService;

    @Inject
    private LoginController loginController;

    private Bolao novoBolao;
    private String codigoParaEntrar;
    private List<Bolao> meusBoloes;

    @PostConstruct
    public void init() {
        try {
            novoBolao = new Bolao();
            carregarMeusBoloes();
        } catch (Exception e) {
            // A NOSSA LUPA DE DETETIVE! Vai imprimir o erro verdadeiro no console.
            System.out.println("🚨 ERRO FATAL AO ABRIR TELA DE BOLÕES 🚨");
            e.printStackTrace();
        }
    }

    public void carregarMeusBoloes() {
        // TRAVA DE SEGURANÇA: Checa tudo antes de tentar ir no banco
        if (loginController != null && loginController.getUsuarioLogado() != null) {
            meusBoloes = bolaoService.listarPorUsuario(loginController.getUsuarioLogado());
        } else {
            // Se o usuário não estiver logado ou a sessão cair, cria uma lista vazia pra tela não dar Erro 500
            meusBoloes = new ArrayList<>();
        }
    }

    public void criarBolao() {
        try {
            bolaoService.criarBolao(novoBolao, loginController.getUsuarioLogado());

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Bolão '" + novoBolao.getNome() + "' criado! Compartilhe o código: " + novoBolao.getCodigoAcesso()));

            // Limpa o formulário e atualiza a tabela
            novoBolao = new Bolao();
            carregarMeusBoloes();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não foi possível criar o bolão."));
        }
    }

    public void entrarBolao() {
        try {
            bolaoService.entrarNoBolao(codigoParaEntrar, loginController.getUsuarioLogado());

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Você entrou no bolão!"));

            codigoParaEntrar = ""; // Limpa o campo
            carregarMeusBoloes(); // Atualiza a tabela na hora

        } catch (Exception e) {
            // Mostra o erro exato na tela (ex: "Você já está neste bolão" ou "Código inválido")
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção", e.getMessage()));
        }
    }

    // Getters e Setters
    public Bolao getNovoBolao() { return novoBolao; }
    public void setNovoBolao(Bolao novoBolao) { this.novoBolao = novoBolao; }
    public String getCodigoParaEntrar() { return codigoParaEntrar; }
    public void setCodigoParaEntrar(String codigoParaEntrar) { this.codigoParaEntrar = codigoParaEntrar; }
    public List<Bolao> getMeusBoloes() { return meusBoloes; }
}