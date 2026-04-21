package com.chutaai.model.repository;

import com.chutaai.model.entity.Partida;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

public class PartidaRepository {

    @Inject
    private EntityManager em;

    // 1. CREATE
    public void salvar(Partida partida) {
        try {
            em.getTransaction().begin();

            em.merge(partida);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao salvar no banco: " + e.getMessage());
        }
    }

    // 2. READ (Buscar por ID)
    public Partida buscarPorId(Long id) {
        return em.find(Partida.class, id);
    }

    // 3. READ ALL (Usando a NamedQuery geral)
    public List<Partida> listarTodas() {
        try {
            return em.createQuery("SELECT p FROM Partida p", Partida.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    // -------------------------------------------

    // 4. UPDATE
    public void atualizar(Partida partida) {
        em.getTransaction().begin();
        em.merge(partida);
        em.getTransaction().commit();
    }

    // 5. DELETE
    public void excluir(Long id) {
        Partida partida = buscarPorId(id);
        if (partida != null) {
            em.getTransaction().begin();
            em.remove(partida);
            em.getTransaction().commit();
        }
    }
}