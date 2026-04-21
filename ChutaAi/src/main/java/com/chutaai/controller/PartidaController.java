package com.chutaai.controller;

import com.chutaai.model.entity.Partida;
import com.chutaai.service.PartidaService;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class PartidaController implements Serializable {

    @Inject
    private PartidaService partidaService;

    private List<Partida> listaPartidas;

    @PostConstruct
    public void init() {
        // Busca os jogos reais do MySQL
        listaPartidas = partidaService.listarTodas();
    }

    public List<Partida> getListaPartidas() {
        return listaPartidas;
    }
}