package com.chutaai.model.repository;

import com.chutaai.model.entity.Bolao;
import com.chutaai.model.entity.Usuario;
import com.chutaai.util.JPAUtil; // Importação correta maiúscula

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@ApplicationScoped
public class BolaoRepository {


    public void salvar(Bolao bolao) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(bolao);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao salvar bolão: " + e.getMessage());
        }
    }

    public Bolao buscarPorCodigo(String codigo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT b FROM Bolao b WHERE b.codigoAcesso = :codigo", Bolao.class)
                    .setParameter("codigo", codigo)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Busca todos os bolões onde o usuário está na lista de participantes
    public List<Bolao> listarPorUsuario(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        return em.createQuery("SELECT b FROM Bolao b JOIN b.participantes p WHERE p = :usuario", Bolao.class)
                .setParameter("usuario", usuario)
                .getResultList();
    }

    public Bolao buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        return em.find(Bolao.class, id);
    }
    public void atualizar(Bolao bolao) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(bolao);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Erro ao atualizar bolão: " + e.getMessage());
        }
    }

    public void excluir(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Bolao b = em.find(Bolao.class, id);
            if (b != null) {
                b.getParticipantes().clear();
                em.remove(b);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Erro ao excluir bolão: " + e.getMessage());
        }
    }
}