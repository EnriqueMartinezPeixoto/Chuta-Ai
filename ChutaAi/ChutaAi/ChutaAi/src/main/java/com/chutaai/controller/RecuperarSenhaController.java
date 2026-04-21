package com.chutaai.controller;

import com.chutaai.model.entity.Usuario;
import com.chutaai.service.UsuarioService;
import com.chutaai.service.EmailService;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Random;

@Named
@ViewScoped
public class RecuperarSenhaController implements Serializable {

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private EmailService emailService; // Injetando o serviço de e-mail

    private String emailInformado;

    public void recuperarSenha() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            Usuario usuario = usuarioService.buscarPorEmail(emailInformado);

            if (usuario == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Aviso", "Se este e-mail estiver cadastrado, as instruções serão enviadas."));
                return;
            }

            // Gera uma senha numérica de 6 dígitos (A que vai no e-mail)
            String novaSenha = String.format("%06d", new Random().nextInt(999999));

            // CRIPTOGRAFA a nova senha antes de salvar no banco!
            String senhaCriptografada = org.mindrot.jbcrypt.BCrypt.hashpw(novaSenha, org.mindrot.jbcrypt.BCrypt.gensalt());

            // Salva a senha embaralhada no banco
            usuario.setSenha(senhaCriptografada);
            usuarioService.atualizar(usuario);

            // Dispara o e-mail
            emailService.enviarEmailRecuperacao(usuario.getEmail(), novaSenha);

            // Mensagem de sucesso na tela
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sucesso", "Uma senha provisória foi enviada para o seu e-mail!"));

            this.emailInformado = ""; // Limpa o campo

        } catch (Exception e) {
            System.out.println("Erro crítico ao enviar e-mail: " + e.getMessage());
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro", "Falha ao enviar o e-mail de recuperação. Tente novamente mais tarde."));
        }
    }

    // Getter e Setter
    public String getEmailInformado() { return emailInformado; }
    public void setEmailInformado(String emailInformado) { this.emailInformado = emailInformado; }
}