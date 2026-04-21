package com.chutaai.model.repository;

import com.chutaai.model.entity.Palpite;
import com.chutaai.model.entity.Partida;
import com.chutaai.model.entity.Usuario;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class PalpiteRepository {

    @Inject
    private EntityManager em;

    // 1. CREATE / UPDATE (Salvar ou Editar)
    public void salvar(Palpite palpite) {
        em.getTransaction().begin();
        if (palpite.getId() == null) {
            em.persist(palpite); // É novo, insere
        } else {
            em.merge(palpite);   // Já existe, atualiza
        }
        em.getTransaction().commit();
    }

    // 2. READ ALL (Usando a NamedQuery)
    public List<Palpite> listarTodos() {
        return em.createNamedQuery("Palpite.findAll", Palpite.class).getResultList();
    }

    // 3. READ POR USUÁRIO (Tela "Meus Palpites")
    public List<Palpite> listarPorUsuario(Usuario usuario) {
        return em.createNamedQuery("Palpite.findByUsuario", Palpite.class)
                .setParameter("usuario", usuario)
                .getResultList();
    }

    // --- MÉTODOS EXTRAS USANDO AS NAMED QUERIES ---

    // Busca todos os palpites feitos em um jogo específico
    public List<Palpite> listarPorPartida(Partida partida) {
        return em.createNamedQuery("Palpite.findByPartida", Palpite.class)
                .setParameter("partida", partida)
                .getResultList();
    }

    // REGRA DE SEGURANÇA: Descobre se o cara JÁ palpitou nesse jogo!
    public Palpite buscarPorUsuarioEPartida(Usuario usuario, Partida partida) {
        try {
            return em.createNamedQuery("Palpite.findByUsuarioAndPartida", Palpite.class)
                    .setParameter("usuario", usuario)
                    .setParameter("partida", partida)
                    .getSingleResult();
        } catch (Exception e) {
            // Se cair no catch, significa que ele não palpitou ainda (o que é ótimo!)
            return null;
        }
    }

    // 4. DELETE (Caso queira dar a opção do usuário apagar o palpite)
    public void excluir(Long id) {
        Palpite palpite = em.find(Palpite.class, id);
        if (palpite != null) {
            em.getTransaction().begin();
            em.remove(palpite);
            em.getTransaction().commit();
        }
    }

    public List<Palpite> listarTodosPorUsuario(Usuario u) {
        return em.createNamedQuery("Palpite.findByUsuario", Palpite.class)
                .setParameter("usuario", u)
                .getResultList();
    }

    // Puxa todos os palpites feitos em um jogo específico
    public List<Palpite> buscarPorPartida(Partida partida) {
        return em.createQuery("SELECT p FROM Palpite p WHERE p.partida = :partida", Palpite.class)
                .setParameter("partida", partida)
                .getResultList();
    }

    public void atualizar(Palpite palpite) {
        try {
            em.getTransaction().begin();
            em.merge(palpite);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao atualizar palpite: " + e.getMessage());
        }
    }
}