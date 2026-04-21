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

    private String email;
    private String senha;
    private Usuario usuarioLogado;

    @Inject
    private UsuarioService usuarioService;

    public String login() {
        Usuario usuario = usuarioService.autenticarLogin(email, senha);

        if (usuario != null) {
            this.usuarioLogado = usuario;
            return "partidas.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acesso Negado", "E-mail ou senha incorretos!"));
            return null;
        }
    }

    public String logout() {
        this.usuarioLogado = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index.xhtml?faces-redirect=true";
    }

    // Getters e Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Usuario getUsuarioLogado() { return usuarioLogado; }
    public boolean isLogado() { return usuarioLogado != null; }
}