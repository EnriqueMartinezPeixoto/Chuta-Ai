package com.chutaai.service;

import com.chutaai.model.entity.Partida;
import com.chutaai.model.repository.PartidaRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@ApplicationScoped
public class PartidaService {

    @Inject
    private PartidaRepository partidaRepository;

    public void salvar(Partida partida) {
        partidaRepository.salvar(partida);
    }

    public List<Partida> listarTodas() {
        return partidaRepository.listarTodas();
    }

    public Partida buscarPorId(Long id) {
        return partidaRepository.buscarPorId(id);
    }
}