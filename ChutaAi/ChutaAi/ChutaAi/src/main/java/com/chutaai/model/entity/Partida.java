package com.chutaai.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "partida")
@NamedQueries({
        @NamedQuery(name = "Partida.findAll", query = "SELECT p FROM Partida p ORDER BY p.dataHora ASC"),

        @NamedQuery(name = "Partida.findAbertas", query = "SELECT p FROM Partida p WHERE p.placarCasaReal IS NULL ORDER BY p.dataHora ASC"),

        @NamedQuery(name = "Partida.findEncerradas", query = "SELECT p FROM Partida p WHERE p.placarCasaReal IS NOT NULL ORDER BY p.dataHora DESC"),

        @NamedQuery(name = "Partida.findByFase", query = "SELECT p FROM Partida p WHERE p.fase = :fase ORDER BY p.dataHora ASC")
})
public class Partida implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String timeCasa;

    @Column(nullable = false)
    private String timeFora;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dataHora;

    private Integer placarCasaReal;
    private Integer placarForaReal;

    @Column(nullable = false)
    private String fase;  // Ex: "Fase de Grupos", "Oitavas", "Quartas", etc.

    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL)
    private List<Palpite> palpites = new ArrayList<>();

    // Construtor vazio
    public Partida() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTimeCasa() { return timeCasa; }
    public void setTimeCasa(String timeCasa) { this.timeCasa = timeCasa; }

    public String getTimeFora() { return timeFora; }
    public void setTimeFora(String timeFora) { this.timeFora = timeFora; }

    public Date getDataHora() { return dataHora; }
    public void setDataHora(Date dataHora) { this.dataHora = dataHora; }

    public Integer getPlacarCasaReal() { return placarCasaReal; }
    public void setPlacarCasaReal(Integer placarCasaReal) { this.placarCasaReal = placarCasaReal; }

    public Integer getPlacarForaReal() { return placarForaReal; }
    public void setPlacarForaReal(Integer placarForaReal) { this.placarForaReal = placarForaReal; }

    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }

    public List<Palpite> getPalpites() { return palpites; }
    public void setPalpites(List<Palpite> palpites) { this.palpites = palpites; }
}