package com.chutaai.model.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "palpite")
@NamedQueries({
        @NamedQuery(name = "Palpite.findAll", query = "SELECT p FROM Palpite p"),
        @NamedQuery(name = "Palpite.findByUsuario", query = "SELECT p FROM Palpite p WHERE p.usuario = :usuario"),
        @NamedQuery(name = "Palpite.findByPartida", query = "SELECT p FROM Palpite p WHERE p.partida = :partida"),
        @NamedQuery(name = "Palpite.findByUsuarioAndPartida", query = "SELECT p FROM Palpite p WHERE p.usuario = :usuario AND p.partida = :partida")
})
public class Palpite implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "partida_id", nullable = false)
    private Partida partida;

    @Column(nullable = false)
    private Integer palpiteCasa;

    @Column(nullable = false)
    private Integer palpiteFora;

    private Integer pontos = 0;

    // Construtor vazio obrigatório pelo JPA
    public Palpite() {}

    // --- GETTERS E SETTERS (GERADOS PARA CORRIGIR OS ERROS) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public Integer getPalpiteCasa() {
        return palpiteCasa;
    }

    public void setPalpiteCasa(Integer palpiteCasa) {
        this.palpiteCasa = palpiteCasa;
    }

    public Integer getPalpiteFora() {
        return palpiteFora;
    }

    public void setPalpiteFora(Integer palpiteFora) {
        this.palpiteFora = palpiteFora;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }
}