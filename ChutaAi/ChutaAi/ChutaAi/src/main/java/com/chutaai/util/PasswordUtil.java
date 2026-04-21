package com.chutaai.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hash(String senha) {
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("A senha não pode ser vazia");
        }
        return BCrypt.hashpw(senha, BCrypt.gensalt(12));
    }

    public static boolean verificar(String senhaDigitada, String hashArmazenado) {
        if (senhaDigitada == null || hashArmazenado == null) {
            return false;
        }
        return BCrypt.checkpw(senhaDigitada, hashArmazenado);
    }
}