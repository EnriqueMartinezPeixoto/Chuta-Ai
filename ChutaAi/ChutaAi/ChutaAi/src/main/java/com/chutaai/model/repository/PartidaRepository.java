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
        em.getTransaction().begin();
        em.persist(partida);
        em.getTransaction().commit();
    }

    // 2. READ (Buscar por ID)
    public Partida buscarPorId(Long id) {
        return em.find(Partida.class, id);
    }

    // 3. READ ALL (Usando a NamedQuery geral)
    public List<Partida> listarTodas() {
        try {
            // Use o nome da CLASSE (Partida) e não da tabela
            return em.createQuery("SELECT p FROM Partida p", Partida.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // Retorna lista vazia se o banco falhar
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