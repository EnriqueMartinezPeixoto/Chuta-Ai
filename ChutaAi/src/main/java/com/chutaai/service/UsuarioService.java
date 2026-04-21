package com.chutaai.service;

import com.chutaai.model.entity.Usuario;
import com.chutaai.model.repository.UsuarioRepository;
import com.chutaai.util.PasswordUtil; // Importando o nosso utilitário de segurança
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@ApplicationScoped
public class UsuarioService {

    @Inject
    private UsuarioRepository usuarioRepository;

    // ---------------------------------------------------------
    // CADASTRAR USUÁRIO (Protegendo a senha antes de salvar)
    // ---------------------------------------------------------
    public void salvar(Usuario usuario) {

        // 1. Pega a senha em texto puro que veio da tela (ex: "123456")
        String senhaPlana = usuario.getSenha();

        // 2. Transforma em um Hash seguro e irreversível (ex: "$2a$12$x8S...")
        String senhaSegura = PasswordUtil.hash(senhaPlana);

        // 3. Devolve a senha segura para o objeto ANTES de mandar para o banco
        usuario.setSenha(senhaSegura);

        // Agora sim, salva no banco de dados
        usuarioRepository.salvar(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.listarTodos();
    }

    // ---------------------------------------------------------
    // NOVO MÉTODO: O jeito certo de fazer LOGIN com BCrypt
    // ---------------------------------------------------------
    public Usuario autenticarLogin(String email, String senhaDigitada) {

        // Passo 1: Busca o usuário no banco usando APENAS o e-mail
        Usuario usuarioEncontrado = usuarioRepository.buscarPorEmail(email);

        // Passo 2: Se o usuário existe, vamos comparar a senha
        if (usuarioEncontrado != null) {

            // O PasswordUtil vai fazer a matemática complexa para ver se batem
            boolean senhaCorreta = PasswordUtil.verificar(senhaDigitada, usuarioEncontrado.getSenha());

            if (senhaCorreta) {
                return usuarioEncontrado;
            }
        }

        // Se o e-mail não existir OU a senha estiver errada, retorna nulo
        return null;
    }
    public List<Usuario> listarRanking() {
        return usuarioRepository.listarRanking();
    }

    public void atualizar(Usuario usuario) {
        usuarioRepository.atualizar(usuario);
    }

    public void excluir(Usuario usuario) {
        usuarioRepository.excluir(usuario);
    }
}