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
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw e;
        }
    }

    public void atualizar(Usuario usuario) {
        // Abre a transação, atualiza e depois "commita" no banco
        em.getTransaction().begin();
        em.merge(usuario);
        em.getTransaction().commit();
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

    // BÔNUS: Já adicionei o método do Ranking que vai ser muito útil pro bolão!
    public List<Usuario> listarRanking() {
        return em.createNamedQuery("Usuario.ranking", Usuario.class).getResultList();
    }
}