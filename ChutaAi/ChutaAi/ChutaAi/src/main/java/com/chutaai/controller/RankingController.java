package com.chutaai.controller;

import com.chutaai.model.entity.Usuario;
import com.chutaai.service.UsuarioService;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class RankingController {

    @Inject
    private UsuarioService usuarioService;

    private List<Usuario> listaRanking;

    @PostConstruct
    public void init() {
        listaRanking = usuarioService.listarRanking();
    }

    public List<Usuario> getListaRanking() {
        return listaRanking;
    }
}