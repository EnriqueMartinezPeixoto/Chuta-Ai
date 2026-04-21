package com.chutaai.controller;

import com.chutaai.model.entity.Usuario;
import com.chutaai.service.UsuarioService;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginController implements Serializable {

    // CORREÇÃO: Usando o String padrão do Java
    private String email;
    private String senha;
    private Usuario usuarioLogado;

    @Inject
    private UsuarioService usuarioService;

    public String login() {
        // Chama o método robusto que criamos no Service
        Usuario usuario = usuarioService.autenticarLogin(email, senha);

        if (usuario != null) {
            this.usuarioLogado = usuario;
            // Tudo certo! Vai para a tela de partidas do JSF
            return "partidas.xhtml?faces-redirect=true";
        } else {
            // Envia a mensagem de erro para a tela
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acesso Negado", "E-mail ou senha incorretos!"));
            return null; // Fica na mesma tela de login para ele tentar de novo
        }
    }

    public String logout() {
        this.usuarioLogado = null;
        // Destrói a sessão oficial do servidor por segurança
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        // Volta para a tela inicial
        return "index.xhtml?faces-redirect=true";
    }

    // Getters e Setters ajustados
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Usuario getUsuarioLogado() { return usuarioLogado; }
    public boolean isLogado() { return usuarioLogado != null; }
}