package com.chutaai.controller;

import com.chutaai.model.entity.Usuario;
import com.chutaai.service.UsuarioService;
import com.chutaai.util.PasswordUtil;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class PerfilController implements Serializable {

    @Inject
    private LoginController loginController;

    @Inject
    private UsuarioService usuarioService;

    private Usuario usuarioEdicao;

    // Campos exclusivos para a troca de senha na tela
    private String senhaAtual;
    private String novaSenha;
    private String confirmarSenha;

    @PostConstruct
    public void init() {
        // Quando a tela abre, busca o usuário logado lá do banco para garantir que os dados estão frescos
        if (loginController.isLogado()) {
            usuarioEdicao = usuarioService.buscarPorEmail(loginController.getUsuarioLogado().getEmail());
        }
    }

    public void salvarPerfil() {
        try {
            usuarioService.atualizar(usuarioEdicao);

            // Atualiza o nome na sessão atual para a barra verde mudar na hora
            loginController.getUsuarioLogado().setNome(usuarioEdicao.getNome());

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Seu perfil foi atualizado!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não foi possível salvar os dados."));
        }
    }

    public void alterarSenha() {
        FacesContext context = FacesContext.getCurrentInstance();

        // 1ª Trava: As senhas novas precisam ser iguais
        if (!novaSenha.equals(confirmarSenha)) {
            context.addMessage("msgsSenha", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "A nova senha e a confirmação não batem."));
            return;
        }

        // 2ª Trava: A senha atual digitada precisa bater com a do banco (Usando nossa ferramenta oficial)
        if (!PasswordUtil.verificar(senhaAtual, usuarioEdicao.getSenha())) {
            context.addMessage("msgsSenha", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "A senha atual está incorreta."));
            return;
        }

        // 3º Passo: Se passou nas travas, criptografa a nova e salva!
        try {
            usuarioEdicao.setSenha(PasswordUtil.hash(novaSenha));
            usuarioService.atualizar(usuarioEdicao);

            // Limpa os campos da tela por segurança
            senhaAtual = "";
            novaSenha = "";
            confirmarSenha = "";

            context.addMessage("msgsSenha", new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Sua senha foi alterada com segurança!"));
        } catch (Exception e) {
            context.addMessage("msgsSenha", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Falha ao alterar a senha no banco."));
        }
    }

    // Getters e Setters
    public Usuario getUsuarioEdicao() { return usuarioEdicao; }
    public void setUsuarioEdicao(Usuario usuarioEdicao) { this.usuarioEdicao = usuarioEdicao; }

    public String getSenhaAtual() { return senhaAtual; }
    public void setSenhaAtual(String senhaAtual) { this.senhaAtual = senhaAtual; }

    public String getNovaSenha() { return novaSenha; }
    public void setNovaSenha(String novaSenha) { this.novaSenha = novaSenha; }

    public String getConfirmarSenha() { return confirmarSenha; }
    public void setConfirmarSenha(String confirmarSenha) { this.confirmarSenha = confirmarSenha; }
}