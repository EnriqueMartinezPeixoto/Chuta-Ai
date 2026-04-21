package com.chutaai.model.repository;

import com.chutaai.model.entity.Usuario;
import javax.inject.Inject; // MUDA O IMPORT
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class UsuarioRepository {

    @Inject
    private EntityManager em;

    public void salvar(Usuario usuario) {
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Desfaz se der erro para não travar o banco
            }
            e.printStackTrace(); // Isso vai mostrar o erro REAL no console do IntelliJ
            throw e; // Repassa o erro para o Controller exibir a mensagem rosa
        }
    }

    public Usuario buscarPorEmail(String email) {
        try {
            // Usando a NamedQuery "Usuario.findByEmail" que criamos na Entidade
            TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByEmail", Usuario.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (Exception e) {
            // Se não achar ninguém com esse e-mail, retorna null sem quebrar o sistema
            return null;
        }
    }

    public List<Usuario> listarTodos() {
        // Usando a NamedQuery "Usuario.findAll"
        return em.createNamedQuery("Usuario.findAll", Usuario.class).getResultList();
    }

    public List<Usuario> listarRanking() {
        return em.createNamedQuery("Usuario.ranking", Usuario.class).getResultList();
    }

    public void atualizar(Usuario usuario) {
        try {
            em.getTransaction().begin();
            em.merge(usuario);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao atualizar usuário no banco: " + e.getMessage());
        }
    }

    public void excluir(Usuario usuario) {
        try {
            em.getTransaction().begin();
            Usuario usuarioGerenciado = em.merge(usuario);
            em.remove(usuarioGerenciado);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao excluir. O usuário já deve ter palpites cadastrados.");
        }
    }
}