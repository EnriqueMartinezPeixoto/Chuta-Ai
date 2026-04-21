package com.chutaai.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bolao")
public class Bolao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    // O código de 6 letras/números que o dono vai compartilhar no WhatsApp
    @Column(nullable = false, unique = true)
    private String codigoAcesso;

    // Quem criou o bolão (o dono da bola)
    @ManyToOne
    @JoinColumn(name = "dono_id", nullable = false)
    private Usuario dono;

    // A lista de todos os usuários que entraram com o código
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "bolao_participantes",
            joinColumns = @JoinColumn(name = "bolao_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private List<Usuario> participantes = new ArrayList<>();

    // ==========================================
    // GETTERS E SETTERS
    // ==========================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCodigoAcesso() { return codigoAcesso; }
    public void setCodigoAcesso(String codigoAcesso) { this.codigoAcesso = codigoAcesso; }

    public Usuario getDono() { return dono; }
    public void setDono(Usuario dono) { this.dono = dono; }

    public List<Usuario> getParticipantes() { return participantes; }
    public void setParticipantes(List<Usuario> participantes) { this.participantes = participantes; }
}