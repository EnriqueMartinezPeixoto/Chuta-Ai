package com.chutaai.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
@NamedQueries({
        @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),

        @NamedQuery(name = "Usuario.login", query = "SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha"),

        @NamedQuery(name = "Usuario.findByEmail", query = "SELECT u FROM Usuario u WHERE u.email = :email"),

        @NamedQuery(name = "Usuario.ranking", query = "SELECT u FROM Usuario u ORDER BY u.pontosTotal DESC")

})
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String perfil = "APOSTADOR"; // O padrão é sempre ser apostador comum

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String senha;

    private int pontosTotal = 0;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Palpite> palpites = new ArrayList<>();

    // Construtor vazio obrigatório pelo visto
    public Usuario() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public int getPontosTotal() { return pontosTotal; }
    public void setPontosTotal(int pontosTotal) { this.pontosTotal = pontosTotal; }

    public List<Palpite> getPalpites() { return palpites; }
    public void setPalpites(List<Palpite> palpites) { this.palpites = palpites; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }
}