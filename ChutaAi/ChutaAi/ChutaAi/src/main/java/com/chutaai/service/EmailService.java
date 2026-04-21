package com.chutaai.service;

import javax.enterprise.context.ApplicationScoped;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@ApplicationScoped
public class EmailService {


    private final String remetente = "chutaaibsi@gmail.com";
    private final String senhaApp = "ADICIONAR SENHA APP";

    public void enviarEmailRecuperacao(String destinatario, String novaSenha) throws MessagingException {
        // Configurações do servidor do Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // Autenticação
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetente, senhaApp);
            }
        });

        // Montando o e-mail
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(remetente));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject("Recuperação de Senha - CHUTA AÍ!");

        String corpoHtml = "<h2 style='color: #d32f2f;'>⚽ CHUTA AÍ!</h2>"
                + "<p>Olá,</p>"
                + "<p>Você solicitou a recuperação de senha do seu Bolão.</p>"
                + "<p>Sua nova senha provisória é: <strong>" + novaSenha + "</strong></p>"
                + "<p>Recomendamos que você altere essa senha assim que fizer o login.</p>"
                + "<br><p>Boa sorte nos palpites!</p>";

        message.setContent(corpoHtml, "text/html; charset=utf-8");

        Transport.send(message);
        System.out.println(">>> E-mail de recuperação enviado com sucesso para: " + destinatario);
    }
}
