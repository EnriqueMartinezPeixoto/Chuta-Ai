package com.chutaai.controller;

import com.chutaai.model.entity.Usuario;
import com.chutaai.service.UsuarioService;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class UsuarioController implements Serializable {

    // A tela de cadastro (JSF) vai preencher os dados (nome, e-mail, senha) dentro deste objeto.
    private Usuario novoUsuario = new Usuario();

    @Inject
    private UsuarioService usuarioService;

    public String cadastrar() {
        try {
            // 1. Verifica se o e-mail já existe no banco antes de tentar salvar
            Usuario usuarioExistente = usuarioService.buscarPorEmail(novoUsuario.getEmail());

            if (usuarioExistente != null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Este e-mail já está cadastrado!"));
                return null; // Fica na mesma página
            }

            // 2. Se o e-mail for livre, chama o Service para proteger a senha com BCrypt e salvar no MySQL
            usuarioService.salvar(novoUsuario);

            // 3. Limpa o objeto para a tela ficar em branco de novo (opcional, mas boa prática)
            novoUsuario = new Usuario();

            // 4. Manda uma mensagem de sucesso para a tela de Login
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Conta criada com sucesso! Faça seu login."));

            // 5. Redireciona o usuário para a tela de login
            return "index.xhtml?faces-redirect=true";

        } catch (Exception e) {
            e.printStackTrace();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Ocorreu um erro ao criar a conta."));
            return null;
        }
    }

    // Getters e Setters
    public Usuario getNovoUsuario() {
        return novoUsuario;
    }

    public void setNovoUsuario(Usuario novoUsuario) {
        this.novoUsuario = novoUsuario;
    }
}